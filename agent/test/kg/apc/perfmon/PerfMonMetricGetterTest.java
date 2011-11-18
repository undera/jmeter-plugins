package kg.apc.perfmon;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import junit.framework.TestCase;
import kg.apc.emulators.DatagramChannelEmul;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class PerfMonMetricGetterTest extends TestCase {

    public PerfMonMetricGetterTest() {
    }

    /**
     * Test of processCommand method, of class PerfMonMetricGetter.
     */
    public void testProcessCommand() throws IOException {
        System.out.println("processCommand");
        String toString = "test\ntest\nerr\n";
        final DatagramChannel channel = DatagramChannelEmul.open();
        channel.configureBlocking(false);
        final InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 4444);
        channel.connect(inetSocketAddress);
        PerfMonMetricGetter instance = new PerfMonMetricGetter(SigarProxyCache.newInstance(new Sigar(), 500), new PerfMonWorker(), channel, inetSocketAddress);
        instance.addCommandString(toString);
        instance.processNextCommand();
        instance.processNextCommand();
        try {
            instance.processNextCommand();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of addCommandString method, of class PerfMonMetricGetter.
     */
    public void testAddCommandString() throws IOException {
        System.out.println("addCommandString");
        String byteBufferToString = "";
        PerfMonMetricGetter instance = new PerfMonMetricGetter(SigarProxyCache.newInstance(new Sigar(), 500), new PerfMonWorker(), DatagramChannel.open());
        instance.addCommandString(byteBufferToString);
    }

    /**
     * Test of processNextCommand method, of class PerfMonMetricGetter.
     */
    public void testProcessNextCommand() throws Exception {
        System.out.println("processNextCommand");
        PerfMonMetricGetter instance = new PerfMonMetricGetter(SigarProxyCache.newInstance(new Sigar(), 500), new PerfMonWorker(), DatagramChannel.open());
        boolean expResult = false;
        boolean result = instance.processNextCommand();
        assertEquals(expResult, result);
    }

    /**
     * Test of sendMetrics method, of class PerfMonMetricGetter.
     */
    public void testSendMetrics() throws IOException {
        System.out.println("sendMetrics");
        PerfMonMetricGetter instance = new PerfMonMetricGetter(SigarProxyCache.newInstance(new Sigar(), 500), new PerfMonWorker(), DatagramChannel.open());
        instance.getMetricsLine();
    }
}
