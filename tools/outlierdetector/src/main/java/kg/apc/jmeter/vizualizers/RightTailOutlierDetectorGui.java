// Needs Java library with minimum level of 1.8 
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

package kg.apc.jmeter.vizualizers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.io.FilenameUtils;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.save.CSVSaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.gui.NumberRenderer;
import org.apache.jorphan.gui.RendererUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kg.apc.jmeter.JMeterPluginsUtils;

public class RightTailOutlierDetectorGui extends AbstractVisualizer implements ActionListener, Clearable {
	/**
	 * For each group of samplers, this non-test element plug-in removes those where
	 * their elapsed time exceeds Tukey's upper fence.
	 */
	private static final long serialVersionUID = 1L;

	// Strings associated with the actions of the control buttons in the UI
	public static final String ACTION_DETECT = "detect";
	public static final String ACTION_SAVE = "save";

	// File extensions that are authorized in the File Panel filter
	private static final String[] EXTS = { ".jtl", ".csv", ".tsv" };

	private static final Logger oLogger = LoggerFactory.getLogger(RightTailOutlierDetectorGui.class);

	// URL for project Wiki page http://jmeter-plugin.org/wiki/wikipage
	public static final String WIKIPAGE = "RightTailOutlierDetection";

	// Internal lists to store the samples grouped by their labels and to save the outliers
	private List<SampleResult> aOutlierList = new ArrayList<SampleResult>();
	private HashMap<String, List<SampleResult>> mSampleList = new HashMap<String, List<SampleResult>>();

	// Only for use in unit tests
	private Boolean bUnitTests = false;

	// Objects for the File Panel selector
	private FilePanel oFilePanel;
	private String sInputFile = null;
	private String sInputFileBaseName;
	private String sInputFileDirectoryName;
	private String sInputFileExtension;

	// Objects to save various results into files
	private FileWriter oFileWriter = null;
	private PrintWriter oPrintWriter = null;
	private String sOutputFile = null;

	// Buttons for Tukey's Control Panel
	private double fTukey_K;
	private final JRadioButton oJRadioButton_1_5 = new JRadioButton("1.5 (detect all outliers)", false);
	private final JRadioButton oJRadioButton_3 = new JRadioButton("3 (detect only extreme values)", true);

	// Table for displaying the results of trimming
	private JTable oJTable = null;
	private PowerTableModel oPowerTableModel = null;

    public void setInputFile(String string) {
    	sInputFile = string;
		sInputFileDirectoryName = FilenameUtils.getFullPath(sInputFile);
		sInputFileBaseName = FilenameUtils.getBaseName(sInputFile);
		sInputFileExtension = FilenameUtils.getExtension(sInputFile);
    }

    public void setTukey(String string) {
    	fTukey_K = Double.parseDouble(string);
    }
 
	// Create the GUI
	public RightTailOutlierDetectorGui() {
		super();
		init();
	}
	
