package kg.apc.jmeter;

import java.io.IOException;
import kg.apc.emulators.SocketChannelEmul;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class PerfMonMetricGetterTest {

    public PerfMonMetricGetterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of processCommand method, of class PerfMonMetricGetter.
     */
    @Test
    public void testProcessCommand() throws IOException {
        System.out.println("processCommand");
        String toString = "test\ntest\nerr\n";
        PerfMonMetricGetter instance = new PerfMonMetricGetter(new PerfMonWorker(), new SocketChannelEmul());
        instance.addCommandString(toString);
        instance.processNextCommand();
        instance.processNextCommand();
        try {
            instance.processNextCommand();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }
}
