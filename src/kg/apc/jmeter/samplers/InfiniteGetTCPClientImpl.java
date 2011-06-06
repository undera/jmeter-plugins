package kg.apc.jmeter.samplers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import org.apache.jmeter.protocol.tcp.sampler.TCPClientImpl;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class InfiniteGetTCPClientImpl extends TCPClientImpl {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private final int infiniteTCPChunkSize = JMeterUtils.getPropDefault("tcp.infiniteTCPChunkSize", 5120); // $NON-NLS-1$

    private boolean isRequestSent = false;

    @Override
    public void write(OutputStream os, InputStream is) {
        if (!isRequestSent) {
            isRequestSent = true;
            super.write(os, is);
        }
    }

    @Override
    public void write(OutputStream out, String string) {
        if (!isRequestSent) {
            isRequestSent = true;
            super.write(out, string);
        }
    }

    @Override
    public String read(InputStream is) {
        byte[] buffer = new byte[infiniteTCPChunkSize];
        ByteArrayOutputStream w = new ByteArrayOutputStream();
        int x = 0;
        try {
            x = is.read(buffer);
        } catch (SocketTimeoutException e) {
            // drop out to handle buffer
        } catch (InterruptedIOException e) {
            // drop out to handle buffer
        } catch (IOException e) {
            log.warn("Read error:" + e);
            return "";
        }

        w.write(buffer, 0, x);
        log.debug("Read: " + w.size() + "\n" + w.toString());
        return w.toString();
    }
}
