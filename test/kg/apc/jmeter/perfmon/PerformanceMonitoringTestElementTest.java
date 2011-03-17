package kg.apc.jmeter.perfmon;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author APC
 */
public class PerformanceMonitoringTestElementTest {

    private PerformanceMonitoringTestElement instance;
    private PowerTableModel dataModel;

    public PerformanceMonitoringTestElementTest() {
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
        instance = new PerformanceMonitoringTestElement();
        instance.setType(2);

        dataModel = new PowerTableModel(AbstractPerformanceMonitoringGui.columnIdentifiers, AbstractPerformanceMonitoringGui.columnClasses);
        dataModel.addRow(new Object[]{
                    "localhost", 4444
                });
        dataModel.addRow(new Object[]{
                    "server1", 5555
                });
        dataModel.addRow(new Object[]{
                    "server2", 6666
                });

        instance.setData(PerformanceMonitoringTestElement.tableModelToCollectionProperty(dataModel));

        AbstractPerformanceMonitoringGui gui = new AbstractPerformanceMonitoringGuiImpl();
        instance.register(gui);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTableModelToCollectionProperty() {
        System.out.println("tableModelToCollectionProperty");
        CollectionProperty result = PerformanceMonitoringTestElement.tableModelToCollectionProperty(dataModel);
        assertTrue(result instanceof CollectionProperty);
    }

    @Test
    public void testGetData() {
        System.out.println("getData");
        CollectionProperty prop = PerformanceMonitoringTestElement.tableModelToCollectionProperty(dataModel);
        instance.setData(prop);
        JMeterProperty result = instance.getData();
        assertFalse(result instanceof NullProperty);
        assertEquals(prop.getStringValue(), result.getStringValue());
    }

    @Test
    public void testSetData() {
        System.out.println("setData");
        CollectionProperty prop = PerformanceMonitoringTestElement.tableModelToCollectionProperty(dataModel);
        instance.setData(prop);
    }

    @Test
    public void testGetType() {
        System.out.println("getType");
        int expResult = 2;
        int result = instance.getType();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetType() {
        System.out.println("setType");
        int type = 3;
        instance.setType(type);
    }

    @Test
    public void testClearData() {
        System.out.println("clearData");
        instance.clearData();
    }

    @Test
    public void testRegister() {
        System.out.println("register");
        AbstractPerformanceMonitoringGui gui = new AbstractPerformanceMonitoringGuiImpl();
        instance.register(gui);
        assertTrue(gui == instance.gui);
    }

    @Test
    public void testClone() {
        System.out.println("clone");
        Object result = instance.clone();
        assertTrue(instance.gui == ((PerformanceMonitoringTestElement) result).gui);
    }

    @Test
    public void testTestStarted_0args() {
        System.out.println("testStarted");
        dataModel.clearData();
        dataModel.addRow(new Object[]{
                    "localhost", 4444
                });
        instance.setData(PerformanceMonitoringTestElement.tableModelToCollectionProperty(dataModel));
        instance.testStarted();
    }

    @Test
    public void testTestStarted_String() {
        System.out.println("testStarted");
        String string = "host";
        instance.testStarted(string);
    }

    @Test
    public void testTestEnded_0args() {
        System.out.println("testEnded");
        instance.testEnded();
    }

    @Test
    public void testTestEnded_String() {
        System.out.println("testEnded");
        String string = "host";
        instance.testEnded(string);
    }

    @Test
    public void testTestIterationStart() {
        System.out.println("testIterationStart");
        LoopIterationEvent lie = null;
        instance.testIterationStart(lie);
    }

    public class AbstractPerformanceMonitoringGuiImpl
            extends AbstractPerformanceMonitoringGui {

        public String getStaticLabel() {
            return "test";
        }

        public void addPerfRecord(String serverName, double value) {
        }

        @Override
        public void addPerfRecord(String serverName, double value, long time) {
        }

        @Override
        public void setErrorMessage(String msg) {
        }

        @Override
        public void clearErrorMessage() {
        }

        public void setChartType(int monitorType) {
        }

        @Override
        protected JSettingsPanel getSettingsPanel() {
            return new JSettingsPanel(this, JSettingsPanel.GRADIENT_OPTION);
        }

        @Override
        public void setLoadMenuEnabled(boolean enabled) {
        }

        @Override
        public String getWikiPage() {
            return "";
        }
    }
}
