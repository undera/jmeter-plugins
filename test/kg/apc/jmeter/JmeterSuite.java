package kg.apc.jmeter;

import kg.apc.charting.ChartingSuite;
import kg.apc.jmeter.cmd.CmdSuite;
import kg.apc.jmeter.config.ConfigSuite;
import kg.apc.jmeter.control.ControlSuite;
import kg.apc.jmeter.dcerpc.DcerpcSuite;
import kg.apc.jmeter.functions.FunctionsSuite;
import kg.apc.jmeter.graphs.GraphsSuite;
import kg.apc.jmeter.gui.GuiSuite;
import kg.apc.jmeter.img.ImgSuite;
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
    {TimersSuite.class, RuntimeEOFExceptionTest.class, VizualizersSuite.class, PluginsCMDTest.class, ImgSuite.class, ModifiersSuite.class, DcerpcSuite.class, ReportersSuite.class, GraphsSuite.class, ControlSuite.class, JMeterPluginsUtilsTest.class, PluginsCMDWorkerTest.class, PerfmonSuite.class, GuiSuite.class, ConfigSuite.class, SamplersSuite.class, EndOfFileExceptionTest.class, ThreadsSuite.class, FunctionsSuite.class})
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
