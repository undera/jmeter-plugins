package kg.apc.perfmon.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

/**
 *
 * @author undera
 */
class UDPInputStream extends InputStream {

    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private ByteBuffer data=ByteBuffer.allocateDirect(0);

    public UDPInputStream(DatagramSocket sock) {
        socket = sock;

        byte[] buffer = new byte[4096]; // fixme: remove magic constants
        packet = new DatagramPacket(buffer, 4096);
    }

    public int read() throws IOException {
        if (!data.hasRemaining()) {
            socket.receive(packet);
            data = ByteBuffer.wrap(packet.getData());
        }

        if (!data.hasRemaining()) {
            return -1;
        } else {
            return data.get();
        }
    }
}
