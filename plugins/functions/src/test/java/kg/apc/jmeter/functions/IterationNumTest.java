package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class IterationNumTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testExecute() throws Exception {
        IterationNum obj = new IterationNum();
        String res = obj.execute(null, null);
        Assert.assertEquals("0", res);
    }

}