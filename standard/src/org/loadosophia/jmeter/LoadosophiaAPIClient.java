package org.loadosophia.jmeter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.GZIPOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class LoadosophiaAPIClient {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String COLOR_NONE = "none";
    public static final String[] colors = {COLOR_NONE, "red", "green", "blue", "gray", "orange", "violet", "cyan", "black"};
    public static final String STATUS_DONE = "4";
    private final HttpClient httpClient = new HttpClient();
    private final StatusNotifierCallback notifier;
    private final String project;
    private final String address;
    private final String token;
    private final String colorFlag;
    private final String title;

    public LoadosophiaAPIClient(StatusNotifierCallback informer, String aAddress, String aToken, String projectName, String aColorFlag, String aTitle) {
        project = projectName;
        address = aAddress;
        token = aToken;
        notifier = informer;
        colorFlag = aColorFlag;
        title = aTitle;
    }

    public LoadosophiaUploadResults sendFiles(File targetFile, LinkedList<String> perfMonFiles) throws IOException {
        LoadosophiaUploadResults results = new LoadosophiaUploadResults();
        if (targetFile.length() == 0) {
            throw new IOException("Cannot send empty file to Loadosophia.org");
        }

        log.info("Preparing files to send");
        LinkedList<Part> partsList = new LinkedList<Part>();
        partsList.add(new StringPart("projectKey", project));
        partsList.add(new FilePart("jtl_file", new FilePartSource(gzipFile(targetFile))));

        Iterator<String> it = perfMonFiles.iterator();
        int index = 0;
        while (it.hasNext()) {
            File perfmonFile = new File(it.next());
            if (!perfmonFile.exists()) {
                log.warn("File not exists, skipped: " + perfmonFile.getAbsolutePath());
                continue;
            }
            partsList.add(new FilePart("perfmon_" + index, new FilePartSource(gzipFile(perfmonFile))));
            perfmonFile.delete();
            index++;
        }

        notifier.notifyAbout("Starting upload to Loadosophia.org");
        String[] fields = multipartPost(partsList, getUploaderURI(), HttpStatus.SC_OK);
        int queueID = Integer.parseInt(fields[0]);
        results.setQueueID(queueID);

        if (!title.trim().isEmpty() || !colorFlag.equals(COLOR_NONE)) {
            int testID = getTestByUpload(queueID);
            results.setTestID(testID);

            if (!title.trim().isEmpty()) {
                setTestTitle(testID, title.trim());
            }

            if (!colorFlag.equals(COLOR_NONE)) {
                setTestColor(testID, colorFlag);
            }

            results.setRedirectLink(address + "gui/" + testID + "/");
        } else {
            results.setRedirectLink(address + "api/file/status/" + queueID + "/?redirect=true");
        }
        return results;
    }

    public String startOnline() throws IOException {
        String uri = address + "api/active/receiver/start/";
        LinkedList<Part> partsList = new LinkedList<Part>();
        partsList.add(new StringPart("token", token));
        partsList.add(new StringPart("projectKey", project));
        partsList.add(new StringPart("title", title));
        String[] res = multipartPost(partsList, uri, HttpStatus.SC_CREATED);
        JSONObject obj = JSONObject.fromObject(res[0]);
        return address + "gui/active/" + obj.optString("OnlineID", "N/A") + "/";
    }

    public void sendOnlineData(JSONArray data) throws IOException {
        String uri = address + "api/active/receiver/data/";
        LinkedList<Part> partsList = new LinkedList<Part>();
        String dataStr = data.toString();
        log.debug("Sending active test data: " + dataStr);
        partsList.add(new StringPart("data", dataStr));
        multipartPost(partsList, uri, HttpStatus.SC_ACCEPTED);
    }

    public void endOnline() throws IOException {
        String uri = address + "api/active/receiver/stop/";
        LinkedList<Part> partsList = new LinkedList<Part>();
        multipartPost(partsList, uri, HttpStatus.SC_RESET_CONTENT);
    }

    private File gzipFile(File src) throws IOException {
        // Never try to make it stream-like on the fly, because content-length still required
        // Create the GZIP output stream
        String outFilename = src.getAbsolutePath() + ".gz";
        notifier.notifyAbout("Gzipping " + src.getAbsolutePath());
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outFilename));

        // Open the input file
        FileInputStream in = new FileInputStream(src);

        // Transfer bytes from the input file to the GZIP output stream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();

        // Complete the GZIP file
        out.finish();
        out.close();

        src.delete();

        return new File(outFilename);
    }

    private int getTestByUpload(int queueID) throws IOException {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Failed to get test ID");
            }

            String[] status = getUploadStatus(queueID);
            if (status.length > 2 && !status[2].isEmpty()) {
                throw new RuntimeException("Loadosophia processing error: " + status[2]);
            }

            if (status[1].equals(STATUS_DONE)) {
                return Integer.parseInt(status[0]);
            }
        }
    }

    private void setTestTitle(int testID, String trim) throws IOException {
        String uri = address + "api/test/edit/title/" + testID + "/?title=" + URLEncoder.encode(trim, "UTF-8");
        multipartPost(new LinkedList<Part>(), uri, HttpStatus.SC_NO_CONTENT);
    }

    private void setTestColor(int testID, String colorFlag) throws IOException {
        String uri = address + "api/test/edit/color/" + testID + "/?color=" + colorFlag;
        multipartPost(new LinkedList<Part>(), uri, HttpStatus.SC_NO_CONTENT);
    }

    private String getUploaderURI() {
        return address + "api/file/upload/?format=csv";
    }

    protected String[] getUploadStatus(int queueID) throws IOException {
        String uri = address + "api/file/status/" + queueID + "/?format=csv";
        return multipartPost(new LinkedList<Part>(), uri, HttpStatus.SC_OK);
    }

    protected String[] multipartPost(LinkedList<Part> parts, String URL, int expectedSC) throws IOException {
        log.debug("Request POST: " + URL);
        parts.add(new StringPart("token", token));

        PostMethod postRequest = new PostMethod(URL);
        MultipartRequestEntity multipartRequest = new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), postRequest.getParams());
        postRequest.setRequestEntity(multipartRequest);
        int result = httpClient.executeMethod(postRequest);
        if (result != expectedSC) {
            String fname = File.createTempFile("error_", ".html").getAbsolutePath();
            notifier.notifyAbout("Saving server error response to: " + fname);
            FileOutputStream fos = new FileOutputStream(fname);
            FileChannel resultFile = fos.getChannel();
            resultFile.write(ByteBuffer.wrap(postRequest.getResponseBody()));
            resultFile.close();
            throw new HttpException("Request returned not " + expectedSC + " status code: " + result);
        }
        byte[] bytes = postRequest.getResponseBody();
        if (bytes == null) {
            bytes = new byte[0];
        }
        String response = new String(bytes);
        return response.trim().split(";");
    }
}
