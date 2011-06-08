/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.graphs;

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
@Suite.SuiteClasses({HeaderClickCheckAllListenerTest.class, GraphRendererInterfaceTest.class, ColorRendererTest.class, CompositeNotifierInterfaceTest.class, AbstractVsThreadVisualizerTest.class, ChartRowsTableTest.class, SettingsInterfaceTest.class, AbstractOverTimeVisualizerTest.class, JRowsSelectorPanelTest.class, HeaderAsTextRendererTest.class, AbstractGraphPanelVisualizerTest.class, GraphPanelTest.class})
public class GraphsSuite {

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
