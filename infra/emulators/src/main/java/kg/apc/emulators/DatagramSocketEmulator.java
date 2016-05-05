package kg.apc.emulators;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class DatagramSocketEmulator
        extends DatagramSocket {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private DatagramPacket toRead;

    public DatagramSocketEmulator() throws SocketException {
        log.debug("Created emulator");
    }

    @Override
    public synchronized void receive(DatagramPacket p) throws IOException {
        log.debug("Emulate receive: " + p);
        if (toRead == null) {
            throw new SocketException();
        }

        p.setData(toRead.getData());
        p.setAddress(toRead.getAddress());
        //p.setSocketAddress(toRead.getSocketAddress());
        toRead = null;
    }

    @Override
    public void send(DatagramPacket p) throws IOException {
        log.debug("Emulate send: " + p);
    }

    public void setDatagramToReceive(DatagramPacket datagramPacket) {
        toRead = datagramPacket;
    }
}
