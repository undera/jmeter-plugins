package kg.apc.jmeter.config;

import java.io.File;
import java.io.IOException;
import kg.apc.io.FileSystem;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterStopTestNowException;
import org.apache.log.Logger;

public class LockFile extends ConfigTestElement
        implements TestListener {

    public static Logger log = LoggingManager.getLoggerForClass();
    public static final String FILENAME = "filename";
    public static final String FILEMASK = "filemask";

    public void testStarted() {
        testStarted(null);
    }

    public void testStarted(String string) {
        log.debug("Test started captured");
        if (getFilename() != null && getFilename().length() > 0) {
            log.info("Checking lockfile at " + getFilename());
            File file = new File(getFilename());
            String path;
            try {
                if (file.getParentFile() != null) {
                    path = file.getParentFile().getCanonicalPath();
                } else {
                    path = new File(".").getCanonicalPath();
                }
            } catch (IOException ex) {
                log.error("Failed to get path");
                throw new JMeterStopTestNowException("Failed to get path");
            }
            log.info("and by wildcard at " + path + getFilemask());
            if (file.exists()) {
                log.error("Lock file found: " + getFilename());
                throw new JMeterStopTestNowException("Lock file found: " + getFilename());
            } else if (getFilemask() != null && getFilemask().length() > 0
                    && FileSystem.checkFileExistByPattern(path, getFilemask())) {
                log.error("Lock file found by pattern " + getFilemask());
                throw new JMeterStopTestNowException("Lock file found by pattern " + getFilemask());
            } else {
                try {
                    log.info("Create lockfile at " + getFilename());
                    file.createNewFile();
                } catch (IOException e) {
                    log.error("Could not create lock file: " + e.getLocalizedMessage());
                    throw new JMeterStopTestNowException("Could not create lock file: " + e.getLocalizedMessage());
                }
            }
        } else {
            log.debug("Filename: " + getFilename());
            log.warn("No lockfile set. Ignore.");
        }
    }

    public void testEnded() {
        testEnded(null);
    }

    public void testEnded(String string) {
        log.debug("Test ended captured");
        if (getFilename() != null && getFilename().length() > 0) {
            File file = new File(getFilename());
            if (file.exists()) {
                log.info("Remove lockfile from " + getFilename());
                file.delete();
            }
        }
    }

    public void testIterationStart(LoopIterationEvent lie) {
        // DO NOTHING
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        log.debug("Return filename: " + getPropertyAsString(FILENAME));
        return getPropertyAsString(FILENAME);
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        log.debug("Set filename to: " + filename);
        setProperty(FILENAME, filename);
    }

    /**
     * @return the filemask
     */
    public String getFilemask() {
        log.debug("Return filemask: " + getPropertyAsString(FILEMASK));
        return getPropertyAsString(FILEMASK);
    }

    /**
     * @param filename the filename to set
     */
    public void setFilemask(String filemask) {
        log.debug("Set filemask to: " + filemask);
        setProperty(FILEMASK, filemask);
    }
}
