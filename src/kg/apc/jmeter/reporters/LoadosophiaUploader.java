package kg.apc.jmeter.reporters;

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
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class LoadosophiaUploader extends ResultCollector implements TestListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String ADDRESS = "address";
    public static final String TITLE = "title";
    public static final String COLOR = "color";
    public static final String UPLOAD_TOKEN = "uploadToken";
    public static final String PROJECT = "project";
    public static final String STORE_DIR = "storeDir";
    private String fileName;
    private static final Object LOCK = new Object();
    private boolean isSaving;
    private LoadosophiaUploadingNotifier perfMonNotifier = LoadosophiaUploadingNotifier.getInstance();
    private String address;
    public static final String COLOR_NONE = "none";
    public static final String[] colors = {COLOR_NONE, "red", "green", "blue", "gray", "orange", "violet", "cyan", "black"};
    private static final String STATUS_DONE = "4";

    public LoadosophiaUploader() {
        super();
        address = JMeterUtils.getPropDefault("loadosophia.address", "https://loadosophia.org/");
    }

    @Override
    public void testStarted(String host) {
        synchronized (LOCK) {
            try {
                if (!isSaving) {
                    isSaving = true;
                    setupSaving();
                }
            } catch (IOException ex) {
                log.error("Error setting up saving", ex);
            }
        }
        super.testStarted(host);
        perfMonNotifier.startCollecting();
    }

    @Override
    public void testEnded(String host) {
        super.testEnded(host);
        synchronized (LOCK) {
            try {
                if (fileName == null) {
                    throw new IOException("File for upload was not set, search for errors above in log");
                }

                isSaving = false;
                int queueID = sendFilesToLoadosophia(new File(fileName), perfMonNotifier.getFiles());
                String results;
                if (!getTitle().trim().isEmpty() || !getColorFlag().equals(COLOR_NONE)) {
                    int testID = getTestByUpload(queueID);

                    if (!getTitle().trim().isEmpty()) {
                        setTestTitle(testID, getTitle().trim());
                    }

                    if (!getColorFlag().equals(COLOR_NONE)) {
                        setTestColor(testID, getColorFlag());
                    }

                    results = address + "gui/" + testID + "/";
                } else {
                    results = address + "api/file/status/" + queueID + "/?redirect=true";
                }

                informUser("Uploaded successfully, go to results: " + results);
            } catch (IOException ex) {
                informUser("Failed to upload results to Loadosophia.org, see log for detais");
                log.error("Failed to upload results to loadosophia", ex);
            }
        }
        clearData();
        perfMonNotifier.endCollecting();
    }

    private void setupSaving() throws IOException {
        String dir = getStoreDir();
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }

        File tmpFile;
        try {
            tmpFile = File.createTempFile("Loadosophia_", ".jtl", new File(dir));
        } catch (IOException ex) {
            informUser("Unable to create temp file: " + ex.getMessage());
            informUser("Try to set another directory in the above field.");
            throw ex;
        }

        fileName = tmpFile.getAbsolutePath();
        tmpFile.delete(); // IMPORTANT! this is required to have CSV headers
        informUser("Storing results for upload to Loadosophia.org: " + fileName);
        setFilename(fileName);
        // OMG, I spent 2 days finding that setting properties in testStarted
        // marks them temporary, though they cleared in some places.
        // So we do dirty(?) hack here...
        clearTemporary(getProperty(FILENAME));

        SampleSaveConfiguration conf = (SampleSaveConfiguration) getSaveConfig();
        JMeterPluginsUtils.doBestCSVSetup(conf);

        setSaveConfig(conf);
    }

    private int sendFilesToLoadosophia(File targetFile, LinkedList<String> perfMonFiles) throws IOException {
        if (targetFile.length() == 0) {
            throw new IOException("Cannot send empty file to Loadosophia.org");
        }

        log.info("Preparing files to send");
        LinkedList<Part> partsList = new LinkedList<Part>();
        partsList.add(new StringPart("projectKey", getProject()));
        partsList.add(new FilePart("jtl_file", new FilePartSource(gzipFile(targetFile))));
        targetFile.delete();

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

        informUser("Starting upload to Loadosophia.org");
        String[] fields = doRequest(partsList, getUploaderURI(), HttpStatus.SC_OK);
        return Integer.parseInt(fields[0]);
    }

    private File gzipFile(File src) throws IOException {
        // Never try to make it stream-like on the fly, because content-length still required
        // Create the GZIP output stream
        String outFilename = src.getAbsolutePath() + ".gz";
        informUser("Gzipping " + src.getAbsolutePath());
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

        return new File(outFilename);
    }

    public void setProject(String proj) {
        setProperty(PROJECT, proj);
    }

    public String getProject() {
        return getPropertyAsString(PROJECT);
    }

    public void setUploadToken(String token) {
        setProperty(UPLOAD_TOKEN, token);
    }

    public String getUploadToken() {
        return getPropertyAsString(UPLOAD_TOKEN);
    }

    public void setTitle(String prefix) {
        setProperty(TITLE, prefix);
    }

    public String getTitle() {
        return getPropertyAsString(TITLE);
    }

    private void informUser(String string) {
        log.info(string);
        if (getVisualizer() != null && getVisualizer() instanceof LoadosophiaUploaderGui) {
            ((LoadosophiaUploaderGui) getVisualizer()).inform(string);
        } else {
            log.info(string);
        }
    }

    public String getStoreDir() {
        return getPropertyAsString(STORE_DIR);
    }

    public void setStoreDir(String prefix) {
        setProperty(STORE_DIR, prefix);
    }

    @Override
    public void testIterationStart(LoopIterationEvent lie) {
    }

    public void setColorFlag(String color) {
        setProperty(COLOR, color);
    }

    public String getColorFlag() {
        return getPropertyAsString(COLOR);
    }

    private String getUploaderURI() {
        return address + "api/file/upload/?format=csv";
    }

    private int getTestByUpload(int queueID) throws IOException {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Failed to get test ID");
            }

            String[] status = get_upload_status(queueID);
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
        doRequest(new LinkedList<Part>(), uri, HttpStatus.SC_NO_CONTENT);
    }

    private void setTestColor(int testID, String colorFlag) throws IOException {
        String uri = address + "api/test/edit/color/" + testID + "/?color=" + colorFlag;
        doRequest(new LinkedList<Part>(), uri, HttpStatus.SC_NO_CONTENT);
    }

    protected String[] get_upload_status(int queueID) throws IOException {
        String uri = address + "api/file/status/" + queueID + "/?format=csv";
        return doRequest(new LinkedList<Part>(), uri, HttpStatus.SC_OK);
    }

    private String[] doRequest(LinkedList<Part> parts, String URL, int expectedSC) throws IOException {
        log.debug("Request " + URL);
        parts.add(new StringPart("token", getUploadToken()));

        HttpClient uploader = new HttpClient();
        PostMethod postRequest = new PostMethod(URL);
        MultipartRequestEntity multipartRequest = new MultipartRequestEntity(parts.toArray(new Part[0]), postRequest.getParams());
        postRequest.setRequestEntity(multipartRequest);
        int result = uploader.executeMethod(postRequest);
        if (result != expectedSC) {
            String fname = File.createTempFile("error_", ".html").getAbsolutePath();
            informUser("Saving server error response to: " + fname);
            FileOutputStream fos = new FileOutputStream(fname);
            FileChannel resultFile = fos.getChannel();
            resultFile.write(ByteBuffer.wrap(postRequest.getResponseBody()));
            resultFile.close();
            HttpException $e = new HttpException("Request returned not " + expectedSC + " status code: " + result);
            throw $e;
        }
        byte[] bytes = postRequest.getResponseBody();
        if (bytes == null) {
            bytes = new byte[0];
        }
        String response = new String(bytes);
        String[] fields = response.trim().split(";");
        return fields;
    }
}
