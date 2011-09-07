/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter;

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
@Suite.SuiteClasses({kg.apc.jmeter.PluginsCMDWorkerTest.class, kg.apc.jmeter.samplers.SamplersSuite.class, kg.apc.jmeter.RuntimeEOFExceptionTest.class, kg.apc.jmeter.CMDReporterToolTest.class, kg.apc.jmeter.AbstractCMDToolTest.class, kg.apc.jmeter.threads.ThreadsSuite.class, kg.apc.jmeter.JMeterPluginsUtilsTest.class, kg.apc.jmeter.EndOfFileExceptionTest.class, kg.apc.jmeter.timers.TimersSuite.class, kg.apc.jmeter.gui.GuiSuite.class, kg.apc.jmeter.control.ControlSuite.class, kg.apc.jmeter.config.ConfigSuite.class, kg.apc.jmeter.vizualizers.VizualizersSuite.class, kg.apc.jmeter.functions.FunctionsSuite.class, kg.apc.jmeter.graphs.GraphsSuite.class, kg.apc.jmeter.modifiers.ModifiersSuite.class, kg.apc.jmeter.PluginsCMDTest.class, kg.apc.jmeter.reporters.ReportersSuite.class, kg.apc.jmeter.dcerpc.DcerpcSuite.class, kg.apc.jmeter.img.ImgSuite.class})
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
