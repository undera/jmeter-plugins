/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter;

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
 * @author undera
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({PluginsCMDWorkerTest.class, SamplersSuite.class, RuntimeEOFExceptionTest.class, ThreadsSuite.class, JMeterPluginsUtilsTest.class, kg.apc.cmdtools.ReporterToolTest.class, EndOfFileExceptionTest.class, TimersSuite.class, GuiSuite.class, ControlSuite.class, ConfigSuite.class, VizualizersSuite.class, GraphsSuite.class, FunctionsSuite.class, ModifiersSuite.class, PerfmonSuite.class, ReportersSuite.class, DcerpcSuite.class, ImgSuite.class})
public class JmeterSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
