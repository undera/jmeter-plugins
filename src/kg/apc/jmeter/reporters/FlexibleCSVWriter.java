package kg.apc.jmeter.reporters;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 * @see ResultCollector
 */
public class FlexibleCSVWriter
        extends AbstractListenerElement
        implements SampleListener, Serializable,
        TestListener, Remoteable, NoThreadClone {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String FILENAME = "filename";
    private static final String COLUMNS = "columns";
    protected FileChannel fileChannel;

    public void sampleOccurred(SampleEvent e) {
        if (fileChannel == null || !fileChannel.isOpen()) {
            return;
        }
    }

    public void sampleStarted(SampleEvent e) {
    }

    public void sampleStopped(SampleEvent e) {
    }

    public void testStarted() {
        compileColumns();
        try {
            openFile();
        } catch (FileNotFoundException ex) {
            log.error("Cannot open file " + getFilename(), ex);
        }
    }

    public void testStarted(String host) {
        testStarted();
    }

    public void testEnded() {
        closeFile();
    }

    public void testEnded(String host) {
        testEnded();
    }

    public void testIterationStart(LoopIterationEvent event) {
    }

    public void setFilename(String name) {
        setProperty(FILENAME, name);
    }

    public String getFilename() {
        return getPropertyAsString(FILENAME);
    }

    public void setColumns(String cols) {
        setProperty(COLUMNS, cols);
    }

    public String getColumns() {
        return getPropertyAsString(COLUMNS);
    }

    private void compileColumns() {
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    protected void openFile() throws FileNotFoundException {
        String filename = getFilename();
        FileOutputStream fos = new FileOutputStream(filename, true);
        fileChannel = fos.getChannel();
    }

    private void closeFile() {
        if (fileChannel != null && fileChannel.isOpen()) {
            try {
                fileChannel.close();
            } catch (IOException ex) {
                log.error("Failed to close file: " + getFilename(), ex);
            }
        }
    }
}
