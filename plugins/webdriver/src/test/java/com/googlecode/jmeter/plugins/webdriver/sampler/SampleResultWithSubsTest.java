package com.googlecode.jmeter.plugins.webdriver.sampler;

import junit.framework.TestCase;
import org.junit.Test;

public class SampleResultWithSubsTest {

    @Test
    public void testSubSampleStart() throws Exception {
        SampleResultWithSubs res = new SampleResultWithSubs();
        res.subSampleStart("test");

        res.subSampleStart("test"); // just to cover warning
    }

    @Test
    public void testSubSampleEnd() throws Exception {
        SampleResultWithSubs res = new SampleResultWithSubs();
        res.subSampleEnd(true);// just to cover warning

        res.subSampleStart("test");
        res.subSampleEnd(true);
    }

    @Test
    public void testSampleEnd() throws Exception {
        SampleResultWithSubs res = new SampleResultWithSubs();
        res.subSampleStart("test");

        res.subSampleStart("test"); // just to cover warning
    }
}