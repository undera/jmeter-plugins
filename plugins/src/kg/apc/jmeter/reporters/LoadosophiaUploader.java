package kg.apc.jmeter.reporters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class LoadosophiaUploader extends ResultCollector implements TestListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String URI = "uploaderURI";
    public static final String FILE_PREFIX = "filePrefix";
    public static final String UPLOAD_TOKEN = "uploadToken";
    public static final String PROJECT = "project";
    public static final String STORE_DIR = "storeDir";
    private String fileName;
    private static final Object LOCK = new Object();
    private boolean isSaving;
    private LoadosophiaUploadingNotifier perfMonNotifier = LoadosophiaUploadingNotifier.getInstance();

    public LoadosophiaUploader() {
        super();
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
                sendFilesToLoadosophia(new File(fileName), perfMonNotifier.getFiles());
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
            tmpFile = File.createTempFile(getFilePrefix() + "_", ".jtl", new File(dir));
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

    private void sendFilesToLoadosophia(File targetFile, LinkedList<String> perfMonFiles) throws IOException {
        if (targetFile.length() == 0) {
            throw new IOException("Cannot send empty file to Loadosophia.org");
        }

        log.info("Preparing files to send");
        LinkedList<Part> partsList = new LinkedList<Part>();
        partsList.add(new StringPart("projectKey", getProject()));
        partsList.add(new StringPart("uploadToken", getUploadToken()));
        partsList.add(new FilePart("jtl_file", new FilePartSource(gzipFile(targetFile))));
        targetFile.delete();

        Iterator<String> it = perfMonFiles.iterator();
        int index=0;
        while (it.hasNext()) {
            File perfmonFile = new File(it.next());
            if (!perfmonFile.exists()) {
                log.debug("File not exists, skipped: "+perfmonFile.getAbsolutePath());
                continue;
            }
            partsList.add(new FilePart("perfmon_" + index, new FilePartSource(gzipFile(perfmonFile))));
            perfmonFile.delete();
            index++;
        }
        Part[] parts =  partsList.toArray(new Part[0]);

        informUser("Starting upload to Loadosophia.org");
        HttpClient uploader = new HttpClient();
        PostMethod filePost = new PostMethod(getUploaderURI());
        MultipartRequestEntity multipartRequest = new MultipartRequestEntity(parts, filePost.getParams());
        filePost.setRequestEntity(multipartRequest);

        int result = uploader.executeMethod(filePost);
        if (result != HttpStatus.SC_ACCEPTED) {
            String fname = targetFile.getAbsolutePath() + ".html";
            informUser("Saving server error response to: " + fname);
            FileOutputStream fos = new FileOutputStream(fname);
            FileChannel resultFile = fos.getChannel();
            resultFile.write(ByteBuffer.wrap(filePost.getResponseBody()));
            resultFile.close();
            HttpException $e = new HttpException("Upload returned not 202 ACCEPTED status: " + result);
            throw $e;
        }

        informUser("Finished upload to Loadosophia.org successfully");
        informUser("Go to https://loadosophia.org/service/upload/ to see processing status.");
    }

    private File gzipFile(String perfmonFile) throws IOException {
        return gzipFile(new File(perfmonFile));
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
        if (getVisualizer() != null && getVisualizer() instanceof LoadosophiaUploaderGui) {
            ((LoadosophiaUploaderGui) getVisualizer()).inform(string);
        } else {
            log.info("No Visualizer");
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
      //TODO: should be handled ?
   }
}
