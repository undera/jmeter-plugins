package kg.apc.perfmon.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author undera
 */
class UDPOutputStream extends OutputStream {

    private final DatagramSocket socket;
    ByteArrayOutputStream bos;

    public UDPOutputStream(DatagramSocket sock) {
        socket = sock;
        bos = new ByteArrayOutputStream();
    }

    public void write(int i) throws IOException {
        bos.write(i);

        if (i == '\n') {
            byte[] data = bos.toByteArray();
            bos.reset();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.send(packet);
        }
    }
}
