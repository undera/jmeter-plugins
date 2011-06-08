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
    private final int chunkSize = JMeterUtils.getPropDefault("tcp.infiniteTCPChunkSize", 5120); // $NON-NLS-1$
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
        byte[] buffer = new byte[chunkSize];
        ByteArrayOutputStream w = new ByteArrayOutputStream();
        int x = 0;
        try {
            x = is.read(buffer);
            if (x > 0) {
                w.write(buffer, 0, x);
            } else {
                throw new RuntimeException("Read 0 bytes, seems we have the timeout");
            }
        } catch (SocketTimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedIOException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Read: " + w.size() + "\n" + w.toString());
        }
        return w.toString();
    }
}
