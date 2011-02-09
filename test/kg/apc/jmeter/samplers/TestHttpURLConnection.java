package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import kg.apc.jmeter.util.SocketEmulatorInputStream;
import kg.apc.jmeter.util.SocketEmulatorOutputStream;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class TestHttpURLConnection extends HttpURLConnection {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private SocketEmulatorInputStream socketEmulatorInputStream;
    private SocketEmulatorOutputStream socketEmulatorOutputStream;

    public TestHttpURLConnection() throws MalformedURLException {
        super(new URL("http://localhost/"));
    }

    @Override
    public void disconnect() {
        log.debug("disconnect");
    }

    @Override
    public boolean usingProxy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connect() throws IOException {
        log.debug("connect");
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return getSocketEmulatorOutputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return getSocketEmulatorInputStream();
    }

    /**
     * @return the socketEmulatorInputStream
     */
    public SocketEmulatorInputStream getSocketEmulatorInputStream() {
        return socketEmulatorInputStream;
    }

    /**
     * @return the socketEmulatorOutputStream
     */
    public SocketEmulatorOutputStream getSocketEmulatorOutputStream() {
        return socketEmulatorOutputStream;
    }

    /**
     * @param socketEmulatorInputStream the socketEmulatorInputStream to set
     */
    public void setSocketEmulatorInputStream(SocketEmulatorInputStream socketEmulatorInputStream) {
        this.socketEmulatorInputStream = socketEmulatorInputStream;
    }

    /**
     * @param socketEmulatorOutputStream the socketEmulatorOutputStream to set
     */
    public void setSocketEmulatorOutputStream(SocketEmulatorOutputStream socketEmulatorOutputStream) {
        this.socketEmulatorOutputStream = socketEmulatorOutputStream;
    }
}
