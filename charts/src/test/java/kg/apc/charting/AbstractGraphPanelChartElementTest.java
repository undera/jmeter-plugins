package kg.apc.charting;

import org.junit.*;

public class AbstractGraphPanelChartElementTest {

    /**
     *
     */
    public AbstractGraphPanelChartElementTest() {
    }

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of getValue method, of class AbstractGraphPanelChartElement.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        AbstractGraphPanelChartElement instance = new AbstractGraphPanelChartElementImpl();
        double expResult = 0.0;
        double result = instance.getValue();
        Assert.assertEquals(expResult, result, 0.0);
    }

    /**
     * {@inheritDoc}
     */
    public class AbstractGraphPanelChartElementImpl
            extends AbstractGraphPanelChartElement {

        /**
         * {@inheritDoc}
         */
        public double getValue() {
            return 0.0;
        }

        @Override
        public void add(double val) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Test of isPointRepresentative method, of class AbstractGraphPanelChartElement.
     */
    @Test
    public void testIsPointRepresentative() {
        System.out.println("isPointRepresentative");
        int limit = 10;
        AbstractGraphPanelChartElement instance = new AbstractGraphPanelChartElementImpl();
        boolean expResult = true;
        boolean result = instance.isPointRepresentative(limit);
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class AbstractGraphPanelChartElement.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        double val = 0.0;
        AbstractGraphPanelChartElement instance = new AbstractGraphPanelChartElementImpl();
        try {
            instance.add(val);
            Assert.fail("Exception expected");
        } catch (UnsupportedOperationException e) {
        }
    }
}
