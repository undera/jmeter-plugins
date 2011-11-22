package kg.apc.emulators;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author apc
 */
public class SocketEmulatorOutputStream
        extends OutputStream {

    private StringBuffer buffer;

    /**
     *
     */
    public SocketEmulatorOutputStream() {
        buffer = new StringBuffer();
    }

    public void write(int b)
            throws IOException {
        buffer.append(b > 10 ? "" : "0").append(Integer.toHexString(b));
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
        byte[] res = BinaryTools.hexStringToByteArray(getWrittenBytesAsHexString());
        return res.toString();
    }
}
