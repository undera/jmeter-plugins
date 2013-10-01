package kg.apc.jmeter.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServersListPanelTest {
    @Test
    public void testClear() {
        ServersListPanel obj = new ServersListPanel();
        assertEquals(0, obj.getCount());
        obj.add();
        assertEquals(1, obj.getCount());
        obj.clear();
        assertEquals(0, obj.getCount());
    }

    @Test
    public void testSaveToTestElement() {
        DistributedTestControl te = new DistributedTestControl();
        ServersListPanel obj1 = new ServersListPanel();
        obj1.add();
        obj1.saveToTestElement(te);
        ServersListPanel obj2 = new ServersListPanel();
        assertEquals(0, obj2.getCount());
        obj2.loadFromTestElement(te);
        assertEquals(1, obj2.getCount());
    }
}
