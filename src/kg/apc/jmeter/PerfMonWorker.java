package kg.apc.jmeter;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class PerfMonWorker implements Runnable {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private int tcpPort = 4444;
    private int udpPort = 4444;
    private int exitCode = -1;
    private boolean isFinished = true;
    private final Selector acceptSelector;
    private List<SelectableChannel> connections = new LinkedList<SelectableChannel>();
    private ServerSocketChannel serverChannel;
    private final Thread writerThread;
    private final Selector sendSelector;

    public PerfMonWorker() throws IOException {
        acceptSelector = Selector.open();
        sendSelector = Selector.open();
        writerThread = new Thread(this);
    }

    public void setTCPPort(int parseInt) {
        tcpPort = parseInt;
    }

    public void setUDPPort(int parseInt) {
        udpPort = parseInt;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void processCommands() throws IOException {
        if (isFinished) {
            throw new IOException("Worker finished");
        }

        if (!acceptSelector.isOpen() || (connections.isEmpty() && serverChannel == null)) {
            throw new IOException("Nothing to do with this settings");
        }

        log.debug("Selecting");
        this.acceptSelector.select();
        log.debug("Selected");

        // wakeup to work on selected keys
        Iterator keys = this.acceptSelector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = (SelectionKey) keys.next();

            keys.remove();

            if (!key.isValid() || !key.channel().isOpen()) {
                continue;
            }

            if (key.isAcceptable()) {
                this.accept(key);
            } else if (key.isReadable()) {
                this.read(key);
            } else if (key.isWritable()) {
                this.write(key);
            }
        }
    }

    public int getExitCode() {
        return exitCode;
    }

    public void startAcceptingCommands() {
        log.debug("Start accepting connections");
        isFinished = false;
        writerThread.start();
        try {
            listenUDP();
        } catch (IOException ex) {
            log.error("Can't accept UDP connections", ex);
        }

        try {
            listenTCP();
        } catch (IOException ex) {
            log.error("Can't accept TCP connections", ex);
        }
    }

    private void listenTCP() throws IOException {
        if (tcpPort > 0) {
            log.debug("Binding TCP to " + tcpPort);
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            serverChannel.socket().bind(new InetSocketAddress(tcpPort));
            serverChannel.register(this.acceptSelector, SelectionKey.OP_ACCEPT);
        }
    }

    private void listenUDP() throws IOException {
        if (udpPort > 0) {
            log.debug("Binding UDP to " + udpPort);
            DatagramChannel udp = DatagramChannel.open();
            udp.socket().bind(new InetSocketAddress(udpPort));
            udp.configureBlocking(false);
            SelectionKey key = udp.register(acceptSelector, SelectionKey.OP_READ);
            accept(key);
        }
    }

    private void accept(SelectionKey key) throws IOException {
        log.debug("Accepting connection " + key);
        SelectableChannel channel = key.channel();
        SelectableChannel c;
        SelectionKey k;
        if (channel instanceof ServerSocketChannel) {
            c = ((ServerSocketChannel) channel).accept();
            c.configureBlocking(false);
            k = c.register(this.acceptSelector, SelectionKey.OP_READ);
        } else {
            c = channel;
            k = key;
        }

        PerfMonMetricGetter getter = new PerfMonMetricGetter(this, c);
        k.attach(getter);
        connections.add(c);
    }

    private void read(SelectionKey key) throws IOException {
        log.debug("Reading from " + key);
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        if (key.channel() instanceof SocketChannel) {
            SocketChannel channel = (SocketChannel) key.channel();
            if (channel.read(buf) < 0) {
                log.debug("Closing connection");
                channel.close();
                return;
            }
        }

        if (key.channel() instanceof DatagramChannel) {
            DatagramChannel channel = (DatagramChannel) key.channel();
            channel.receive(buf);
            //channel.

            DatagramSocket sock = channel.socket();
            log.debug(sock.toString());
        }

        buf.flip();
        log.debug("Read: " + buf.toString());

        PerfMonMetricGetter getter = (PerfMonMetricGetter) key.attachment();

        getter.addCommandString(JMeterPluginsUtils.byteBufferToString(buf));
        try {
            while (getter.processNextCommand()) {
                log.debug("Done executing command");
            }
        } catch (Exception e) {
            log.error("Error executing command", e);
        }
    }

    private void write(SelectionKey key) {
        log.debug("Writing to " + key);
    }

    public void shutdownConnections() throws IOException {
        log.debug("Shutdown connections");
        isFinished = true;
        Iterator<SelectableChannel> it = connections.iterator();
        while (it.hasNext()) {
            SelectableChannel entry = it.next();
            log.debug("Closing " + entry);
            entry.close();
            it.remove();
        }

        if (serverChannel != null) {
            serverChannel.close();
        }
        acceptSelector.close();
    }

    @Override
    public void run() {
        while (!isFinished) {
            try {
                processSenders();
            } catch (IOException ex) {
                log.error("Error processing senders", ex);
                break;
            }
        }
    }

    public void registerWritingChannel(SelectableChannel channel, PerfMonMetricGetter worker) throws ClosedChannelException {
        channel.register(sendSelector, SelectionKey.OP_WRITE, worker);
    }

    private void processSenders() throws IOException {
        log.debug("Selecting senders");
        sendSelector.selectNow();
        log.debug("Selected senders");

        // wakeup to work on selected keys
        Iterator keys = this.sendSelector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = (SelectionKey) keys.next();

            keys.remove();

            if (!key.isValid() || !key.channel().isOpen()) {
                continue;
            }

            if (key.isWritable()) {
                PerfMonMetricGetter getter = (PerfMonMetricGetter) key.attachment();
                getter.sendMetrics();
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            log.debug("Thread interrupted", ex);
        }
    }
}
