package kg.apc.jmeter.functions;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IterationNumTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testExecute() throws Exception {
        IterationNum obj = new IterationNum();
        String res = obj.execute(null, null);
        assertEquals("0", res);
    }

}