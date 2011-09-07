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
@Suite.SuiteClasses({JRowsSelectorPanelTest.class, GraphPanelTest.class, CompositeNotifierInterfaceTest.class, AbstractVsThreadVisualizerTest.class, ColorRendererTest.class, HeaderClickCheckAllListenerTest.class, ChartRowsTableTest.class, GraphRendererInterfaceTest.class, HeaderAsTextRendererTest.class, AbstractGraphPanelVisualizerTest.class, SettingsInterfaceTest.class, AbstractOverTimeVisualizerTest.class})
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