	public void actionPerformed(ActionEvent actionEvt) {
		String _sActionCommand = actionEvt.getActionCommand();
		switch (_sActionCommand) {
		case ACTION_DETECT:
			// Parse filename
			String _sInputFile = oFilePanel.getFilename();
			if (_sInputFile.isEmpty()) {
				if (bUnitTests) {
					System.out.println("RightTailOutlierDetectorGUI_ERROR: file name empty - Please set a filename.");
				} else {
					GuiPackage.showErrorMessage("File name empty - please enter a filename.", "Input file error");
				}
				return;
			}
			if (!bUnitTests) {
				if (!(new File(_sInputFile).exists())) {
					GuiPackage.showErrorMessage("Cannot find specified file - please enter a valid filename.", "Input file error");
					return;
				}
			}

			// Extract filename parameters
	        setInputFile(_sInputFile);

			// Get Tukey's option that was selected
			if (oJRadioButton_1_5.isSelected()) {
				fTukey_K = 1.5;
			} else {
				fTukey_K = 3.0;
			}

			// Clear any statistics from a previous analysis
			// Not called in unit tests so that test data remains for the testing
			if (!bUnitTests) {
				clearData();
			}

			// Now, process the data
			int _iTrimResult = outlierDetection();
			switch (_iTrimResult) {
			case -1:
				if (bUnitTests) {
					System.out.println(
							"RightTailOutlierDetectorGUI_ERROR: No samplers found - please give some samplers!");
				} else {
					GuiPackage.showErrorMessage("No samplers found in results file - please check your file.",
							"Input file error");
				}
				break;
			case 0:
				if (bUnitTests) {
					System.out.println("RightTailOutlierDetectorGUI_INFO: Done, no outliers found.");
				} else {
					GuiPackage.showInfoMessage("No outliers found in the right tail.", "Right Tail Outlier Detection");
				}
				break;
			default:
				if (bUnitTests) {
					System.out.println("RightTailOutlierDetectorGUI_INFO: Done, " + _iTrimResult
							+ " outliers found in the right tail.\n" + "Refer to " + sInputFileBaseName
							+ "_Outliers and " + sInputFileBaseName + "_Trimmed files.");
				} else {
					GuiPackage.showInfoMessage(_iTrimResult + " outliers found in the right tail.\n" + "Refer to "
							+ sInputFileBaseName + "_Outliers and " + sInputFileBaseName + "_Trimmed files.",
							"Right Tail Outlier Detection");
				}
			}
			break;
		case ACTION_SAVE:
			if (oPowerTableModel.getRowCount() == 0) {
				if (bUnitTests) {
					System.out.println("RightTailOutlierDetectorGUI_ERROR: Data table empty!");
				} else {
					GuiPackage.showErrorMessage("Data table empty - please perform Detect before.",
							"Save Table Data error");
				}
				return;
			}
			saveTrimStatistics();
			if (bUnitTests) {
				System.out.println("RightTailOutlierDetectorGUI_INFO: Stats saved to " + sOutputFile + ".");
			} else {
				GuiPackage.showInfoMessage("Data saved to " + sOutputFile + ".", "Save Table Data");
			}
			break;
		default:
			if (bUnitTests) {
				System.out.println("RightTailOutlierDetectorGUI_ERROR: unknown action " + _sActionCommand + ".");
			} else {
				oLogger.warn("RightTailOutlierDetectorGUI: unknown action " + _sActionCommand + ".");
			}
		}
	}

	@Override
	public void add(SampleResult sample) {
		/*
		 * Called by JMeter's engine when we load the data file, but not by
		 * TestJMeterUtils. This will add the samples to mSampleList with label as key.
		 */	
		String _sLabelId = sample.getSampleLabel();
		if (!mSampleList.containsKey(_sLabelId)) {
			// New label sample
			mSampleList.put(_sLabelId, new ArrayList<SampleResult>());
		}
		// Use the default config of this.collector as save properties for the sample
		sample.setSaveConfig(collector.getSaveConfig());
		mSampleList.get(_sLabelId).add(sample);
	}
	
	public void clearData() {
		/*
		 * Called when user clicks on "Clear" or "Clear All" buttons. Clears data
		 * specific to this plugin
		 */
		collector.clearData();
		mSampleList.clear();
		aOutlierList.clear();
		oPowerTableModel.clearData();
	}

	public void clearGui() {
		/*
		 * Called when user selects the plugin in the tree Call default clear method
		 */
		super.clearGui();
	}

	public void createDataModelTable( ) {
		// Grid to store some statistics results after the trimming of samplers
		// TODO: add the new column labels to
		// core/org/apache/jmeter/resources/messages.properties files.
		oPowerTableModel = new PowerTableModel(new String[] { JMeterUtils.getResString("sampler label"), // Label
				JMeterUtils.getResString("aggregate_report_count"), // # Samples
				"Upper Fence", "# Trimmed", // number of samples that have been discarded
				"Trimmed %", // number of samples that have been discarded as a percentage
				"Small Group" // shows a tick if remaining number of samples < 100
		}, new Class[] { String.class, Integer.class, Integer.class, Integer.class, Double.class, Boolean.class });
	}

	public void enableUnitTests() {
		bUnitTests = true;
	}

	public FilePanel getInputFilePanel() {
		return oFilePanel;
	}

