// TODO: document it!
package kg.apc.jmeter.samplers;

import java.io.*;
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
    private final int chunkSize = JMeterUtils.getPropDefault("tcp.infiniteTCPChunkSize", 5120);
    private boolean isRequestSent = false;
    private int recvDataLimit;

    public InfiniteGetTCPClientImpl() {
        super();
        recvDataLimit = JMeterUtils.getPropDefault(AbstractIPSampler.RESULT_DATA_LIMIT, Integer.MAX_VALUE);
        if (recvDataLimit < Integer.MAX_VALUE) {
            log.info("Limiting result data to " + recvDataLimit);
        }
    }

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
        int x;
        try {
            x = is.read(buffer);
            if (x > 0) {
                if (w.size() < recvDataLimit) {
                    w.write(buffer, 0, x);
                }
            } else {
                log.warn("Read 0 bytes, seems the connection was closed. Closing stream");
                is.close();
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
