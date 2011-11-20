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
    private ByteBuffer data;

    public UDPInputStream(DatagramSocket sock) {
        socket = sock;

        byte[] buffer = new byte[4096]; // fixme: remove magic constants
        packet = new DatagramPacket(buffer, 4096);
    }

    public int read() throws IOException {
        if (data == null) {
            socket.receive(packet);
            byte[] packetData = packet.getData();
            data = ByteBuffer.wrap(packetData, packet.getOffset(), packet.getLength());
        }

        if (data.position() >= data.limit()) {
            data = null;
            return -1;
        } else {
            return data.get();
        }
    }
}
