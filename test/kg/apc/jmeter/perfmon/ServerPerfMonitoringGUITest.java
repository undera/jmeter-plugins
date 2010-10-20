package kg.apc.jmeter.perfmon;

import org.apache.jmeter.gui.util.PowerTableModel;
import kg.apc.jmeter.util.TestJMeterUtils;
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
      dataModel.addRow(new Object[]
            {
               "server1", 5555
            });
      dataModel.addRow(new Object[]
            {
               "server2", 6666
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
      instance.testStarted();
   }

   @Test
   public void testTestEnded()
   {
      System.out.println("testEnded");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.configure(testElementWithConnectors);
      instance.testEnded();
   }

   @Test
   public void testRun()
   {
      System.out.println("run");
      ServerPerfMonitoringGUI instance = new ServerPerfMonitoringGUI();
      instance.configure(testElementWithConnectors);
      instance.run();
   }
}
