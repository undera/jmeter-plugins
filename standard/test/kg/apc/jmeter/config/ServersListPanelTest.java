package kg.apc.jmeter.config;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;

public class ServersListPanelTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testClear() {
        ServersListPanel obj = new ServersListPanel();
        new JPanel().add(obj);
        assertEquals(0, obj.getCount());
        obj.add("Test");
        assertEquals(1, obj.getCount());
        obj.clear();
        assertEquals(0, obj.getCount());
    }

    @Test
    public void testSaveToTestElement() {
        DistributedTestControl te = new DistributedTestControl();
        ServersListPanel obj1 = new ServersListPanel();
        new JPanel().add(obj1);
        obj1.add("Test");
        obj1.saveToTestElement(te);
        ServersListPanel obj2 = new ServersListPanel();
        new JPanel().add(obj2);
        assertEquals(0, obj2.getCount());
        obj2.loadFromTestElement(te);
        assertEquals(1, obj2.getCount());
    }
}
