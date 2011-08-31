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
    {PerfmonSuite.class, GuiSuite.class, PluginsCMDTest.class, FunctionsSuite.class, JMeterPluginsUtilsTest.class, PluginsCMDWorkerTest.class, PerfMonAgentToolTest.class, PerfMonWorkerTest.class, InvalidPerfMonMetricTest.class, VizualizersSuite.class, SamplersSuite.class, CPUPerfMetricTest.class, ImgSuite.class, CMDReporterToolTest.class, TimersSuite.class, RuntimeEOFExceptionTest.class, ConfigSuite.class, ThreadsSuite.class, ControlSuite.class, AbstractPerfMonMetricTest.class, GraphsSuite.class, PerfMonMetricGetterTest.class, ReportersSuite.class, AbstractCMDToolTest.class, DcerpcSuite.class, EndOfFileExceptionTest.class, ModifiersSuite.class})
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