	@Override
	public String getLabelResource() {
		/*
		 * TODO get the title name (an possibly translations) from the
		 * message.properties file. The files are located in
		 * core/org/apache/jmeter/resources.
		 */
		return this.getClass().getSimpleName();
	}

	public Collection<String> getMenuCategories() {
		/*
		 * Adds this visualizer to the Non-Test Elements of the JMeter GUI
		 */
		return Arrays.asList(MenuFactory.NON_TEST_ELEMENTS);
	}

	 @Override
	public String getStaticLabel() {
		return JMeterPluginsUtils.prefixLabel("Right Tail Outlier Detection");
	}

	private double getUpperFence(List<SampleResult> dataList) {
		/*
		 * dataList is a list of sample results that have been sorted by increasing
		 * values of elapsed time. This function returns the upper fence in this list as
		 * per following Tukey criteria: upperFence = Q3 + k*(Q3-Q1) where: Q1 = median
		 * of the lower half of the data Q3 = median of the upper half of the data To
		 * select Tukey's Q1 and Q3 hinges: - if there is an odd number of data points
		 * in the original ordered data set, we include the median (i.e., the central
		 * value in the ordered list) in both halves; - if there is an even number of
		 * data points, we split this data set exactly in half; Also please note that in
		 * the following code, when using indexes, we substract 1 because arrays start
		 * at 0!
		 */
		int _iMidHinge, _iLowerHinge, _iUpperHinge;
		double _fQ1Elapsed, _fQ3Elapsed;

		if ((dataList.size() % 2) != 0) {
			// Odd set of numbers in the dataList
			_iMidHinge = (dataList.size() + 1) / 2;
			if ((_iMidHinge % 2) != 0) {
				// Odd set of numbers in each half
				_iLowerHinge = (_iMidHinge + 1) / 2;
				_fQ1Elapsed = dataList.get(_iLowerHinge - 1).getTime();
				_iUpperHinge = _iLowerHinge + _iMidHinge - 1;
				_fQ3Elapsed = dataList.get(_iUpperHinge - 1).getTime();
			} else {
				// Even size in each half: return the average of the 2 middle values
				_iLowerHinge = _iMidHinge / 2;
				_fQ1Elapsed = (dataList.get(_iLowerHinge - 1).getTime() + dataList.get(_iLowerHinge).getTime()) / 2.0;
				_iUpperHinge = _iLowerHinge + _iMidHinge;
				_fQ3Elapsed = (dataList.get(_iUpperHinge - 2).getTime() + dataList.get(_iUpperHinge - 1).getTime())
						/ 2.0;

			}
		} else {
			// Even set of numbers in the dataList
			_iMidHinge = dataList.size() / 2;
			if ((_iMidHinge % 2) != 0) {
				// Odd set of numbers in each half
				_iLowerHinge = (_iMidHinge + 1) / 2;
				_fQ1Elapsed = dataList.get(_iLowerHinge - 1).getTime();
				_iUpperHinge = _iLowerHinge + _iMidHinge;
				_fQ3Elapsed = dataList.get(_iUpperHinge - 1).getTime();
			} else {
				// Even size in each half: return the average of the 2 middle values
				_iLowerHinge = _iMidHinge / 2;
				_fQ1Elapsed = (dataList.get(_iLowerHinge - 1).getTime() + dataList.get(_iLowerHinge).getTime()) / 2.0;
				_iUpperHinge = _iLowerHinge + _iMidHinge;
				_fQ3Elapsed = (dataList.get(_iUpperHinge - 1).getTime() + dataList.get(_iUpperHinge).getTime()) / 2.0;
			}
		}

		// Return the upper fence value
		double _fIqr = _fQ3Elapsed - _fQ1Elapsed; // inter-quartile range
		double _fUpLimit = _fQ3Elapsed + (fTukey_K * _fIqr);
		return _fUpLimit;
	}
	
	public String getWikiPage() {
		return WIKIPAGE;
	}

