/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import java.io.File;
import java.io.IOException;
import javax.net.SocketFactory;
import kg.apc.jmeter.util.SocketEmulatorInputStream;
import kg.apc.jmeter.util.TestJMeterUtils;
import kg.apc.jmeter.util.TestSocketFactory;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author z000205
 */
public class MetricsProviderTest
{

    private AgentConnector[] connectors =
    {
        new AgentConnector("localhost", 4444), new AgentConnector("server1", 4455)
    };

    public MetricsProviderTest()
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

    public void runWithType(int aType, String data) throws InterruptedException, IOException
    {
        System.out.println("Run With type " + aType + " and data " + data);
        MetricsProvider instance = new MetricsProvider(aType, new AbstractPerformanceMonitoringGuiImpl(), connectors);


        TestSocketFactory sf = new TestSocketFactory();
        SocketEmulatorInputStream is = (SocketEmulatorInputStream) sf.createSocket().getInputStream();
        is.setBytesToRead(data.getBytes());
        instance.setSocketFactory(sf);

        instance.testStarted();
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
        MetricsProvider instance = new MetricsProvider(1, new AbstractPerformanceMonitoringGuiImpl(), connectors);
        TestSocketFactory sf = new TestSocketFactory();
        SocketEmulatorInputStream is = (SocketEmulatorInputStream) sf.createSocket().getInputStream();
        String data = "test\n1\n2\n25\n3:4\n5:6\n7:8\n";
        is.setBytesToRead(data.getBytes());
        instance.setSocketFactory(sf);

        instance.testStarted();
        Thread.sleep(2500);
        instance.testEnded();
    }

    /**
     * Test of run method, of class MetricsProvider.
     */
    @Test
    public void testRun()
    {
        //covered above
    }

    /**
     * Test of testStarted method, of class MetricsProvider.
     */
    @Test
    public void testTestStarted()
    {
        System.out.println("testStarted");
        MetricsProvider instance = new MetricsProvider(1, new AbstractPerformanceMonitoringGuiImpl(), connectors);
        TestSocketFactory sf = new TestSocketFactory();
        instance.setSocketFactory(sf);
        instance.testStarted();
        instance.testEnded();
    }

    /**
     * Test of testEnded method, of class MetricsProvider.
     */
    @Test
    public void testTestEnded()
    {
        System.out.println("testEnded");
        MetricsProvider instance = new MetricsProvider(1, new AbstractPerformanceMonitoringGuiImpl(), connectors);
        TestSocketFactory sf = new TestSocketFactory();
        instance.setSocketFactory(sf);
        instance.testEnded();
    }

    /**
     * Test of setSocketFactory method, of class MetricsProvider.
     */
    @Test
    public void testSetSocketFactory()
    {
        System.out.println("setSocketFactory");
        SocketFactory socketFactory = new TestSocketFactory();
        MetricsProvider instance = new MetricsProvider(2, new AbstractPerformanceMonitoringGuiImpl(), connectors);
        instance.setSocketFactory(socketFactory);
    }

    public class AbstractPerformanceMonitoringGuiImpl
         extends AbstractPerformanceMonitoringGui
   {
      public String getLabelResource()
      {
         return "test";
      }

      public void add(SampleResult sample)
      {
         return;
      }

        @Override
        protected JSettingsPanel getSettingsPanel()
        {
            return new JSettingsPanel(this, true, true, true, true, true);
        }

        @Override
        public String getStaticLabel()
        {
            return "perfmontest";
        }

        @Override
        public void addPerfRecord(String serverName, double value)
        {
            
        }

        @Override
        public void addPerfRecord(String serverName, double value, long time)
        {
            
        }

        @Override
        public void setErrorMessage(String msg)
        {
           
        }

        @Override
        public void clearErrorMessage()
        {
            
        }

        @Override
        public void setChartType(int monitorType)
        {
            
        }

        @Override
        public void setLoadMenuEnabled(boolean enabled)
        {
            
        }
   }

    /**
     * Test of loadFile method, of class MetricsProvider.
     */
    @Test
    public void testLoadFile()
    {
        System.out.println("loadFile");
        File file = null;
        MetricsProvider instance = null;
        instance.loadFile(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
