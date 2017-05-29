package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleResult;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonitoringSampleResultTest {

    @Test
    public void testSetValue() {
        System.out.println("setValue");
        double value = 0.0;
        MonitoringSampleResult instance = new MonitoringSampleResult();
        instance.setValue(value);
        double expResult = value;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testSetResponseMessage() {
        System.out.println("setResponseMessage");
        String msg = "Test";
        MonitoringSampleResult instance = new MonitoringSampleResult();
        instance.setResponseMessage(msg);
        String expResult = msg;
        String result = instance.getResponseMessage();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetValue0args() {
        System.out.println("getValue");
        MonitoringSampleResult instance = new MonitoringSampleResult();
        instance.setResponseMessage("0");
        double expResult = 0.0;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testGetValueSampleResult() {
        System.out.println("getValue");
        SampleResult res = new SampleResult();
        res.setResponseMessage("0");
        double expResult = 0.0;
        double result = MonitoringSampleResult.getValue(res);
        assertEquals(expResult, result, 0.0);
    }
}
