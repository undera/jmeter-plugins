package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public abstract class AbstractIPSampler
        extends AbstractSampler
        implements Serializable, Cloneable, Interruptible {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String RECV_BUFFER_LEN_PROPERTY = "kg.apc.jmeter.samplers.ReceiveBufferSize";
    public static final String RESULT_DATA_LIMIT = "kg.apc.jmeter.samplers.ResultDataLimit";
    public static final String HOSTNAME = "hostname";
    public static final String PORT = "port";
    public static final String TIMEOUT = "timeout";
    public static final String DATA = "data";
    public static final String CRLF = "\r\n";
    public static final String EMPTY = "";
    public static final String RC200 = "200";
    public static final String RC500 = "500";
    protected final static int recvBufSize = JMeterUtils.getPropDefault(RECV_BUFFER_LEN_PROPERTY, 1024 * 4);
    private transient ByteBuffer recvBuf;
    protected final int recvDataLimit;

    public AbstractIPSampler() {
        //recvBuf = ByteBuffer.allocateDirect(JMeterUtils.getPropDefault(RECV_BUFFER_LEN_PROPERTY, 1024 * 4));
        recvDataLimit = JMeterUtils.getPropDefault(RESULT_DATA_LIMIT, Integer.MAX_VALUE);
        if (recvDataLimit < Integer.MAX_VALUE) {
            log.info("Limiting result data to " + recvDataLimit);
        }
    }

    public ByteBuffer getRecvBuf() {
       if(recvBuf == null) {
          recvBuf = ByteBuffer.allocateDirect(recvBufSize);
       }
       return recvBuf;
    }

    public final String getHostName() {
        return getPropertyAsString(AbstractIPSampler.HOSTNAME);
    }

    public String getPort() {
        return getPropertyAsString(AbstractIPSampler.PORT);
    }

    public String getRequestData() {
        return getPropertyAsString(AbstractIPSampler.DATA);
    }

    public String getTimeout() {
        return getPropertyAsString(AbstractIPSampler.TIMEOUT);
    }

    public void setHostName(String text) {
        setProperty(AbstractIPSampler.HOSTNAME, text);
    }

    public void setPort(String text) {
        setProperty(AbstractIPSampler.PORT, text);
    }

    public void setRequestData(String text) {
        setProperty(AbstractIPSampler.DATA, text);
    }

    public void setTimeout(String text) {
        setProperty(AbstractIPSampler.TIMEOUT, text);
    }

    protected int getPortAsInt() {
        try {
            return Integer.parseInt(getPort());
        } catch (NumberFormatException ex) {
            log.error("Wrong port: " + getTimeout(), ex);
            return 0;
        }
    }

    protected int getTimeoutAsInt() {
        try {
            return Integer.parseInt(getTimeout());
        } catch (NumberFormatException ex) {
            log.error("Wrong timeout: " + getTimeout(), ex);
            return 0;
        }
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.setSamplerData(getRequestData());
        res.sampleStart();
        res.setDataType(SampleResult.TEXT);
        res.setSuccessful(true);
        res.setResponseCode(HTTPRawSampler.RC200);
        try {
            res.setResponseData(processIO(res));
        } catch (Exception ex) {
            if (!(ex instanceof SocketTimeoutException)) {
                log.error(getHostName(), ex);
            }
            res.sampleEnd();
            res.setSuccessful(false);
            res.setResponseCode(HTTPRawSampler.RC500);
            res.setResponseMessage(ex.toString());
            res.setResponseData((ex.toString() + HTTPRawSampler.CRLF + JMeterPluginsUtils.getStackTrace(ex)).getBytes());
        }

        return res;
    }

    abstract protected AbstractSelectableChannel getChannel() throws IOException;

    abstract protected byte[] processIO(SampleResult res) throws Exception;
}
