package kg.apc.emulators;

import java.io.IOException;
import java.io.InputStream;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class SocketEmulatorInputStream
        extends InputStream {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private byte[] bytes;
    private int pos = 0;

    /**
     *
     * @param hexStringToByteArray
     */
    public SocketEmulatorInputStream(byte[] hexStringToByteArray) {
        this();
        setBytesToRead(hexStringToByteArray);
    }

    /**
     *
     */
    public SocketEmulatorInputStream() {
        bytes = null;
        pos = 0;
        log.info("Created input stream emulator");
    }

    /**
     *
     * @param hexStringToByteArray
     */
    public final void setBytesToRead(byte[] hexStringToByteArray) {
        log.debug("Set bytes to read: " + Integer.toString(hexStringToByteArray.length));
        bytes = hexStringToByteArray;
    }

    public final void setBytesToRead(String str) {
        setBytesToRead(str.getBytes());
    }

    @Override
    public int read()
            throws IOException {
        if (bytes == null) {
            log.debug("Expected emulator data was not set");
            return -1;
        }

        if (pos >= bytes.length) {
            log.debug("End of data  reached, resetting buffer");
            bytes = null;
            pos = 0;
            return -1;
        } else {
            final byte byteToret = bytes[pos];
            pos++;
            //log.debug("Byte: #"+Integer.toString(pos)+" "+Byte.toString(byteToret));
            return byteToret;
        }
    }
}
