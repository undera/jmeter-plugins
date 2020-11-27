package kg.apc.jmeter.vizualizers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JButton;

import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.BeforeClass;
import org.junit.Test;

import kg.apc.emulators.TestJMeterUtils;

public class RightTailOutlierDetectorGuiTest {
	private static String DATA_DIR;

	@BeforeClass
	public static void setUpClass() throws Exception {	
		TestJMeterUtils.createJmeterEnv();
		DATA_DIR = TestJMeterUtils.getTempDir();
	}

	public RightTailOutlierDetectorGuiTest() {
	}

	/*
	 * Test of actionPerformed method, of class RightTailOutlierDetectorGui.
	 */
	@Test
	public void testActionPerformed() {
		long _lStart, _lEnd;
		SampleResult _oSampleResult;
		RightTailOutlierDetectorGui _oRightTailOutlierDetectorGui = new RightTailOutlierDetectorGui();

		// Set the instance into Unit Test mode
		_oRightTailOutlierDetectorGui.enableUnitTests();

		// Set the file to process in the file panel UI
		FilePanel _oFilePanel = _oRightTailOutlierDetectorGui.getInputFilePanel();
		String _sFilePath = DATA_DIR + "/BogusUnitTests.csv";
		_oFilePanel.setFilename(_sFilePath);

		/*
		 * Test of add method, of class RightTailOutlierDetectorGui
		 */
		System.out.println("RightTailOutlierDetectorGuiTest: add");
		// Creation of 100 test results with outliers in the middle
		// We start with some results with random times from 100 to 299ms
		_lStart = System.currentTimeMillis();
		Random _oRandom = new Random();
		for (int i = 0; i < 30; i++) {
			_lEnd = _lStart + _oRandom.nextInt(200) + 100;
			_oSampleResult = SampleResult.createTestSample(_lStart,_lEnd);
			_oSampleResult.setSampleLabel("UnitTest");
			_oRightTailOutlierDetectorGui.add(_oSampleResult);
	        _lStart = _lEnd;
		}
		// Now we add some outliers around 1000ms
		for (int i = 0; i < 10; i++) {
			_lEnd = _lStart + _oRandom.nextInt(1000) + 1000;
			_oSampleResult = SampleResult.createTestSample(_lStart,_lEnd);
			_oSampleResult.setSampleLabel("UnitTest");
			_oRightTailOutlierDetectorGui.add(_oSampleResult);
	        _lStart = _lEnd;
		}
		// We finish with some results with normal random times
		for (int i = 0; i < 60; i++) {
			_lEnd = _lStart + _oRandom.nextInt(200) + 100;
			_oSampleResult = SampleResult.createTestSample(_lStart,_lEnd);
			_oSampleResult.setSampleLabel("UnitTest");
			_oRightTailOutlierDetectorGui.add(_oSampleResult);
	        _lStart = _lEnd;
		}

		// Detect
		System.out.println("RightTailOutlierDetectorGuiTest: actionPerformed_Detect");
		ActionEvent _oActionEvent_Detect = new ActionEvent(new JButton(), 1, "detect");
		_oRightTailOutlierDetectorGui.actionPerformed(_oActionEvent_Detect);

		// Save
		System.out.println("RightTailOutlierDetectorGuiTest: actionPerformed_SaveTableData");
		ActionEvent _oActionEvent_Save = new ActionEvent(new JButton(), 2, "save");
		_oRightTailOutlierDetectorGui.actionPerformed(_oActionEvent_Save);
		// Test for existence of the saved file
		_sFilePath = DATA_DIR + "/BogusUnitTests_TrimSummary.csv";
		File _oFile = new File(_sFilePath);
		assertTrue(_oFile.exists());
	}
	
	/*
	 * Test of clearData method, of class RightTailOutlierDetectorGui.
	 */
	@Test
	public void testClearData() {
		System.out.println("RightTailOutlierDetectorGuiTest: clearData");
		RightTailOutlierDetectorGui _oRightTailOutlierDetectorGui = new RightTailOutlierDetectorGui();
		_oRightTailOutlierDetectorGui.clearData();
	}

	/*
	 * Test of clearGui method, of class RightTailOutlierDetectorGui.
	 */
	@Test
	public void testClearGui() {
		System.out.println("RightTailOutlierDetectorGuiTest: clearGui");
		RightTailOutlierDetectorGui _oRightTailOutlierDetectorGui = new RightTailOutlierDetectorGui();
		_oRightTailOutlierDetectorGui.clearGui();
	}

	/*
	 * Test of getLabelResource method, of class RightTailOutlierDetectorGui.
	 */
	@Test
	public void testgetLabelResource() {
		System.out.println("RightTailOutlierDetectorGuiTest: getLabelResource");
		RightTailOutlierDetectorGui _oRightTailOutlierDetectorGui = new RightTailOutlierDetectorGui();
		String _sExpectedResult = "RightTailOutlierDetectorGui";
		String _sLabelResource = _oRightTailOutlierDetectorGui.getLabelResource();
		assertEquals(_sExpectedResult, _sLabelResource);
	}

	/*
	 * Test of getMenuCategories method, of class RightTailOutlierDetectorGui.
	 */
	@Test
	public void testgetMenuCategories() {
		System.out.println("RightTailOutlierDetectorGuiTest: getMenuCategories");
		RightTailOutlierDetectorGui _oRightTailOutlierDetectorGui = new RightTailOutlierDetectorGui();
		String _sExpectedResult = MenuFactory.NON_TEST_ELEMENTS;
		Collection<String> _oCollection = _oRightTailOutlierDetectorGui.getMenuCategories();
		Iterator<String> _oIterator = _oCollection.iterator();
		assertEquals(_sExpectedResult, _oIterator.next());
	}

	/*
	 * Test of getStaticLabel method, of class RightTailOutlierDetectorGui.
	 */
	@Test
	public void testgetStaticLabel() {
		System.out.println("RightTailOutlierDetectorGuiTest: getStaticLabel");
		RightTailOutlierDetectorGui _oRightTailOutlierDetectorGui = new RightTailOutlierDetectorGui();
		String _sExpectedResult = "jp@gc - Right Tail Outlier Detection";
		String _sStaticLabel = _oRightTailOutlierDetectorGui.getStaticLabel();
		assertEquals(_sExpectedResult, _sStaticLabel);
	}
}
