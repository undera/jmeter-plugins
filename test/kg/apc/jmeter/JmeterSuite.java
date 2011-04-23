package kg.apc.jmeter;

import kg.apc.jmeter.charting.ChartingSuite;
import kg.apc.jmeter.cmd.CmdSuite;
import kg.apc.jmeter.config.ConfigSuite;
import kg.apc.jmeter.control.ControlSuite;
import kg.apc.jmeter.dcerpc.DcerpcSuite;
import kg.apc.jmeter.gui.GuiSuite;
import kg.apc.jmeter.modifiers.ModifiersSuite;
import kg.apc.jmeter.perfmon.PerfmonSuite;
import kg.apc.jmeter.reporters.ReportersSuite;
import kg.apc.jmeter.samplers.SamplersSuite;
import kg.apc.jmeter.threads.ThreadsSuite;
import kg.apc.jmeter.timers.TimersSuite;
import kg.apc.jmeter.vizualizers.VizualizersSuite;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author APC
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
    {DcerpcSuite.class, RuntimeEOFExceptionTest.class, ModifiersSuite.class, SamplersSuite.class, PluginsCMDWorkerTest.class, JMeterPluginsUtilsTest.class, PerfmonSuite.class, TimersSuite.class, ConfigSuite.class, PluginsCMDTest.class, GuiSuite.class, CmdSuite.class, ControlSuite.class, EndOfFileExceptionTest.class, ReportersSuite.class, ChartingSuite.class, VizualizersSuite.class, ThreadsSuite.class})
public class JmeterSuite
{

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }
}
