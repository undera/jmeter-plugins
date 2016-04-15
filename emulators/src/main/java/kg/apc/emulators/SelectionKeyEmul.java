package kg.apc.emulators;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectionKey;

public class SelectionKeyEmul extends AbstractSelectionKey {

    public SelectionKeyEmul() {
    }

    @Override
    public SelectableChannel channel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Selector selector() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int interestOps() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SelectionKey interestOps(int ops) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int readyOps() {
        return Integer.MAX_VALUE;
    }
}
