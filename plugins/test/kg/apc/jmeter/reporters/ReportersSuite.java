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
@Suite.SuiteClasses({LoadosophiaUploaderTest.class, LoadosophiaUploaderGuiTest.class, ConsoleStatusLoggerTest.class, FlexibleFileWriterTest.class, FlexibleFileWriterGuiTest.class, ConsoleStatusLoggerGuiTest.class, AutoStopGuiTest.class, JAutoStopPanelTest.class, AutoStopTest.class})
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