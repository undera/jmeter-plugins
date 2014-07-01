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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.save.CSVSaveService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * 
 * @author Félix Henry
 * @author Vincent Daburon
 */
public class MergeResultsServiceTest extends TestCase {

	// testMergeSample test samples
	public static final List<SampleResult> SAMPLES = Collections
			.unmodifiableList(Arrays.asList(
					SampleResult.createTestSample(10000, 15000),
					SampleResult.createTestSample(11000, 16000)));

	public MergeResultsServiceTest() {
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

	public void testMergeSamples() throws Exception {
		String fname = "test-merge.csv";
		MergeResultsService.mergeSamples(fname,
				CSVSaveService.printableFieldNamesToString(), SAMPLES);

		File ftest = new File(fname);
		assertEquals(true, ftest.exists());

		LinkedList<String> csvLoaded = readFile(ftest);
		assertEquals(CSVSaveService.printableFieldNamesToString(),
				csvLoaded.pop());
		assertEquals(CSVSaveService.resultToDelimitedString(new SampleEvent(
				SAMPLES.get(0), null)), csvLoaded.pop());
		assertEquals(CSVSaveService.resultToDelimitedString(new SampleEvent(
				SAMPLES.get(1), null)), csvLoaded.pop());

		// Delete the newly saved file test-merge.csv
		ftest.delete();
	}

	private LinkedList<String> readFile(File fname) throws Exception {
		LinkedList<String> lines = new LinkedList<String>();
		BufferedReader bufferReader = null;
		if (fname.exists()) {
			try {
				bufferReader = new BufferedReader(new FileReader(fname),
						50 * 1024);
				String line;
				while ((line = bufferReader.readLine()) != null) {
					lines.addLast(line);
				}
			} finally {
				if (null != bufferReader) {
					bufferReader.close();
				}
			}
		}
		return lines;
	}

}
