package kg.apc.jmeter.modifiers;

import java.net.MalformedURLException;
import java.net.URL;
import kg.apc.emulators.EmulatorJmeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.*;

/**
 *
 * @author undera
 */
public class AnchorModifierTest {

    public AnchorModifierTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        JMeterContext context = JMeterContextService.getContext();
        context.setEngine(new EmulatorJmeterEngine());
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
     * Test of process method, of class AnchorModifier.
     */
    @Test
    public void testProcess() throws MalformedURLException {
        System.out.println("process");
        AnchorModifier instance = new AnchorModifier();
        JMeterContext context = JMeterContextService.getContext();
        Sampler s = new HTTPSampler();
        HTTPSampleResult res = new HTTPSampleResult();
        res.setURL(new URL("http://test/"));
        String data = "<a href='http://test'/><a href='http://test/2'/><a href='http://test'/>";
        data += "<a href='testsub'/><a href='http://test/2'/><a href='http://test'/>";
        res.setResponseData(data.getBytes());
        context.setPreviousResult(res);
        context.setCurrentSampler(s);
        instance.process();
        instance.process();
        instance.process();
    }
}
