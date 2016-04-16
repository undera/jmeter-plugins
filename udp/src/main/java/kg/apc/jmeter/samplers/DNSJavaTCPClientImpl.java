// todo: document it in wiki
package kg.apc.jmeter.samplers;

import kg.apc.io.BinaryUtils;
import org.apache.jmeter.protocol.tcp.sampler.TCPClient;
import org.apache.jorphan.logging.LoggingManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class DNSJavaTCPClientImpl extends DNSJavaDecoder implements TCPClient {

    private ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private static final org.apache.log.Logger log = LoggingManager.getLoggerForClass();

    public void setupTest() {
    }

    public void teardownTest() {
    }

    public void write(OutputStream out, InputStream in) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void write(OutputStream out, String string) {
        byte[] msg = getMessageBytes(string);

        try {
            bos.write(getLengthPrefix(msg.length));
            bos.write(msg);
            out.write(bos.toByteArray());
        } catch (IOException ex) {
            log.error("Failed to send DNS request: " + string, ex);
        }
        bos.reset();
    }

    protected byte[] getLengthPrefix(int length) {
        if (length > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Length is too big for DNS");
        }

        return BinaryUtils.shortToByteArray(Short.reverseBytes((short) length));
    }

    public String read(InputStream in) {
        byte[] header = new byte[2];
        byte[] buf = new byte[0];
        try {
            in.read(header);
            int len = (int) BinaryUtils.twoBytesToLongVal(header[1], header[0]);
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

    public String getCharset() {
        return Charset.defaultCharset().name();
    }
}
