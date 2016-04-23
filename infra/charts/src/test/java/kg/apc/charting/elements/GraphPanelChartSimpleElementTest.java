package kg.apc.charting.elements;

import org.junit.*;

public class GraphPanelChartSimpleElementTest {

    public GraphPanelChartSimpleElementTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class GraphPanelChartSimpleElement.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        double yVal = 0.0;
        GraphPanelChartSimpleElement instance = new GraphPanelChartSimpleElement();
        instance.add(yVal);
    }

    /**
     * Test of getValue method, of class GraphPanelChartSimpleElement.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        GraphPanelChartSimpleElement instance = new GraphPanelChartSimpleElement();
        instance.add(1);
        instance.add(2);
        double expResult = 2.0;
        double result = instance.getValue();
        Assert.assertEquals(expResult, result, 0.0);
    }

}