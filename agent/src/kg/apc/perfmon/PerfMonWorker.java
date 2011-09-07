package kg.apc.perfmon;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
    private ServerSocketChannel tcpServer;
    private final Thread writerThread;
    private final Selector sendSelector;
    private DatagramChannel udpServer;
    private final LinkedList<SelectableChannel> tcpConnections = new LinkedList<SelectableChannel>();
    private final HashMap<SocketAddress, PerfMonMetricGetter> udpConnections = new HashMap<SocketAddress, PerfMonMetricGetter>();

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

        if (!acceptSelector.isOpen() || (tcpServer == null && udpServer == null)) {
            throw new IOException("Nothing to do with this settings");
        }

        log.debug("Selecting incoming");
        this.acceptSelector.select();
        log.debug("Selected incoming");

        // wakeup to work on selected keys
        Iterator keys = this.acceptSelector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = (SelectionKey) keys.next();

            keys.remove();

            if (!key.isValid()) {
                continue;
            }

            if (key.isAcceptable()) {
                this.accept(key);
            } else if (key.isReadable()) {
                this.read(key);
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

    private int getInterval() {
        return 1000;
    }

    private void listenTCP() throws IOException {
        if (tcpPort > 0) {
            log.debug("Binding TCP to " + tcpPort);
            tcpServer = ServerSocketChannel.open();
            tcpServer.configureBlocking(false);

            tcpServer.socket().bind(new InetSocketAddress(tcpPort));
            tcpServer.register(this.acceptSelector, SelectionKey.OP_ACCEPT);
        }
    }

    private void listenUDP() throws IOException {
        if (udpPort > 0) {
            log.debug("Binding UDP to " + udpPort);
            DatagramChannel udp = DatagramChannel.open();
            udp.socket().bind(new InetSocketAddress(udpPort));
            udp.configureBlocking(false);
            udp.register(acceptSelector, SelectionKey.OP_READ);
            udp.register(sendSelector, SelectionKey.OP_WRITE);
        }
    }

    private void accept(SelectionKey key) throws IOException {
        log.debug("Accepting TCP connection " + key);
        SelectableChannel channel = key.channel();
        SelectableChannel tcpConn = ((ServerSocketChannel) channel).accept();
        tcpConn.configureBlocking(false);
        SelectionKey k = tcpConn.register(this.acceptSelector, SelectionKey.OP_READ);

        log.debug("Creating new metric getter");
        PerfMonMetricGetter getter = new PerfMonMetricGetter(this, tcpConn);
        k.attach(getter);
        tcpConnections.add(tcpConn);
    }

    // TODO: refactor this code
    private void read(SelectionKey key) throws IOException {
        PerfMonMetricGetter getter = null;
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        if (key.channel() instanceof SocketChannel) {
            SocketChannel channel = (SocketChannel) key.channel();
            if (channel.read(buf) < 0) {
                log.debug("Closing connection");
                channel.close();
                return;
            }
            getter = (PerfMonMetricGetter) key.attachment();
        } else if (key.channel() instanceof DatagramChannel) {
            DatagramChannel channel = (DatagramChannel) key.channel();
            SocketAddress remoteAddr = channel.receive(buf);
            if (!udpConnections.containsKey(remoteAddr)) {
                log.debug("Connecting new UDP client");
                udpConnections.put(remoteAddr, new PerfMonMetricGetter(this, channel));
            }
            getter = udpConnections.get(remoteAddr);
        }

        buf.flip();
        log.debug("Read: " + buf.toString());

        getter.addCommandString(JMeterPluginsUtils.byteBufferToString(buf));
        try {
            while (getter.processNextCommand()) {
                log.debug("Done executing command");
            }
        } catch (Exception e) {
            log.error("Error executing command", e);
        }
    }

    public void shutdownConnections() throws IOException {
        log.debug("Shutdown connections");
        isFinished = true;
        Iterator<SelectableChannel> it = tcpConnections.iterator();
        while (it.hasNext()) {
            SelectableChannel entry = it.next();
            log.debug("Closing " + entry);
            entry.close();
            it.remove();
        }

        if (udpServer != null) {
            udpServer.close();
        }

        if (tcpServer != null) {
            tcpServer.close();
        }
        acceptSelector.close();
        sendSelector.close();
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
        sendSelector.wakeup();
        channel.register(sendSelector, SelectionKey.OP_WRITE, worker);
    }

    private void processSenders() throws IOException {
        log.debug("Selecting senders");
        sendSelector.select(getInterval());
        log.debug("Selected senders");

        long begin = System.currentTimeMillis();

        // wakeup to work on selected keys
        Iterator keys = this.sendSelector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = (SelectionKey) keys.next();

            keys.remove();

            if (!key.isValid()) {
                continue;
            }

            if (key.isWritable()) {
                if (key.channel() instanceof DatagramChannel) {
                    sendToUDP(key);
                } else {
                    PerfMonMetricGetter getter = (PerfMonMetricGetter) key.attachment();
                    ByteBuffer metrics = getter.getMetricsLine();
                    ((WritableByteChannel) key.channel()).write(metrics);
                }
            }
        }

        long spent = System.currentTimeMillis() - begin;
        if (spent < getInterval()) {
            try {
                Thread.sleep(getInterval() - spent);
            } catch (InterruptedException ex) {
                log.debug("Thread interrupted", ex);
            }
        }
    }

    private void sendToUDP(SelectionKey key) throws IOException {
        Iterator<SocketAddress> it = udpConnections.keySet().iterator();
        while (it.hasNext()) {
            SocketAddress addr = it.next();
            PerfMonMetricGetter getter = udpConnections.get(addr);
            if (getter.isStarted()) {
                ByteBuffer metrics = getter.getMetricsLine();
                ((DatagramChannel) key.channel()).send(metrics, addr);
            }
        }
    }

    public void setMetrics(String next) {
        // TODO: implement command-line printing mode for SSH transport
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
