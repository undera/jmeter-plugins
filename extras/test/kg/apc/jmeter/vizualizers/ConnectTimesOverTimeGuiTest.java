package kg.apc.jmeter.vizualizers;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectTimesOverTimeGuiTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testDummies() {
        ConnectTimesOverTimeGui obj = new ConnectTimesOverTimeGui();
        obj.getLabelResource();
    }

    @Test
    public void testAdd() {
        ConnectTimesOverTimeGui obj = new ConnectTimesOverTimeGui();
        SampleResult res=new SampleResult();
        obj.add(res);
    }
}