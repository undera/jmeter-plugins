/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jmeterplugins.save;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Henry
 * @author Vincent Daburon
 */
public class MergeResultsServiceTest {

    // testMergeSample test samples
    public static final List<SampleResult> SAMPLES = Collections
            .unmodifiableList(Arrays.asList(
                    SampleResult.createTestSample(10000, 15000),
                    SampleResult.createTestSample(11000, 16000)));

    public MergeResultsServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
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

    @Test
    public void testMergeSamples() throws Exception {
        String fname = "test-merge.csv";
        CorrectedResultCollector crc = new CorrectedResultCollector();
        crc.getSaveConfig().setAsXml(false);
        crc.setFilename(fname);
        MergeResultsService instance = new MergeResultsService();
        instance.mergeSamples(crc, SAMPLES);

        File f = new File(fname);
        assertEquals(true, f.exists());
        f.delete();
    }

    @Test
    public void testMergeSamples_saveFieldNames() throws Exception {
        String fname = "test-merge.csv";
        CorrectedResultCollector crc = new CorrectedResultCollector();
        crc.getSaveConfig().setAsXml(false);
        crc.setFilename(fname);
        crc.getSaveConfig().setFieldNames(true);
        MergeResultsService instance = new MergeResultsService();
        instance.mergeSamples(crc, SAMPLES);

        File f = new File(fname);
        assertEquals(true, f.exists());
        f.delete();
    }

    @Test
    public void testMergeSamples_nullFilename() throws Exception {
        CorrectedResultCollector crc = new CorrectedResultCollector();
        crc.getSaveConfig().setAsXml(false);
        crc.setFilename(null);
        MergeResultsService instance = new MergeResultsService();
        instance.mergeSamples(crc, SAMPLES);
    }

    @Test
    public void testMergeSamples_emptyFilename() throws Exception {
        CorrectedResultCollector crc = new CorrectedResultCollector();
        crc.getSaveConfig().setAsXml(false);
        crc.setFilename("");
        MergeResultsService instance = new MergeResultsService();
        instance.mergeSamples(crc, SAMPLES);
    }

    @Test
    public void testMergeSamples_XML() throws Exception {
        String fname = "test-merge.csv";
        CorrectedResultCollector crc = new CorrectedResultCollector();
        crc.getSaveConfig().setAsXml(true);
        crc.setFilename(fname);
        MergeResultsService instance = new MergeResultsService();
        instance.mergeSamples(crc, SAMPLES);

        File f = new File(fname);
        assertEquals(true, f.exists());
        f.delete();
    }
}
