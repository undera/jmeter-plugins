package kg.apc.emulators;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

class ServerSocketChannelEmul extends ServerSocketChannel {

    public ServerSocketChannelEmul() {
        super(null);
    }

    @Override
    public ServerSocketChannel bind(SocketAddress socketAddress, int i) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SocketAddress getLocalAddress() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> ServerSocketChannel setOption(SocketOption<T> socketOption, T t) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        throw new UnsupportedOperationException("Not supported yet.");
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
