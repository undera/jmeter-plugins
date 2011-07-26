package kg.apc.jmeter.reporters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
// TODO: how will it go in distributed test?
public class LoadosophiaUploader extends ResultCollector implements TestListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String URI = "uploaderURI";
    public static final String FILE_PREFIX = "filePrefix";
    public static final String UPLOAD_TOKEN = "uploadToken";
    public static final String PROJECT = "project";
    private String fileName;

    public LoadosophiaUploader() {
        super();
    }

    @Override
    public void testStarted() {
        try {
            setupSaving();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LoadosophiaUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        super.testStarted();

    }

    private void setupSaving() throws IOException {
        File tmp = File.createTempFile(getFilePrefix() + "_", ".jtl");
        tmp.delete();
        tmp.deleteOnExit();
        fileName = tmp.getAbsolutePath();
        informUser("Storing results for upload to Loadosophia.org: " + fileName);
        setFilename(fileName);

        SampleSaveConfiguration conf = new SampleSaveConfiguration();
        conf.setFormatter(null);
        conf.setSamplerData(false);
        conf.setRequestHeaders(false);
        conf.setFileName(false);
        conf.setIdleTime(false);
        conf.setSuccess(true);
        conf.setMessage(true);
        conf.setEncoding(false);
        conf.setThreadCounts(true);
        conf.setFieldNames(true);
        conf.setAssertions(false);
        conf.setResponseData(false);
        conf.setSubresults(false);
        conf.setLatency(true);
        conf.setLabel(true);
        conf.setThreadName(false);
        conf.setBytes(true);
        conf.setHostname(false);
        conf.setAssertionResultsFailureMessage(false);
        conf.setResponseHeaders(false);
        conf.setUrl(false);
        conf.setTime(true);
        conf.setTimestamp(true);
        conf.setCode(true);
        conf.setDataType(false);
        conf.setSampleCount(false);

        conf.setAsXml(false);
        conf.setFieldNames(true);
        setSaveConfig(conf);
    }

    @Override
    public void testEnded() {
        super.testEnded();
        try {
            if (fileName == null) {
                throw new IOException("File for upload was not set, search for errors above in log");
            }

            sendJTLToLoadosophia(new File(fileName));
        } catch (IOException ex) {
            informUser("Failed to upload results to Loadosophia.org, see log for detais");
            log.error("Failed to upload results to loadosophia", ex);
        }
        clearData();
        informUser("");
    }

    private void sendJTLToLoadosophia(File targetFile) throws IOException {
        informUser("Starting upload to Loadosophia.org");
        HttpClient uploader = new HttpClient();
        PostMethod filePost = new PostMethod(getUploaderURI());
        Part[] parts = {
            // TODO: gzip file optionally/mandatory
            new StringPart("projectKey", getProject()),
            new StringPart("uploadToken", getUploadToken()),
            new FilePart("jtl_file", new FilePartSource(gzipFile(targetFile)))
        };
        filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

        int result = uploader.executeMethod(filePost);
        if (result != HttpStatus.SC_ACCEPTED) {
            HttpException $e = new HttpException("Upload returned not 202 ACCEPTED status: " + result);
            throw $e;
        }

        informUser("Finished upload to Loadosophia.org");
        informUser("Go to https://loadosophia.org/service/upload/ to see processing status.");
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

    public void setUploaderURI(String uri) {
        setProperty(URI, uri);
    }

    public String getUploaderURI() {
        return getPropertyAsString(URI);
    }

    public void setFilePrefix(String prefix) {
        setProperty(FILE_PREFIX, prefix);
    }

    public String getFilePrefix() {
        return getPropertyAsString(FILE_PREFIX);
    }

    private void informUser(String string) {
        log.info(string);
        // TODO: test in non-gui mode!
        if (getVisualizer() != null && getVisualizer() instanceof LoadosophiaUploaderGui) {
            ((LoadosophiaUploaderGui) getVisualizer()).inform(string);
        }
    }
}
