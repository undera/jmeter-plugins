package kg.apc.jmeter.graphs;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JRowsSelectorPanelTest {

    public JRowsSelectorPanelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setTable method, of class JRowsSelectorPanel.
     */
    @Test
    public void testSetTable()
    {
        System.out.println("setTable");
        Component table = new JTable();
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        instance.setTable(table);
    }

    /**
     * Test of refreshPreview method, of class JRowsSelectorPanel.
     */
    @Test
    public void testRefreshPreview()
    {
        System.out.println("refreshPreview");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        instance.refreshPreview();
    }

    /**
     * Test of getGraphDisplayPanel method, of class JRowsSelectorPanel.
     */
    @Test
    public void testGetGraphDisplayPanel()
    {
        System.out.println("getGraphDisplayPanel");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        JPanel result = instance.getGraphDisplayPanel();
        assertNotNull(result);
    }

    /**
     * Test of isPreview method, of class JRowsSelectorPanel.
     */
    @Test
    public void testIsPreview()
    {
        System.out.println("isPreview");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        boolean expResult = true;
        boolean result = instance.isPreview();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIncludeSampleLabels method, of class JRowsSelectorPanel.
     */
    @Test
    public void testGetIncludeSampleLabels() {
        System.out.println("getIncludeSampleLabels");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        String expResult = "";
        String result = instance.getIncludeSampleLabels();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIncludeSampleLabels method, of class JRowsSelectorPanel.
     */
    @Test
    public void testSetIncludeSampleLabels() {
        System.out.println("setIncludeSampleLabels");
        String str = "label1;label2";
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        instance.setIncludeSampleLabels(str);
    }

    /**
     * Test of getExcludeSampleLabels method, of class JRowsSelectorPanel.
     */
    @Test
    public void testGetExcludeSampleLabels() {
        System.out.println("getExcludeSampleLabels");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        String expResult = "";
        String result = instance.getExcludeSampleLabels();
        assertEquals(expResult, result);
    }

    /**
     * Test of setExcludeSampleLabels method, of class JRowsSelectorPanel.
     */
    @Test
    public void testSetExcludeSampleLabels() {
        System.out.println("setExcludeSampleLabels");
        String str = "label1;label2";
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        instance.setExcludeSampleLabels(str);
    }

    /**
     * Test of isSelectedRegExpInc method, of class JRowsSelectorPanel.
     */
    @Test
    public void testIsSelectedRegExpInc() {
        System.out.println("isSelectedRegExpInc");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        Boolean result = instance.isSelectedRegExpInc();
        assertFalse(result);
    }

    /**
     * Test of setSelectedRegExpInc method, of class JRowsSelectorPanel.
     */
    @Test
    public void testSetSelectedRegExpInc() {
        System.out.println("setSelectedRegExpInc");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        Boolean b = true;
        instance.setSelectedRegExpInc(b);
    }

    /**
     * Test of isSelectedRegExpExc method, of class JRowsSelectorPanel.
     */
    @Test
    public void testIsSelectedRegExpExc() {
        System.out.println("isSelectedRegExpExc");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        Boolean result = instance.isSelectedRegExpExc();
        assertFalse(result);
    }

    /**
     * Test of setSelectedRegExpExc method, of class JRowsSelectorPanel.
     */
    @Test
    public void testSetSelectedRegExpExc() {
        System.out.println("setSelectedRegExpExc");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        Boolean b = true;
        instance.setSelectedRegExpExc(b);
    }

    /**
     * Test of getStartOffset method, of class JRowsSelectorPanel.
     */
    @Test
    public void testGetStartOffset() {
        System.out.println("getStartOffset");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        String expResult = "";
        String result = instance.getStartOffset();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStartOffset method, of class JRowsSelectorPanel.
     */
    @Test
    public void testSetStartOffset() {
        System.out.println("setStartOffset");
        long l = 1800;
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        instance.setStartOffset(l);
    }

    /**
     * Test of getEndOffset method, of class JRowsSelectorPanel.
     */
    @Test
    public void testGetEndOffset() {
        System.out.println("getEndOffset");
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        String expResult = "";
        String result = instance.getEndOffset();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStartOffset method, of class JRowsSelectorPanel.
     */
    @Test
    public void testSetEndOffset() {
        System.out.println("setEndOffset");
        long l = 1800;
        JRowsSelectorPanel instance = new JRowsSelectorPanel(new GraphPanel());
        instance.setEndOffset(l);
    }
}