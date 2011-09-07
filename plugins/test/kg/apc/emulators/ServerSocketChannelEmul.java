package kg.apc.emulators;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *
 * @author undera
 */
class ServerSocketChannelEmul extends ServerSocketChannel {

    public ServerSocketChannelEmul() {
        super(null);
    }

    @Override
    public ServerSocket socket() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SocketChannel accept() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void implConfigureBlocking(boolean bln) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
