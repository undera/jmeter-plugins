// TODO: add startTimeSec - integer epoch seconds only - why startTime does not apply?
// TODO: buffer file writes to bigger chunks?
package kg.apc.jmeter.reporters;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.Remoteable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @see ResultCollector
 */
public class FlexibleFileWriter
        extends AbstractListenerElement
        implements SampleListener, Serializable,
        TestStateListener, Remoteable, NoThreadClone {

    public static final String AVAILABLE_FIELDS = "isSuccsessful "
            + "startTime endTime "
            + "sentBytes receivedBytes "
            + "responseTime latency "
            + "responseCode responseMessage "
            + "isFailed " // surrogates
            + "threadName sampleLabel "
            + "startTimeMillis endTimeMillis "
            + "responseTimeMicros latencyMicros "
            + "requestData responseData responseHeaders "
            + "threadsCount requestHeaders connectTime "
            + "grpThreads sampleCount errorCount "
            + "responseHeaderSize responseSize URL";
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String OVERWRITE = "overwrite";
    private static final String FILENAME = "filename";
    private static final String COLUMNS = "columns";
    private static final String HEADER = "header";
    private static final String FOOTER = "footer";
    private static final String VAR_PREFIX = "variable#";
    private static final String WRITE_BUFFER_LEN_PROPERTY = "kg.apc.jmeter.reporters.FFWBufferSize";
    private final int writeBufferSize = JMeterUtils.getPropDefault(WRITE_BUFFER_LEN_PROPERTY, 1024 * 10);
    protected volatile FileChannel fileChannel;
    private int[] compiledVars;
    private int[] compiledFields;
    private ByteBuffer[] compiledConsts;
    private ArrayList<String> availableFieldNames = new ArrayList<>(Arrays.asList(AVAILABLE_FIELDS.trim().split(" ")));
    private static final byte[] b1 = "1".getBytes();
    private static final byte[] b0 = "0".getBytes();

    public FlexibleFileWriter() {
        super();
    }

    @Override
    public void sampleStarted(SampleEvent e) {
    }

    @Override
    public void sampleStopped(SampleEvent e) {
    }

    @Override
    public void testStarted() {
        compileColumns();
        try {
            openFile();
        } catch (FileNotFoundException ex) {
            log.error("Cannot open file " + getFilename(), ex);
        } catch (IOException ex) {
            log.error("Cannot write file header " + getFilename(), ex);
        }
    }

    @Override
    public void testStarted(String host) {
        testStarted();
    }

    @Override
    public void testEnded() {
        closeFile();
    }

    @Override
    public void testEnded(String host) {
        testEnded();
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

    public boolean isOverwrite() {
        return getPropertyAsBoolean(OVERWRITE, false);
    }

    public void setOverwrite(boolean ov) {
        setProperty(OVERWRITE, ov);
    }

    public void setFileHeader(String str) {
        setProperty(HEADER, str);
    }

    public String getFileHeader() {
        return getPropertyAsString(HEADER);
    }

    public void setFileFooter(String str) {
        setProperty(FOOTER, str);
    }

    public String getFileFooter() {
        return getPropertyAsString(FOOTER);
    }

    /**
     * making this once to be efficient and avoid manipulating strings in switch
     * operators
     */
    private void compileColumns() {
        log.debug("Compiling columns string: " + getColumns());
        String[] chunks = JMeterPluginsUtils.replaceRNT(getColumns()).split("\\|");
        log.debug("Chunks " + chunks.length);
        compiledFields = new int[chunks.length];
        compiledVars = new int[chunks.length];
        compiledConsts = new ByteBuffer[chunks.length];
        for (int n = 0; n < chunks.length; n++) {
            int fieldID = availableFieldNames.indexOf(chunks[n]);
            if (fieldID >= 0) {
                //log.debug(chunks[n] + " field id: " + fieldID);
                compiledFields[n] = fieldID;
            } else {
                compiledFields[n] = -1;
                compiledVars[n] = -1;
                if (chunks[n].contains(VAR_PREFIX)) {
                    log.debug(chunks[n] + " is sample variable");
                    String varN = chunks[n].substring(VAR_PREFIX.length());
                    try {
                        compiledVars[n] = Integer.parseInt(varN);
                    } catch (NumberFormatException e) {
                        log.error("Seems it is not variable spec: " + chunks[n]);
                        compiledConsts[n] = ByteBuffer.wrap(chunks[n].getBytes());
                    }
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
    }

    protected void openFile() throws IOException {
        String filename = getFilename();
        FileOutputStream fos = new FileOutputStream(filename, !isOverwrite());
        fileChannel = fos.getChannel();

        String header = JMeterPluginsUtils.replaceRNT(getFileHeader());
        if (!header.isEmpty()) {
            syncWrite(ByteBuffer.wrap(header.getBytes()));
        }
    }

    private synchronized void closeFile() {
        if (fileChannel != null && fileChannel.isOpen()) {
            try {
                String footer = JMeterPluginsUtils.replaceRNT(getFileFooter());
                if (!footer.isEmpty()) {
                    syncWrite(ByteBuffer.wrap(footer.getBytes()));
                }

                fileChannel.force(false);
                fileChannel.close();
            } catch (IOException ex) {
                log.error("Failed to close file: " + getFilename(), ex);
            }
        }
    }

    @Override
    public void sampleOccurred(SampleEvent evt) {
        if (fileChannel == null || !fileChannel.isOpen()) {
            if (log.isWarnEnabled()) {
                log.warn("File writer is closed! Maybe test has already been stopped");
            }
            return;
        }

        ByteBuffer buf = ByteBuffer.allocateDirect(writeBufferSize);
        for (int n = 0; n < compiledConsts.length; n++) {
            if (compiledConsts[n] != null) {
                //noinspection SynchronizeOnNonFinalField
                synchronized (compiledConsts) {
                    buf.put(compiledConsts[n].duplicate());
                }
            } else {
                if (!appendSampleResultField(buf, evt.getResult(), compiledFields[n])) {
                    appendSampleVariable(buf, evt, compiledVars[n]);
                }
            }
        }

        buf.flip();

        try {
            syncWrite(buf);
        } catch (IOException ex) {
            log.error("Problems writing to file", ex);
        }
    }

    private synchronized void syncWrite(ByteBuffer buf) throws IOException {
        FileLock lock = fileChannel.lock();
        try {
            fileChannel.write(buf);
        } finally {
            lock.release();
        }
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

    private void appendSampleVariable(ByteBuffer buf, SampleEvent evt, int varID) {
        if (SampleEvent.getVarCount() < varID + 1) {
            buf.put(("UNDEFINED_variable#" + varID).getBytes());
            log.warn("variable#" + varID + " does not exist!");
        } else {
            if (evt.getVarValue(varID) != null) {
                buf.put(evt.getVarValue(varID).getBytes());
            }
        }
    }

    /**
     * @return boolean true if existing field found, false instead
     */
    private boolean appendSampleResultField(ByteBuffer buf, SampleResult result, int fieldID) {
        // IMPORTANT: keep this as fast as possible
        switch (fieldID) {
            case 0:
                buf.put(result.isSuccessful() ? b1 : b0);
                break;

            case 1:
                buf.put(String.valueOf(result.getStartTime()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 2:
                buf.put(String.valueOf(result.getEndTime()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 3:
                if (result.getSamplerData() != null) {
                    buf.put(String.valueOf(result.getSamplerData().length()).getBytes(JMeterPluginsUtils.CHARSET));
                } else {
                    buf.put(b0);
                }
                break;

            case 4:
                if (result.getResponseData() != null) {
                    buf.put(String.valueOf(result.getResponseData().length).getBytes(JMeterPluginsUtils.CHARSET));
                } else {
                    buf.put(b0);
                }
                break;

            case 5:
                buf.put(String.valueOf(result.getTime()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 6:
                buf.put(String.valueOf(result.getLatency()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 7:
                buf.put(result.getResponseCode().getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 8:
                buf.put(result.getResponseMessage().getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 9:
                buf.put(!result.isSuccessful() ? b1 : b0);
                break;

            case 10:
                buf.put(result.getThreadName().getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 11:
                buf.put(result.getSampleLabel().getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 12:
                buf.put(getShiftDecimal(result.getStartTime(), 3).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 13:
                buf.put(getShiftDecimal(result.getEndTime(), 3).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 14:
                buf.put(String.valueOf(result.getTime() * 1000).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 15:
                buf.put(String.valueOf(result.getLatency() * 1000).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 16:
                if (result.getSamplerData() != null) {
                    buf.put(result.getSamplerData().getBytes(JMeterPluginsUtils.CHARSET));
                } else {
                    buf.put(b0);
                }
                break;

            case 17:
                buf.put(result.getResponseData());
                break;

            case 18:
                buf.put(result.getResponseHeaders().getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 19:
                buf.put(String.valueOf(result.getAllThreads()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 20:
                buf.put(String.valueOf(result.getRequestHeaders()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 21:
                buf.put(String.valueOf(result.getConnectTime()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 22:
                buf.put(String.valueOf(result.getGroupThreads()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 23:
                buf.put(String.valueOf(result.getSampleCount()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 24:
                buf.put(String.valueOf(result.getErrorCount()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 25:
                buf.put(String.valueOf(result.getHeadersSize()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 26:
                buf.put(String.valueOf(result.getBodySize()).getBytes(JMeterPluginsUtils.CHARSET));
                break;

            case 27:
                buf.put(result.getUrlAsString().getBytes(JMeterPluginsUtils.CHARSET));
                break;

            default:
                return false;
        }
        return true;
    }
}
