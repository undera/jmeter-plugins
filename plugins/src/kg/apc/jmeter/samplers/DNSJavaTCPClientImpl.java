package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kg.apc.jmeter.dcerpc.BinaryUtils;
import org.apache.jmeter.protocol.tcp.sampler.TCPClient;
import org.apache.jorphan.logging.LoggingManager;

/**
 *
 * @author undera
 */
// todo: document it!
public class DNSJavaTCPClientImpl extends DNSJavaDecoder implements TCPClient {

    private static final org.apache.log.Logger log = LoggingManager.getLoggerForClass();

    public void setupTest() {
    }

    public void teardownTest() {
    }

    public void write(OutputStream out, InputStream in) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void write(OutputStream out, String string) {
        try {
            byte[] msg = getMessageBytes(string);
            out.write(getLengthPrefix(msg.length));
            out.write(msg);
        } catch (IOException ex) {
            log.error("Failed to send DNS request: " + string, ex);
        }
    }

    protected byte[] getLengthPrefix(int length) {
        if (length > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Length is too big for DNS");
        }

        byte[] res = BinaryUtils.shortToByteArray(Short.reverseBytes((short) length));
        return res;
    }

    public String read(InputStream in) {
        byte[] header = new byte[2];
        byte[] buf = new byte[0];
        try {
            in.read(header);
            short len = BinaryUtils.twoBytesToShortVal(header[1], header[0]);
            buf = new byte[len];
            in.read(buf);
        } catch (IOException ex) {
            log.error("Failed to receive DNS response");
        }
        return new String(super.decode(buf));
    }

    public byte getEolByte() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEolByte(int i) {
    }
}
