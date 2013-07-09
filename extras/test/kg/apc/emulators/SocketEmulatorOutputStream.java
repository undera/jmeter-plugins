package kg.apc.emulators;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;

/**
 *
 * @author apc
 */
public class SocketEmulatorOutputStream
        extends OutputStream {

    private StringBuilder buffer;

    /**
     *
     */
    public SocketEmulatorOutputStream() {
        buffer = new StringBuilder();
    }

    @Override
    public void write(int b)
            throws IOException {
        appendByte(b);
    }

    private void appendByte(int b1) {
        String hex = Integer.toHexString(0xFF & b1);
        if (hex.length() == 1) {
            // could use a for loop, but we're only dealing with a single byte
            buffer.append('0');
        }
        buffer.append(hex);
    }

    /**
     *
     * @return
     */
    public String getWrittenBytesAsHexString() {
        final String toString = buffer.toString();
        buffer.setLength(0);
        return toString;
    }

    public String getWrittenBytesAsString() {
        byte[] res = BinaryTCPClientImpl.hexStringToByteArray(getWrittenBytesAsHexString());
        return res.toString();
    }
}
