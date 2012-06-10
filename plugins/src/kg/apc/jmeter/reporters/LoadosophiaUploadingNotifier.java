package kg.apc.jmeter.reporters;

import java.util.LinkedList;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class LoadosophiaUploadingNotifier {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static LoadosophiaUploadingNotifier instance;
    private LinkedList<String> files = new LinkedList<String>();

    private LoadosophiaUploadingNotifier() {
    }

    public static LoadosophiaUploadingNotifier getInstance() {
        if (instance == null) {
            instance = new LoadosophiaUploadingNotifier();
        }

        return instance;
    }

    public void startCollecting() {
        log.debug("Clearing files list, start files collection");
        files.clear();
    }

    public void endCollecting() {
        log.debug("Ended files collection");
    }

    public LinkedList<String> getFiles() {
        return files;
    }

    public void addFile(String file) {
        log.debug("Add new file: " + file);
        files.add(file);
    }
}
