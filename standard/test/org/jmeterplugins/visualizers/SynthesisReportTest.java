package org.jmeterplugins.visualizers;

import static org.junit.Assert.assertEquals;
import kg.apc.emulators.TestJMeterUtils;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SynthesisReportTest {

    public SynthesisReportTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
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

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        SynthesisReport instance = new SynthesisReport();
        String expResult = "SynthesisReport";
        String result = instance.getLabelResource();
        assertEquals(expResult, result);
    }

    @Test
    public void testAdd() {
        System.out.println("add");
        SampleResult res = new SampleResult();
        res.setAllThreads(1);
        res.setThreadName("test 1-2");
        SynthesisReport instance = new SynthesisReport();
        instance.add(res);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new ResultCollector();
        SynthesisReport instance = new SynthesisReport();
        instance.modifyTestElement(c);
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new ResultCollector();
        SynthesisReport instance = new SynthesisReport();
        instance.configure(el);
    }

}
