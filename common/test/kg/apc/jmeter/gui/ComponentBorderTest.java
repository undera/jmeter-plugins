package kg.apc.jmeter.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import kg.apc.jmeter.gui.ComponentBorder.Edge;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Hoblingre
 */
public class ComponentBorderTest {

    public ComponentBorderTest() {
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
     * Test of isAdjustInsets method, of class ComponentBorder.
     */
    @Test
    public void testIsAdjustInsets() {
        System.out.println("isAdjustInsets");
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        boolean expResult = true;
        boolean result = instance.isAdjustInsets();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAdjustInsets method, of class ComponentBorder.
     */
    @Test
    public void testSetAdjustInsets() {
        System.out.println("setAdjustInsets");
        boolean adjustInsets = false;
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        instance.setAdjustInsets(adjustInsets);
    }

    /**
     * Test of getAlignment method, of class ComponentBorder.
     */
    @Test
    public void testGetAlignment() {
        System.out.println("getAlignment");
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        float expResult = 0.5F;
        float result = instance.getAlignment();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setAlignment method, of class ComponentBorder.
     */
    @Test
    public void testSetAlignment() {
        System.out.println("setAlignment");
        float alignment = 0.1F;
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        instance.setAlignment(alignment);
    }

    /**
     * Test of getEdge method, of class ComponentBorder.
     */
    @Test
    public void testGetEdge() {
        System.out.println("getEdge");
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        Edge expResult = Edge.RIGHT;
        Edge result = instance.getEdge();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEdge method, of class ComponentBorder.
     */
    @Test
    public void testSetEdge() {
        System.out.println("setEdge");
        Edge edge = Edge.LEFT;
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        instance.setEdge(edge);
    }

    /**
     * Test of getGap method, of class ComponentBorder.
     */
    @Test
    public void testGetGap() {
        System.out.println("getGap");
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        int expResult = 5;
        int result = instance.getGap();
        assertEquals(expResult, result);
    }

    /**
     * Test of setGap method, of class ComponentBorder.
     */
    @Test
    public void testSetGap() {
        System.out.println("setGap");
        int gap = 7;
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        instance.setGap(gap);
    }

    /**
     * Test of getBorderInsets method, of class ComponentBorder.
     */
    @Test
    public void testGetBorderInsets() {
        System.out.println("getBorderInsets");
        Component c = null;
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        Insets expResult = new Insets(0, 0, 0, 0);
        Insets result = instance.getBorderInsets(c);
        assertEquals(expResult, result);
    }

    /**
     * Test of isBorderOpaque method, of class ComponentBorder.
     */
    @Test
    public void testIsBorderOpaque() {
        System.out.println("isBorderOpaque");
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        boolean expResult = false;
        boolean result = instance.isBorderOpaque();
        assertEquals(expResult, result);
    }

    /**
     * Test of paintBorder method, of class ComponentBorder.
     */
    @Test
    public void testPaintBorder() {
        System.out.println("paintBorder");
        Component c = null;
        Graphics g = null;
        int x = 0;
        int y = 0;
        int width = 20;
        int height = 5;
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        instance.paintBorder(c, g, x, y, width, height);
    }

    /**
     * Test of install method, of class ComponentBorder.
     */
    @Test
    public void testInstall() {
        System.out.println("install");
        JComponent parent = new JTextField();
        ComponentBorder instance = new ComponentBorder(new JButton("test"));
        instance.install(parent);
    }

}