package com.googlecode.jmeter.plugins.webdriver.sampler;

import junit.framework.TestCase;

public class SampleResultWithSubsTest extends TestCase {

    public void testSubSampleStart() throws Exception {
        SampleResultWithSubs res = new SampleResultWithSubs();
        res.subSampleStart("test");

        res.subSampleStart("test"); // just to cover warning
    }

    public void testSubSampleEnd() throws Exception {
        SampleResultWithSubs res = new SampleResultWithSubs();
        res.subSampleEnd(true);// just to cover warning

        res.subSampleStart("test");
        res.subSampleEnd(true);
    }

    public void testSampleEnd() throws Exception {
        SampleResultWithSubs res = new SampleResultWithSubs();
        res.subSampleStart("test");

        res.subSampleStart("test"); // just to cover warning
    }
}