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

package org.jmeterplugins.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.swing.JPanel;

import kg.apc.emulators.TestJMeterUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.jmeterplugins.tools.MergeResultsGui;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Félix Henry
 * @author Vincent Daburon
 */
public class MergeResultsGuiTest {

	public MergeResultsGuiTest() {
	}

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		TestJMeterUtils.createJmeterEnv();
	}

	/**
   *
   */
	@Test
	public void testGetLabelResource() {
		System.out.println("getLabelResource");
		MergeResultsGui instance = new MergeResultsGui();
		String expResult = "MergeResultsGui";
		String result = instance.getLabelResource();
		assertEquals(expResult, result);
	}

	@Test
	public void testGetStaticLabel() {
		System.out.println("getStaticLabel");
		MergeResultsGui instance = new MergeResultsGui();
		String expResult = "jp@gc - Merge Results";
		String result = instance.getStaticLabel();
		assertEquals(expResult, result);
	}

	/**
    *
    */
	@Test
	public void testAdd() {
		System.out.println("add");
		SampleResult res = new SampleResult();
		res.setAllThreads(1);
		res.setThreadName("test 1-2");
		MergeResultsGui instance = new MergeResultsGui();
		instance.add(res);
	}

	@Test
	public void testCreateParamsPanel() {
		System.out.println("getSettingsPanel");
		MergeResultsGui instance = new MergeResultsGui();
		JPanel result = instance.createParamsPanel();
		assertNotNull(result);
	}

	/**
	 * Test of updateUI method, of class MergeResultsGui.
	 */
	@Test
	public void testUpdateUI() {
		System.out.println("updateGui");
		MergeResultsGui instance = new MergeResultsGui();
		instance.updateUI();
	}

	@Test
	public void testCreateTestElement() {
		System.out.println("createTestElement");
		MergeResultsGui instance = new MergeResultsGui();
		TestElement result = instance.createTestElement();
		assertNotNull(result);
	}

	@Test
	public void testModifyTestElement() {
		System.out.println("modifyTestElement");
		TestElement c = new ResultCollector();
		MergeResultsGui instance = new MergeResultsGui();
		instance.modifyTestElement(c);
	}

	@Test
	public void testConfigure() {
		System.out.println("configure");
		TestElement el = new ResultCollector();
		el.setProperty(new StringProperty(MergeResultsGui.FILENAME,
				"fusionRes.csv"));
		MergeResultsGui instance = new MergeResultsGui();
		instance.configure(el);
	}

	/**
	 * Test of getWikiPage method, of class MergeResultsGui.
	 */
	@Test
	public void testGetWikiPage() {
		System.out.println("getWikiPage");
		MergeResultsGui instance = new MergeResultsGui();
		String expResult = "MergeResults";
		String result = instance.getWikiPage();
		assertEquals(expResult, result);
	}

	@Test
	public void testIncludeExclude_none() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);
		SampleResult res = new SampleResult();
		res.setSampleLabel("test");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertEquals("test", vis.lastLabel);
	}

	@Test
	public void testIncludeExclude_include_only() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setIncludeLabels("boom1,test,boom2");
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		SampleResult res = new SampleResult();
		res.setSampleLabel("test");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertEquals("test", vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res2 = new SampleResult();
		res2.setSampleLabel("test1");
		instance.sampleOccurred(new SampleEvent(res2, "tg"));
		assertNull(vis.lastLabel);
	}

	@Test
	public void testIncludeExclude_exclude_only() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setExcludeLabels("boom1,test,boom2");
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		vis.lastLabel = null;
		SampleResult res = new SampleResult();
		res.setSampleLabel("test");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertNull(vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res2 = new SampleResult();
		res2.setSampleLabel("test1");
		instance.sampleOccurred(new SampleEvent(res2, "tg"));
		assertEquals("test1", vis.lastLabel);
	}

	@Test
	public void testIncludeExclude_exclude_include() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setExcludeLabels("boom1,test,boom2");
		instance.setIncludeLabels("boom1,test1,boom2");
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		vis.lastLabel = null;
		SampleResult res = new SampleResult();
		res.setSampleLabel("test");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertNull(vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res2 = new SampleResult();
		res2.setSampleLabel("test1");
		instance.sampleOccurred(new SampleEvent(res2, "tg"));
		assertEquals("test1", vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res3 = new SampleResult();
		res3.setSampleLabel("boom1");
		instance.sampleOccurred(new SampleEvent(res3, "tg"));
		assertNull(vis.lastLabel);
	}

	@Test
	public void testIncludeExcludeRegex_none() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setIncludeLabels("P[0-9].*");
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		vis.lastLabel = null;
		SampleResult res2 = new SampleResult();
		res2.setSampleLabel("P1_TEST");
		instance.sampleOccurred(new SampleEvent(res2, "tg"));
		assertNull(vis.lastLabel);
	}

	@Test
	public void testIncludeExcludeRegex_include_only() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setIncludeLabels("P[0-9].*");
		instance.setEnabledIncludeRegex(true);
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		vis.lastLabel = null;
		SampleResult res = new SampleResult();
		res.setSampleLabel("P1_TEST");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertEquals("P1_TEST", vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res1 = new SampleResult();
		res1.setSampleLabel("T1_TEST");
		instance.sampleOccurred(new SampleEvent(res1, "tg"));
		assertNull(vis.lastLabel);
	}

	@Test
	public void testIncludeExcludeRegex_exclude_only() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setExcludeLabels("P[0-9].*");
		instance.setEnabledExcludeRegex(true);
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		vis.lastLabel = null;
		SampleResult res = new SampleResult();
		res.setSampleLabel("P1_TEST");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertNull(vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res1 = new SampleResult();
		res1.setSampleLabel("T1_TEST");
		instance.sampleOccurred(new SampleEvent(res1, "tg"));
		assertEquals("T1_TEST", vis.lastLabel);
	}

	@Test
	public void testIncludeExcludeRegex_exclude_include() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setExcludeLabels("[T-Z][0-9].*");
		instance.setIncludeLabels("[P-T][0-9].*");
		instance.setEnabledIncludeRegex(true);
		instance.setEnabledExcludeRegex(true);
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		vis.lastLabel = null;
		SampleResult res = new SampleResult();
		res.setSampleLabel("Z1_TEST");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertNull(vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res2 = new SampleResult();
		res2.setSampleLabel("P1_TEST");
		instance.sampleOccurred(new SampleEvent(res2, "tg"));
		assertEquals("P1_TEST", vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res3 = new SampleResult();
		res3.setSampleLabel("T1_TEST");
		instance.sampleOccurred(new SampleEvent(res3, "tg"));
		assertNull(vis.lastLabel);
	}

	@Test
	public void testMinMax_none() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);
		SampleResult res = SampleResult.createTestSample(21000, 30000);
		res.setSampleLabel("test");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertEquals("test", vis.lastLabel);
	}

	@Test
	public void testMinMax_min_only() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setStartOffset("10");
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);
		vis.startTimeRef = 10300;
		vis.startTimeInf = 10000;
		vis.startTimeSup = 11000;

		vis.lastLabel = null;
		SampleResult res = SampleResult.createTestSample(19000, 20000);
		res.setSampleLabel("test");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertNull(vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res1 = SampleResult.createTestSample(21000, 22000);
		res1.setSampleLabel("test1");
		instance.sampleOccurred(new SampleEvent(res1, "tg"));
		assertEquals("test1", vis.lastLabel);

		instance.testEnded();
		instance.setStartOffset("10a");
		instance.testStarted();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		vis.lastLabel = null;
		SampleResult res2 = SampleResult.createTestSample(19000, 20000);
		res2.setSampleLabel("test2");
		instance.sampleOccurred(new SampleEvent(res2, "tg"));
		assertEquals("test2", vis.lastLabel);
	}

	@Test
	public void testMinMax_max_only() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setEndOffset("20");
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);
		vis.startTimeRef = 10300;
		vis.startTimeInf = 10000;
		vis.startTimeSup = 11000;

		vis.lastLabel = null;
		SampleResult res = SampleResult.createTestSample(31500, 32000);
		res.setSampleLabel("test");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertNull(vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res1 = SampleResult.createTestSample(29000, 30000);
		res1.setSampleLabel("test1");
		instance.sampleOccurred(new SampleEvent(res1, "tg"));
		assertEquals("test1", vis.lastLabel);

		instance.testEnded();
		instance.setEndOffset("20a");
		instance.testStarted();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);

		vis.lastLabel = null;
		SampleResult res2 = SampleResult.createTestSample(31000, 32000);
		res2.setSampleLabel("test2");
		instance.sampleOccurred(new SampleEvent(res2, "tg"));
		assertEquals("test2", vis.lastLabel);
	}

	@Test
	public void testMinMax_min_max() {
		CorrectedResultCollector instance = new CorrectedResultCollector();
		instance.setStartOffset("10");
		instance.setEndOffset("20");
		instance.testStarted();
		DebugVisualizer vis = new DebugVisualizer();
		vis.configure(instance);
		vis.setUpFiltering(instance);
		instance.setListener(vis);
		vis.startTimeRef = 10300;
		vis.startTimeInf = 10000;
		vis.startTimeSup = 11000;

		vis.lastLabel = null;
		SampleResult res = SampleResult.createTestSample(31500, 32000);
		res.setSampleLabel("test");
		instance.sampleOccurred(new SampleEvent(res, "tg"));
		assertNull(vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res1 = SampleResult.createTestSample(29000, 30000);
		res1.setSampleLabel("test1");
		instance.sampleOccurred(new SampleEvent(res1, "tg"));
		assertEquals("test1", vis.lastLabel);

		vis.lastLabel = null;
		SampleResult res2 = SampleResult.createTestSample(19000, 20000);
		res2.setSampleLabel("test2");
		instance.sampleOccurred(new SampleEvent(res2, "tg"));
		assertNull(vis.lastLabel);
	}

	private static class DebugVisualizer extends MergeResultsGui {

		String lastLabel;

		public DebugVisualizer() {
		}

		public void add(SampleResult sr) {
			if (isSampleIncluded(sr)) {
				lastLabel = sr.getSampleLabel();
			} else {
				lastLabel = null;
			}
		}

		public boolean isStats() {
			return false;
		}

		@Override
		public String getStaticLabel() {
			return "debug";
		}

		public String getLabelResource() {
			return "debug";
		}

		public String getWikiPage() {
			return "debug";
		}
	}

	/**
	 * Test of setUpFiltering method, of class MergeResultsGui.
	 */
	@Test
	public void testSetUpFiltering() {
		System.out.println("setUpFiltering");
		CorrectedResultCollector rc = new CorrectedResultCollector();
		MergeResultsGui instance = new MergeResultsGui();
		instance.setUpFiltering(rc);
	}

	/**
	 * Test of isSampleIncluded method, of class MergeResultsGui.
	 */
	@Test
	public void testIsSampleIncluded() {
		System.out.println("isSampleIncluded");
		SampleResult res = null;
		MergeResultsGui instance = new MergeResultsGui();
		boolean expResult = true;
		boolean result = instance.isSampleIncluded(res);
		assertEquals(expResult, result);
	}

}
