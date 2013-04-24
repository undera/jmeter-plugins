package kg.apc.emulators;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

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

    @Override
    public SocketAddress getLocalAddress() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getOption(SocketOption<T> name) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServerSocketChannel bind(SocketAddress local, int backlog)
            throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> ServerSocketChannel setOption(SocketOption<T> name, T value)
            throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
}
