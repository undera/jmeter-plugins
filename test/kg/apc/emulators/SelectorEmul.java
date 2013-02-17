package kg.apc.emulators;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.util.Set;

/**
 *
 * @author undera
 */
public class SelectorEmul extends AbstractSelector {
    private Set<SelectionKey> selectedKeys;

    public SelectorEmul() {
        super(null);
    }

    @Override
    public Set<SelectionKey> keys() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<SelectionKey> selectedKeys() {
        return getSelectedKeys();
    }

    @Override
    public int selectNow() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int select(long timeout) throws IOException {
        try {
            Thread.sleep(timeout / 10);
        } catch (InterruptedException ex) {
            throw new IOException("", ex);
        }
        return 1;
    }

    @Override
    public int select() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Selector wakeup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the selectedKeys
     */
    public Set<SelectionKey> getSelectedKeys() {
        return selectedKeys;
    }

    /**
     * @param selectedKeys the selectedKeys to set
     */
    public void setSelectedKeys(Set<SelectionKey> selectedKeys) {
        this.selectedKeys = selectedKeys;
    }

    @Override
    protected void implCloseSelector() throws IOException {
    }

    @Override
    protected SelectionKey register(AbstractSelectableChannel ch, int ops, Object att) {
        return new SelectionKeyEmul();
    }

}