	private void init() {
		/*
		 * Initialize the components and layout of this component.
		 */
		// Use standard JMeter border
		this.setLayout(new BorderLayout());
		this.setBorder(makeBorder());

		// Add title and help link
		this.add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

		// Create a vertical panel layout scheme to hold the different panels of the UI
		JPanel _oVerticalPanel = new VerticalPanel();

		// Panel for Tukey's options
		JPanel _oJPanelTukey = new JPanel(new BorderLayout());
		ButtonGroup _oButtonGroup = new ButtonGroup();
		_oButtonGroup.add(oJRadioButton_1_5);
		_oButtonGroup.add(oJRadioButton_3);
		_oJPanelTukey.setLayout(new FlowLayout());
		_oJPanelTukey.add(oJRadioButton_1_5);
		_oJPanelTukey.add(oJRadioButton_3);
		_oJPanelTukey.setBorder(BorderFactory.createTitledBorder("Tukey's constant k"));

		// Panel for selection of file
		oFilePanel = new FilePanel("Read results from file and Detect outliers in right tail", EXTS);

		// Detect button
		JPanel _oJPanelDetect = new JPanel();
		JButton _oJButtonDetect = new JButton("Detect");
		_oJButtonDetect.addActionListener(this);
		_oJButtonDetect.setActionCommand(ACTION_DETECT);
		_oJPanelDetect.add(_oJButtonDetect);

		// Grid for some statistics results after the trimming of samplers
		createDataModelTable();
		oJTable = new JTable(oPowerTableModel);
		JMeterUtils.applyHiDPI(oJTable);
		oJTable.setAutoCreateRowSorter(true);
		RendererUtils.applyRenderers(oJTable, new TableCellRenderer[] { null, // Label
				null, // Count
				null, // Upper Fence
				null, // Trim count
				new NumberRenderer("#0.00%"), // Trim %
				null }); // Low Sample indicator
		// Create the scroll pane and add the table to it
		JScrollPane _oJScrollPane = new JScrollPane(oJTable);

		// Save Table button
		JPanel _oJPanelSave = new JPanel();
		JButton _oJButtonSave = new JButton(JMeterUtils.getResString("aggregate_graph_save_table"));
		_oJButtonSave.addActionListener(this);
		_oJButtonSave.setActionCommand(ACTION_SAVE);
		_oJPanelSave.add(_oJButtonSave, BorderLayout.CENTER);

		// Finally, assemble all panels
		_oVerticalPanel.add(_oJPanelTukey);
		_oVerticalPanel.add(oFilePanel);
		_oVerticalPanel.add(_oJPanelDetect);
		_oVerticalPanel.add(_oJScrollPane);
		this.add(_oVerticalPanel, BorderLayout.CENTER);
		this.add(_oJPanelSave, BorderLayout.SOUTH);
		// Hide the default file panel of this class as we are using another file panel
		this.getFilePanel().setVisible(false);
	}

