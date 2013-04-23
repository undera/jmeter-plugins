/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.reporters;

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
@Suite.SuiteClasses({LoadosophiaUploaderTest.class, ConsoleStatusLoggerGuiTest.class, FlexibleFileWriterGuiTest.class, JAutoStopPanelTest.class,LoadosophiaAggregatorTest.class, LoadosophiaUploaderGuiTest.class, AutoStopGuiTest.class, AutoStopTest.class, ConsoleStatusLoggerTest.class, FlexibleFileWriterTest.class, LoadosophiaUploadingNotifierTest.class})
public class ReportersSuite {

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