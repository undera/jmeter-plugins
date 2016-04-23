package org.jmeterplugins.visualizers.gui;

import static org.junit.Assert.assertEquals;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class FilterPanelTest {

    public FilterPanelTest() {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetIncludeSampleLabels() {
        System.out.println("getIncludeSampleLabels");
        FilterPanel instance = new FilterPanel();
        String expResult = "";
        String result = instance.getIncludeSampleLabels();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetIncludeSampleLabels() {
        System.out.println("setIncludeSampleLabels");
        FilterPanel instance = new FilterPanel();
        instance.setIncludeSampleLabels("include");
    }

    @Test
    public void testGetExcludeSampleLabels() {
        System.out.println("getExcludeSampleLabels");
        FilterPanel instance = new FilterPanel();
        String expResult = "";
        String result = instance.getExcludeSampleLabels();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetExcludeSampleLabels() {
        System.out.println("setExcludeSampleLabels");
        FilterPanel instance = new FilterPanel();
        instance.setExcludeSampleLabels("exclude");
    }

    @Test
    public void testIsSelectedRegExpInc() {
        System.out.println("isSelectedRegExpInc");
        FilterPanel instance = new FilterPanel();
        boolean expResult = false;
        boolean result = instance.isSelectedRegExpInc();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetSelectedRegExpInc() {
        System.out.println("setSelectedRegExpInc");
        FilterPanel instance = new FilterPanel();
        instance.setSelectedRegExpInc(true);
    }

    @Test
    public void testIsSelectedRegExpExc() {
        System.out.println("isSelectedRegExpExc");
        FilterPanel instance = new FilterPanel();
        boolean expResult = false;
        boolean result = instance.isSelectedRegExpExc();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetSelectedRegExpExc() {
        System.out.println("setSelectedRegExpExc");
        FilterPanel instance = new FilterPanel();
        instance.setSelectedRegExpExc(true);
    }

    @Test
    public void testGetStartOffset() {
        System.out.println("getStartOffset");
        FilterPanel instance = new FilterPanel();
        String expResult = "";
        String result = instance.getStartOffset();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetStartOffset() {
        System.out.println("setStartOffset");
        FilterPanel instance = new FilterPanel();
        instance.setStartOffset(180);
    }

    @Test
    public void testGetEndOffset() {
        System.out.println("getEndOffset");
        FilterPanel instance = new FilterPanel();
        String expResult = "";
        String result = instance.getEndOffset();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetEndOffset() {
        System.out.println("setEndOffset");
        FilterPanel instance = new FilterPanel();
        instance.setEndOffset(180);
    }

    @Test
    public void testClearGui() {
        System.out.println("clearGui");
        FilterPanel instance = new FilterPanel();
        instance.clearGui();
    }

}