	private PrintWriter initializeFileOutput() {
		try {
			oPrintWriter = new PrintWriter(sOutputFile);
		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		// Save the header line
		oPrintWriter.println(CSVSaveService.printableFieldNamesToString());
		return oPrintWriter;
	}

	public int outlierDetection() {
		SampleEvent _oSampleEvent = null;
		int _iNumberOfTotalObjectsTrimmed = 0;

		// Load the data file using the default collector provided by the AbstractVisualizer class
		collector.setFilename(sInputFile);
		// Set the listener when called from RightTailOutlierDetectorTool
		collector.setListener(this);		
		collector.loadExistingFile();
		if (mSampleList.isEmpty()) {
			return -1; // Nothing to load, so abort...
		}

		// Now, process the data points...
		for (String _sLabelId : mSampleList.keySet()) {
			List<SampleResult> _aLabelSamples = mSampleList.get(_sLabelId);
			int _iNumberOfObjectsBefore = _aLabelSamples.size();
			double _fUpperFence = 0.0, _fUpperFenceMin = Double.MAX_VALUE;

			// Only look for outliers if there are at least four items to compare
			if (_aLabelSamples.size() > 3) {
				// First, sort the samples by their elapsed time
				_aLabelSamples.sort(Comparator.comparingLong(SampleResult::getTime));
				/*
				 * An outlier can hide another outlier...so when removing extreme values, we
				 * have to iterate until no extreme values are left.
				 */
				boolean _bDataTrimmed = true;
				do {
					_fUpperFence = getUpperFence(_aLabelSamples);
					_fUpperFenceMin = Math.min(_fUpperFence, _fUpperFenceMin); // Save the most severe limit for the
																				// report

					// Now remove all samples that are higher than the upper fence
					int _iSampleIndex = _aLabelSamples.size() - 1; // for performance reasons, start by the end
					_bDataTrimmed = false;
					boolean _bSampleTrimmed = true;
					do {
						if (_aLabelSamples.get(_iSampleIndex).getTime() > _fUpperFence) {
							/*
							 * Outlier detected: save the outlier in a new list before removing it from
							 * current list
							 */
							aOutlierList.add(_aLabelSamples.get(_iSampleIndex));
							_aLabelSamples.remove(_iSampleIndex);
							_iSampleIndex--;
							_bDataTrimmed = true;
						} else {
							/*
							 * No more samples to trim: stop parsing the list
							 */
							_bSampleTrimmed = false;
						}
					} while (_bSampleTrimmed);
				} while (_bDataTrimmed);
			}

			// Report the results
			int _iNumberOfObjectsTrimmed = _iNumberOfObjectsBefore - _aLabelSamples.size();
			BigDecimal _bDnumberOfObjectsTrimmedPerCent = new BigDecimal(
					(double) _iNumberOfObjectsTrimmed / (double) _iNumberOfObjectsBefore);
			// Round % to 4 decimal places
			BigDecimal _bDnumberOfObjectsTrimmedPerCentRounded = _bDnumberOfObjectsTrimmedPerCent.setScale(4,
					RoundingMode.HALF_UP);
			_iNumberOfTotalObjectsTrimmed = _iNumberOfTotalObjectsTrimmed + _iNumberOfObjectsTrimmed;
			Boolean _bSmallGroup = false;
			if (_aLabelSamples.size() < 100) {
				_bSmallGroup = true;
			}
			// Update the statistics table
			Object[] _oArrayRowData = { _sLabelId, _iNumberOfObjectsBefore, _fUpperFenceMin, _iNumberOfObjectsTrimmed,
					_bDnumberOfObjectsTrimmedPerCentRounded.doubleValue(), _bSmallGroup };
			oPowerTableModel.addRow(_oArrayRowData);
		}

		// Repaint the table
		oPowerTableModel.fireTableDataChanged();

		// Save the outliers in a file for post analysis
		if (aOutlierList.isEmpty() == false) {
			// Filename containing the excluded samplers only
			sOutputFile = sInputFileDirectoryName + sInputFileBaseName + "_Outliers."
					+ sInputFileExtension;
			oPrintWriter = initializeFileOutput();
			// Now save the outliers
			for (SampleResult _oSampleResult : aOutlierList) {
				_oSampleEvent = new SampleEvent(_oSampleResult, null);
				oPrintWriter.println(CSVSaveService.resultToDelimitedString(_oSampleEvent));
			}
			// Close the file
			oPrintWriter.close();

			// Same thing for the non-trimmed results, saved in a separate file
			sOutputFile = sInputFileDirectoryName + sInputFileBaseName + "_Trimmed." + sInputFileExtension;
			oPrintWriter = initializeFileOutput();
			// Now save the good samplers
			for (String _sLabelId : mSampleList.keySet()) {
				List<SampleResult> _aSampleResult = mSampleList.get(_sLabelId);
				for (SampleResult _oSampleResult : _aSampleResult) {
					_oSampleEvent = new SampleEvent(_oSampleResult, null);
					oPrintWriter.println(CSVSaveService.resultToDelimitedString(_oSampleEvent));
				}
			}
			// Close the file
			oPrintWriter.close();
		}
		return _iNumberOfTotalObjectsTrimmed;
	}
	
    public void saveTrimStatistics() {
		// By default, data saved with comma separated values
		sOutputFile = sInputFileDirectoryName + sInputFileBaseName + "_TrimSummary.csv";
		try {
			oFileWriter = new FileWriter(sOutputFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			CSVSaveService.saveCSVStats(oPowerTableModel, oFileWriter);
			oFileWriter.close();
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}		
	}
}
