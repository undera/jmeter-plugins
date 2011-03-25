package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public abstract class AbstractIPSampler extends AbstractSampler implements Serializable, Cloneable {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String HOSTNAME = "hostname";
    public static final String PORT = "port";
    public static final String TIMEOUT = "timeout";
    public static final String DATA = "data";
    public static final String CRLF = "\r\n";
    public static final String EMPTY = "";
    public static final String RC200 = "200";
    public static final String RC500 = "500";
    protected ByteBuffer recvBuf = ByteBuffer.allocateDirect(1024 * 4);

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

    public SampleResult sample(Entry entry) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.setSamplerData(getRequestData());
        res.sampleStart();
        res.setDataType(SampleResult.BINARY);
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
