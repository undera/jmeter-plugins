// TODO: add startTimeSec - integer epoch seconds only - why startTime does not apply?
// TODO: buffer file writes to bigger chunks?
package kg.apc.jmeter.reporters;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Arrays;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 * @see ResultCollector
 */
public class FlexibleFileWriter
        extends AbstractListenerElement
        implements SampleListener, Serializable,
        TestListener, Remoteable, NoThreadClone {

    public static final String AVAILABLE_FIELDS = "isSuccsessful "
            + "startTime endTime "
            + "sentBytes receivedBytes "
            + "responseTime latency "
            + "responseCode responseMessage "
            + "isFailed " // surrogates
            + "threadName sampleLabel "
            + "startTimeMillis endTimeMillis "
            + "responseTimeMicros latencyMicros "
            + "requestData responseData responseHeaders ";
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String FILENAME = "filename";
    private static final String COLUMNS = "columns";
    protected volatile FileChannel fileChannel;
    private int[] compiledFields;
    private ByteBuffer[] compiledConsts;
    private ArrayList<String> availableFieldNames = new ArrayList<String>(Arrays.asList(AVAILABLE_FIELDS.trim().split(" ")));
    private static final byte[] b1 = "1".getBytes();
    private static final byte[] b0 = "0".getBytes();

    public FlexibleFileWriter() {
        super();
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

    /**
     * making this once to be efficient
     * and avoid manipulating strings
     * in switch operators
     */
    private void compileColumns() {
        log.debug("Compiling columns string: " + getColumns());
        String[] chunks = JMeterPluginsUtils.replaceRNT(getColumns()).split("\\|");
        log.debug("Chunks " + chunks.length);
        compiledFields = new int[chunks.length];
        compiledConsts = new ByteBuffer[chunks.length];
        for (int n = 0; n < chunks.length; n++) {
            int fieldID = availableFieldNames.indexOf(chunks[n]);
            if (fieldID >= 0) {
                //log.debug(chunks[n] + " field id: " + fieldID);
                compiledFields[n] = fieldID;
            } else {
                log.debug(chunks[n] + " is const");
                if (chunks[n].length() == 0) {
                    //log.debug("Empty const, treated as |");
                    chunks[n] = "|";
                }

                compiledConsts[n] = ByteBuffer.wrap(chunks[n].getBytes());
            }
        }
    }

    protected void openFile() throws FileNotFoundException {
        String filename = getFilename();
        FileOutputStream fos = new FileOutputStream(filename, true);
        fileChannel = fos.getChannel();
    }

    private synchronized void closeFile() {
        if (fileChannel != null && fileChannel.isOpen()) {
            try {
                fileChannel.force(false);
                fileChannel.close();
            } catch (IOException ex) {
                log.error("Failed to close file: " + getFilename(), ex);
            }
        }
    }

    public void sampleOccurred(SampleEvent evt) {
        if (fileChannel == null || !fileChannel.isOpen()) {
            if (log.isWarnEnabled()) {
                log.warn("File writer is closed! Maybe test has already been stopped");
            }
            return;
        }

        ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 10);
        for (int n = 0; n < compiledConsts.length; n++) {
            if (compiledConsts[n] != null) {
                buf.put(compiledConsts[n].duplicate()); 
            } else {
                appendSampleResultField(buf, evt.getResult(), compiledFields[n]);
            }
        }

        buf.flip();

        // FIXME: some records are not written to file. Try using old file streams?
        try {
            syncWrite(buf);
        } catch (IOException ex) {
            log.error("Problems writing to file", ex);
        }
    }

    private synchronized  void syncWrite(ByteBuffer buf) throws IOException {
        FileLock lock = fileChannel.lock();
        fileChannel.write(buf);
        lock.release();
    }

    /*
     * we work with timestamps, so we assume number > 1000 to avoid tests
     * to be faster
     */
    private String getShiftDecimal(long number, int shift) {
        StringBuilder builder = new StringBuilder();
        builder.append(number);

        int index = builder.length() - shift;
        builder.insert(index, ".");

        return builder.toString();
    }

    private void appendSampleResultField(ByteBuffer buf, SampleResult result, int fieldID) {
        // IMPORTANT: keep this as fast as possible
        switch (fieldID) {
            case 0:
                buf.put(result.isSuccessful() ? b1 : b0);
                break;

            case 1:
                buf.put(String.valueOf(result.getStartTime()).getBytes());
                break;

            case 2:
                buf.put(String.valueOf(result.getEndTime()).getBytes());
                break;

            case 3:
                if (result.getSamplerData() != null) {
                    buf.put(String.valueOf(result.getSamplerData().length()).getBytes());
                } else {
                    buf.put(b0);
                }
                break;

            case 4:
                if (result.getResponseData() != null) {
                    buf.put(String.valueOf(result.getResponseData().length).getBytes());
                } else {
                    buf.put(b0);
                }
                break;

            case 5:
                buf.put(String.valueOf(result.getTime()).getBytes());
                break;

            case 6:
                buf.put(String.valueOf(result.getLatency()).getBytes());
                break;

            case 7:
                buf.put(result.getResponseCode().getBytes());
                break;

            case 8:
                buf.put(result.getResponseMessage().getBytes());
                break;

            case 9:
                buf.put(!result.isSuccessful() ? b1 : b0);
                break;

            case 10:
                buf.put(result.getThreadName().getBytes());
                break;

            case 11:
                buf.put(result.getSampleLabel().getBytes());
                break;

            case 12:
                buf.put(getShiftDecimal(result.getStartTime(), 3).getBytes());
                break;

            case 13:
                buf.put(getShiftDecimal(result.getEndTime(), 3).getBytes());
                break;

            case 14:
                buf.put(String.valueOf(result.getTime() * 1000).getBytes());
                break;

            case 15:
                buf.put(String.valueOf(result.getLatency() * 1000).getBytes());
                break;

            case 16:
                buf.put(result.getSamplerData().getBytes());
                break;

            case 17:
                buf.put(result.getResponseData());
                break;

            case 18:
                buf.put(result.getRequestHeaders().getBytes());
                break;

            default:
                throw new IllegalArgumentException("Unknown field ID: " + fieldID);
        }
    }
}
