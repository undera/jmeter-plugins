package kg.apc.jmeter.reporters;

import java.io.File;
import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.jmeter.reporters.ResultCollector;
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
    private String fileName;

    public LoadosophiaUploader() {
        super();
    }

    @Override
    public void testStarted() {
        super.testStarted();
        File tmp;
        try {
            tmp = File.createTempFile(getFilePrefix() + "_", ".jtl");
            fileName = tmp.getAbsolutePath();
            log.info("Storing results for upload to Loadosophia.org: " + fileName);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LoadosophiaUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
            log.error("Failed to upload results to loadosophia", ex);
        }
    }

    private void sendJTLToLoadosophia(File targetFile) throws IOException {
        log.info("Starting upload to Loadosophia.org");
        HttpClient uploader = new HttpClient();
        PostMethod filePost = new PostMethod(getUploaderURI());
        Part[] parts = {
            // TODO: gzip file optionally/mandatory
            new FilePart(targetFile.getName(), targetFile)
        };
        filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

        int result = uploader.executeMethod(filePost);
        if (result != HttpStatus.SC_ACCEPTED) {
            HttpException $e = new HttpException("Upload returned not 202 ACCEPTED status: " + result);
            throw $e;
        }

        log.info("Finished upload to Loadosophia.org");
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

    private String getFilePrefix() {
        return getPropertyAsString(FILE_PREFIX);
    }
}
