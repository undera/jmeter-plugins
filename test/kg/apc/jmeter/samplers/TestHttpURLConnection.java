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
    public SocketEmulatorInputStream socketEmulatorInputStream = new SocketEmulatorInputStream();
    public SocketEmulatorOutputStream socketEmulatorOutputStream = new SocketEmulatorOutputStream();

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
        return socketEmulatorOutputStream;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socketEmulatorInputStream;
    }
}
