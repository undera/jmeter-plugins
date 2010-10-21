package kg.apc.jmeter.perfmon;

import java.io.IOException;
import javax.net.SocketFactory;
import kg.apc.jmeter.util.SocketEmulatorInputStream;
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
}
