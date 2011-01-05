package kg.apc.jmeter.perfmon;

import kg.apc.jmeter.charting.GraphPanelChart;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
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
   }

   @After
   public void tearDown()
   {
   }

   @Test
   public void testGetStaticLabel()
   {
      System.out.println("getStaticLabel");
      ServerPerfMonitoringGui instance = new ServerPerfMonitoringGui();
      String expResult = "Servers Performance Monitoring";
      String result = instance.getStaticLabel();
      assertEquals(expResult, result);
   }
   @Test
   public void testGetSettingsPanel()
   {
      System.out.println("getSettingsPanel");
      ServerPerfMonitoringGui instance = new ServerPerfMonitoringGui();
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
        String serverName = "test";
        double value = 3.0;
        ServerPerfMonitoringGui instance = new ServerPerfMonitoringGui();
        instance.addPerfRecord(serverName, value);
    }

    /**
     * Test of addPerfRecord method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testAddPerfRecord_3args()
    {
        System.out.println("addPerfRecord");
        String serverName = "test";
        double value = 3.0;
        long time = 100000L;
        ServerPerfMonitoringGui instance = new ServerPerfMonitoringGui();
        instance.addPerfRecord(serverName, value, time);
    }

    /**
     * Test of setErrorMessage method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testSetErrorMessage()
    {
        System.out.println("setErrorMessage");
        String msg = "Error message.";
        ServerPerfMonitoringGui instance = new ServerPerfMonitoringGui();
        instance.setErrorMessage(msg);
    }

    /**
     * Test of clearErrorMessage method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testClearErrorMessage()
    {
        System.out.println("clearErrorMessage");
        ServerPerfMonitoringGui instance = new ServerPerfMonitoringGui();
        String msg = "Error message.";
        instance.setErrorMessage(msg);
        instance.clearErrorMessage();
    }

    /**
     * Test of setChartType method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testSetChartType()
    {
        System.out.println("setChartType");
        int monitorType = GraphPanelChart.CHART_PERCENTAGE;
        ServerPerfMonitoringGui instance = new ServerPerfMonitoringGui();
        instance.setChartType(monitorType);
    }

    /**
     * Test of setLoadMenuEnabled method, of class ServerPerfMonitoringGUI.
     */
    @Test
    public void testSetLoadMenuEnabled()
    {
        System.out.println("setLoadMenuEnabled");
        boolean enabled = false;
        ServerPerfMonitoringGui instance = new ServerPerfMonitoringGui();
        instance.setLoadMenuEnabled(enabled);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
