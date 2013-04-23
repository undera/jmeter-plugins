/*
 * Copyright 2013 undera.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atlantbh.jmeter.plugins.hadooputilities.hdfsoperations;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class HdfsOperationsTest {

    public HdfsOperationsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setExtractedLine method, of class HdfsOperations.
     */
    @Test
    public void testSetExtractedLine() {
        System.out.println("setExtractedLine");
        String extractedLine = "";
        HdfsOperations instance = new HdfsOperations();
        instance.setExtractedLine(extractedLine);
    }

    /**
     * Test of getExtractedLine method, of class HdfsOperations.
     */
    @Test
    public void testGetExtractedLine() {
        System.out.println("getExtractedLine");
        HdfsOperations instance = new HdfsOperations();
        String expResult = "";
        String result = instance.getExtractedLine();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNameNode method, of class HdfsOperations.
     */
    @Test
    public void testSetNameNode() {
        System.out.println("setNameNode");
        String nameNode = "";
        HdfsOperations instance = new HdfsOperations();
        instance.setNameNode(nameNode);
    }

    /**
     * Test of getNameNode method, of class HdfsOperations.
     */
    @Test
    public void testGetNameNode() {
        System.out.println("getNameNode");
        HdfsOperations instance = new HdfsOperations();
        String expResult = "";
        String result = instance.getNameNode();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUploadSuccess method, of class HdfsOperations.
     */
    @Test
    public void testSetUploadSuccess() {
        System.out.println("setUploadSuccess");
        String uploadSuccess = "";
        HdfsOperations instance = new HdfsOperations();
        instance.setUploadSuccess(uploadSuccess);
    }

    /**
     * Test of getUploadSuccess method, of class HdfsOperations.
     */
    @Test
    public void testGetUploadSuccess() {
        System.out.println("getUploadSuccess");
        HdfsOperations instance = new HdfsOperations();
        String expResult = "";
        String result = instance.getUploadSuccess();
        assertEquals(expResult, result);
    }

    /**
     * Test of setInputFilePath method, of class HdfsOperations.
     */
    @Test
    public void testSetInputFilePath() {
        System.out.println("setInputFilePath");
        String inputFilePath = "";
        HdfsOperations instance = new HdfsOperations();
        instance.setInputFilePath(inputFilePath);
    }

    /**
     * Test of getInputFilePath method, of class HdfsOperations.
     */
    @Test
    public void testGetInputFilePath() {
        System.out.println("getInputFilePath");
        HdfsOperations instance = new HdfsOperations();
        String expResult = "";
        String result = instance.getInputFilePath();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOutputFilePath method, of class HdfsOperations.
     */
    @Test
    public void testSetOutputFilePath() {
        System.out.println("setOutputFilePath");
        String outputFilePath = "";
        HdfsOperations instance = new HdfsOperations();
        instance.setOutputFilePath(outputFilePath);
    }

    /**
     * Test of getOutputFilePath method, of class HdfsOperations.
     */
    @Test
    public void testGetOutputFilePath() {
        System.out.println("getOutputFilePath");
        HdfsOperations instance = new HdfsOperations();
        String expResult = "";
        String result = instance.getOutputFilePath();
        assertEquals(expResult, result);
    }

    /**
     * Test of sample method, of class HdfsOperations.
     */
    @Test
    public void testSample() {
        System.out.println("sample");
        Entry e = null;
        HdfsOperations instance = new HdfsOperations();
        SampleResult result = instance.sample(e);
        assertNotNull(result);
    }
}
