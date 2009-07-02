/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.dotchart;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author apc
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({kg.apc.jmeter.dotchart.DotChartTest.class,kg.apc.jmeter.dotchart.LeanSampleResultTest.class,kg.apc.jmeter.dotchart.DotChartModelTest.class,kg.apc.jmeter.dotchart.DotChartVisualizerTest.class,kg.apc.jmeter.dotchart.SamplingStatCalculatorColoredTest.class})
public class DotchartSuite {

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