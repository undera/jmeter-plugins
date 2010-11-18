package kg.apc.jmeter.perfmon;

import java.io.IOException;
import javax.net.SocketFactory;
import kg.apc.jmeter.util.SocketEmulatorInputStream;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import org.apache.jmeter.gui.util.PowerTableModel;
import kg.apc.jmeter.util.TestJMeterUtils;
import kg.apc.jmeter.util.TestSocketFactory;
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
public class ServerPerfMonitoringGUITest
{
   private PerformanceMonitoringTestElement testElementWithConnectors;

   public ServerPerfMonitoringGUITest()
   {
   }

   @BeforeClass
   public static void setUpClass() throws Exception
   {
      TestJMeterUtils.createJmeterEnv();
   }

   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   @Before
   public void setUp()
   {
      PowerTableModel dataModel = new PowerTableModel(AbstractPerformanceMonitoringGui.columnIdentifiers, AbstractPerformanceMonitoringGui.columnClasses);
      dataModel.addRow(new Object[]
            {
               "localhost", 4444
            });

      testElementWithConnectors = new PerformanceMonitoringTestElement();
      testElementWithConnectors.setData(PerformanceMonitoringTestElement.tableModelToCollectionProperty(dataModel));
   }

   @After
   public void tearDown()
   {
   }

   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      String expResult = "Servers Performance Monitoring";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
   }
/*TODO: to be put in AgentMetricsGetter!
   @Test
   public void testTestStarted()
   {
      System.out.println("testStarted");

      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.configure(testElementWithConnectors);
      TestSocketFactory sf = new TestSocketFactory();
      instance.setSocketFactory(sf);
      instance.testStarted();
      instance.testEnded();
   }

   @Test
   public void testTestEnded()
   {
      System.out.println("testEnded");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.configure(testElementWithConnectors);
      TestSocketFactory sf = new TestSocketFactory();
      instance.setSocketFactory(sf);
      instance.testEnded();
   }

   public void runWithType(int aType, String data) throws InterruptedException, IOException
   {
      System.out.println("Run With type " + aType);
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();


      TestSocketFactory sf = new TestSocketFactory();
      SocketEmulatorInputStream is = (SocketEmulatorInputStream) sf.createSocket().getInputStream();
      is.setBytesToRead(data.getBytes());
      instance.setSocketFactory(sf);

      instance.configure(testElementWithConnectors);
      instance.testStarted();
      instance.selectedPerfMonType = aType;
      Thread.sleep(500);
      instance.testEnded();
   }

   @Test
   public void testRun_CPU() throws InterruptedException, IOException
   {
      runWithType(ServerPerfMonitoringGUI.PERFMON_CPU, "test\n1\n2\n25\n3:4\n5:6\n7:8\n");
   }

   @Test
   public void testRun_MEM() throws InterruptedException, IOException
   {
      runWithType(ServerPerfMonitoringGUI.PERFMON_MEM, "test\n1\n2\n25\n3:4\n5:6\n7:8\n");
   }

   @Test
   public void testRun_DISKS() throws InterruptedException, IOException
   {
      runWithType(ServerPerfMonitoringGUI.PERFMON_DISKS_IO, "test\n3:4\n5:6\n7:8\n");
   }

   @Test
   public void testRun_SWAP() throws InterruptedException, IOException
   {
      runWithType(ServerPerfMonitoringGUI.PERFMON_SWAP, "test\n3:4\n5:6\n7:8\n");
   }

   @Test
   public void testRun_NET() throws InterruptedException, IOException
   {
      runWithType(ServerPerfMonitoringGUI.PERFMON_NETWORKS_IO, "test\n3:4\n5:6\n7:8\n");
   }

   @Test
   public void testRun_BadPerfmon() throws InterruptedException, IOException
   {
      runWithType(-1, "");
   }

   @Test
   public void testRun_BrokenData() throws InterruptedException, IOException
   {
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      TestSocketFactory sf = new TestSocketFactory();
      SocketEmulatorInputStream is = (SocketEmulatorInputStream) sf.createSocket().getInputStream();
      String data = "test\n1\n2\n25\n3:4\n5:6\n7:8\n";
      is.setBytesToRead(data.getBytes());
      instance.setSocketFactory(sf);

      instance.configure(testElementWithConnectors);
      instance.testStarted();
      Thread.sleep(2500);
      instance.testEnded();
   }

   @Test
   public void testRun()
   {
      // covered above
   }

   @Test
   public void testSetSocketFactory()
   {
      System.out.println("setSocketFactory");
      SocketFactory socketFactory = new TestSocketFactory();
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.setSocketFactory(socketFactory);
   }
*/
   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      JSettingsPanel result = instance.getSettingsPanel();
      assertNotNull(result);
   }

    /**
     * Test of addPerfRecord method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testAddPerfRecord_String_double()
    {
        System.out.println("addPerfRecord");
        String serverName = "";
        double value = 0.0;
        ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
        instance.addPerfRecord(serverName, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPerfRecord method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testAddPerfRecord_3args()
    {
        System.out.println("addPerfRecord");
        String serverName = "";
        double value = 0.0;
        long time = 0L;
        ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
        instance.addPerfRecord(serverName, value, time);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setErrorMessage method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testSetErrorMessage()
    {
        System.out.println("setErrorMessage");
        String msg = "";
        ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
        instance.setErrorMessage(msg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearErrorMessage method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testClearErrorMessage()
    {
        System.out.println("clearErrorMessage");
        ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
        instance.clearErrorMessage();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setChartType method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testSetChartType()
    {
        System.out.println("setChartType");
        int monitorType = 0;
        ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
        instance.setChartType(monitorType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
