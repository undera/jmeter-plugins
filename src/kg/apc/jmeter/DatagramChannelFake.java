package kg.apc.jmeter;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 *
 * @author undera
 */
class DatagramChannelFake extends AbstractSelectableChannel implements WritableByteChannel {

    static DatagramChannelFake open() {
        return new DatagramChannelFake(SelectorProvider.provider());
    }
    private SocketAddress remote;
    private DatagramChannel commChannel;

    protected DatagramChannelFake(SelectorProvider sp) {
        super(sp);
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
    }

    @Override
    protected void implConfigureBlocking(boolean bln) throws IOException {
    }

    @Override
    public int validOps() {
        return SelectionKey.OP_READ;
    }

    public void connect(SocketAddress remoteAddr) {
        remote = remoteAddr;
    }

    public void setCommChannel(DatagramChannel channel) {
        commChannel = channel;
    }

    @Override
    public int write(ByteBuffer bb) throws IOException {
        return commChannel.write(bb);
    }
}
