package kg.apc.jmeter.threads;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.junit.*;

import static org.junit.Assert.*;

public class UltimateThreadGroupTest {
    private UltimateThreadGroup instance;
    private PowerTableModel dataModel;


    public UltimateThreadGroupTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new UltimateThreadGroup();
        dataModel = getTestModel();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testScheduleThread() {
        System.out.println("scheduleThread");
        HashTree hashtree = new HashTree();
        hashtree.add(new LoopController());
        JMeterThread thread = new JMeterThread(hashtree, null, null);

        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.DATA_PROPERTY);
        instance.setData(prop);
        instance.testStarted();

        instance.scheduleThread(thread);

        assertTrue(thread.getStartTime() > 0);
        assertTrue(thread.getEndTime() > thread.getStartTime());
    }

    @Test
    public void testScheduleThreadAll() {
        System.out.println("scheduleThreadAll");
        HashTree hashtree = new HashTree();
        hashtree.add(new LoopController());

        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.DATA_PROPERTY);
        instance.setData(prop);
        instance.testStarted();

        for (int n = 0; n < instance.getNumThreads(); n++) {
            JMeterThread thread = new JMeterThread(hashtree, null, null);
            thread.setThreadNum(n);
            instance.scheduleThread(thread);
        }
    }

    @Test
    public void testSchedule_Prop() {
        System.out.println("schedule from property");
        String threadsSchedule = "spawn(1,1s,1s,1s,1m) spawn(2,1s,3s,1s,2h)";
        JMeterUtils.setProperty(UltimateThreadGroup.EXTERNAL_DATA_PROPERTY, threadsSchedule);
        UltimateThreadGroup instance = new UltimateThreadGroup();
        JMeterProperty result = instance.getData();
        JMeterUtils.setProperty(UltimateThreadGroup.EXTERNAL_DATA_PROPERTY, ""); // clear!
        assertEquals("[[1, 1, 1, 1, 60], [2, 1, 3, 1, 7200]]", result.toString());
    }

    @Test
    public void testSetData() {
        System.out.println("setSchedule");
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.DATA_PROPERTY);
        instance.setData(prop);
    }

    @Test
    public void testGetData() {
        System.out.println("getSchedule");
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.DATA_PROPERTY);
        instance.setData(prop);
        JMeterProperty result = instance.getData();
        assertFalse(result instanceof NullProperty);
        assertEquals(prop.getStringValue(), result.getStringValue());
    }


    @Test
    public void testGetData_broken_rename() {
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.EXTERNAL_DATA_PROPERTY);
        instance.setProperty(prop);
        JMeterProperty result = instance.getData();
        assertNotNull(instance.getProperty(UltimateThreadGroup.DATA_PROPERTY));
        assertTrue(instance.getProperty(UltimateThreadGroup.EXTERNAL_DATA_PROPERTY) instanceof NullProperty);
        assertFalse(result instanceof NullProperty);
        assertEquals(prop.getStringValue(), result.getStringValue());
    }

    @Test
    public void testGetData_broken_del() {
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.EXTERNAL_DATA_PROPERTY);
        instance.setProperty(prop);
        CollectionProperty prop2 = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.DATA_PROPERTY);
        instance.setProperty(prop2);

        JMeterProperty result = instance.getData();
        assertEquals(prop2, instance.getProperty(UltimateThreadGroup.DATA_PROPERTY));
        assertTrue(instance.getProperty(UltimateThreadGroup.EXTERNAL_DATA_PROPERTY) instanceof NullProperty);
        assertFalse(result instanceof NullProperty);
        assertEquals(prop.getStringValue(), result.getStringValue());
    }

    @Test
    public void testGetNumThreads() {
        System.out.println("getNumThreads");
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(dataModel, UltimateThreadGroup.DATA_PROPERTY);
        instance.setData(prop);
        instance.testStarted();

        int expResult = 15;
        int result = instance.getNumThreads();
        assertEquals(expResult, result);
    }

    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        instance.testStarted();
    }

    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String host = "";
        instance.testStarted(host);
    }

    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        instance.testEnded();
    }

    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String host = "";
        instance.testEnded(host);
    }

    public static PowerTableModel getTestModel() {
        PowerTableModel ret = new PowerTableModel(UltimateThreadGroupGui.columnIdentifiers, UltimateThreadGroupGui.columnClasses);
        ret.addRow(new Integer[]
                {
                        1, 2, 3, 4, 44
                });
        ret.addRow(new Integer[]
                {
                        5, 6, 7, 8, 88
                });
        ret.addRow(new Integer[]
                {
                        9, 10, 11, 12, 122
                });

        return ret;
    }
}
