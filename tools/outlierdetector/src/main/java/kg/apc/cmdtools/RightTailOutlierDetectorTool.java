package kg.apc.cmdtools;

import java.io.File;
import java.io.PrintStream;
import java.util.ListIterator;

import kg.apc.cmd.UniversalRunner;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.RightTailOutlierDetectorGui;
import kg.apc.logging.LoggingUtils;

public class RightTailOutlierDetectorTool extends AbstractCMDTool {
	
	public RightTailOutlierDetectorTool() {
        super();
        JMeterPluginsUtils.prepareJMeterEnv(UniversalRunner.getJARLocation());
        LoggingUtils.addLoggingConfig();
	}

	@Override
	protected int processParams(ListIterator args) throws UnsupportedOperationException, IllegalArgumentException {
		/**
		 * Called by the Universal Command Line Tool runner as in "cmdrunner --tool RightTailOutlierDetector"
		 */
		String _sInputFile = null;
		String _sTukeyK = null;

		if (!args.hasNext()) {
			showHelp(System.out);
			return 0;
		}

		// Process params
		while (args.hasNext()) {
			String arg = (String) args.next();
			if (arg.equalsIgnoreCase("--input-file")) {
				if (!args.hasNext()) {
					throw new IllegalArgumentException("Missing JTL (or CSV) input file name.");
				}
				_sInputFile = ((String) args.next());
			} else if (arg.equalsIgnoreCase("--tukey-k")) {
				if (!args.hasNext()) {
					throw new IllegalArgumentException("Missing constant K value.");
				}
				_sTukeyK = ((String) args.next());
			}
		}

		// Check params
		if (_sInputFile == null) {
			throw new IllegalArgumentException("Missing input file specification.");
		}
		if (!(new File(_sInputFile).exists())) {
			throw new IllegalArgumentException("Cannot find specified file: " + _sInputFile);
		}
		if (_sTukeyK == null) {
			throw new IllegalArgumentException("Missing Tukey constant K.");
		}
		if (!(_sTukeyK.equals("1.5")) && !(_sTukeyK.equals("3")) && !(_sTukeyK.equals("3.0"))) {
			throw new IllegalArgumentException("Invalid K value (only 1.5 or 3 accepted)");
		}

		// Do job:
		// Initialize variables
		RightTailOutlierDetectorGui _oRightTailOutlierDetectorGui = new RightTailOutlierDetectorGui();
		_oRightTailOutlierDetectorGui.setInputFile(_sInputFile);
		_oRightTailOutlierDetectorGui.setTukey(_sTukeyK);

		// Now, process the data
		_oRightTailOutlierDetectorGui.createDataModelTable();
		int _iTrimResult = _oRightTailOutlierDetectorGui.outlierDetection();
		System.out.println("RightTailOutlierDetectorTool: " + _iTrimResult);
		switch (_iTrimResult) {
		case -1:
			System.out.println("No samplers found in input file - please check your file.");
			break;
		case 0:
			System.out.println("No outliers found in the right tail.");
			break;
		default:
			_oRightTailOutlierDetectorGui.saveTrimStatistics();
			System.out.println(_iTrimResult + " outliers found in the right tail.\n" + "Refer to the _Outliers and _Trimmed files.");
			System.out.println("Trimming stats saved to _TrimSummary.csv file.");
		}
		return 0;
	}

	@Override
	protected void showHelp(PrintStream os) {
		os.println("Options for tool 'RightTailOutlierDetector': --input-file <filenameIn>"
				+ "--tukey-k <K value (1.5 or 3)>");
	}
}
