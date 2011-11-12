// TODO: add SummaryReport support
package kg.apc.jmeter;

import java.io.PrintStream;
import java.util.ListIterator;
import kg.apc.jmeter.AbstractCMDTool;
import kg.apc.jmeter.PluginsCMDWorker;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class ReporterTool extends AbstractCMDTool {

    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    protected void showHelp(PrintStream os) {
        os.println("Options for tool 'Reporter': --generate-png <filename> "
                + "--generate-csv <filename> "
                + "--input-jtl <data file> "
                + "--plugin-type <type> "
                + "[ "
                + "--width <graph width> "
                + "--height <graph height> "
                + "--granulation <ms> "
                + "--relative-times <yes/no> " // aggregate all rows into one ||
                + "--aggregate-rows <yes/no> " // aggregate all rows into one ||
                + "--paint-gradient <yes/no> " // paint gradient background ||
                + "--paint-zeroing <yes/no> " // paint zeroing lines ||
                + "--prevent-outliers <yes/no> " // prevent outliers on distribution graph ||
                + "--limit-rows <num of points> " // limit number of points in row ||
                + "--force-y <limit> " // force Y axis limit ||
                + "--hide-low-counts <limit> " // hide points with sample count below limit ||
                // TODO: add rows enabling/disabling function
                + "]");
    }

    @Override
    protected int processParams(ListIterator<String> args) throws UnsupportedOperationException, IllegalArgumentException {

        PluginsCMDWorker worker = new PluginsCMDWorker();


        while (args.hasNext()) {
            String nextArg = args.next();
            log.debug("Arg: " + nextArg);
            if (nextArg.equalsIgnoreCase("--generate-png")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing PNG file name");
                }

                worker.addExportMode(PluginsCMDWorker.EXPORT_PNG);
                worker.setOutputPNGFile(args.next());
            } else if (nextArg.equalsIgnoreCase("--generate-csv")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing CSV file name");
                }

                worker.addExportMode(PluginsCMDWorker.EXPORT_CSV);
                worker.setOutputCSVFile(args.next());
            } else if (nextArg.equalsIgnoreCase("--input-jtl")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing input JTL file name");
                }

                worker.setInputFile(args.next());
            } else if (nextArg.equalsIgnoreCase("--plugin-type")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing plugin type");
                }

                worker.setPluginType(args.next());
            } else if (nextArg.equalsIgnoreCase("--width")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing width specification");
                }

                worker.setGraphWidth(Integer.parseInt(args.next()));
            } else if (nextArg.equalsIgnoreCase("--height")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing height specification");
                }

                worker.setGraphHeight(Integer.parseInt(args.next()));
            } else if (nextArg.equalsIgnoreCase("--aggregate-rows")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing aggregate flag");
                }

                worker.setAggregate(getLogicValue(args.next()));
            } else if (nextArg.equalsIgnoreCase("--paint-zeroing")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing zeroing flag");
                }

                worker.setZeroing(getLogicValue(args.next()));
            } else if (nextArg.equalsIgnoreCase("--relative-times")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing rel time flag");
                }

                worker.setRelativeTimes(getLogicValue(args.next()));
            } else if (nextArg.equalsIgnoreCase("--paint-gradient")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing gradient flag");
                }

                worker.setGradient(getLogicValue(args.next()));
            } else if (nextArg.equalsIgnoreCase("--prevent-outliers")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing outliers flag");
                }

                worker.setPreventOutliers(getLogicValue(args.next()));
            } else if (nextArg.equalsIgnoreCase("--limit-rows")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing limit rows specification");
                }

                worker.setRowsLimit(Integer.parseInt(args.next()));
            } else if (nextArg.equalsIgnoreCase("--force-y")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing limit Y specification");
                }

                worker.setForceY(Integer.parseInt(args.next()));
            } else if (nextArg.equalsIgnoreCase("--hide-low-counts")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing low counts specification");
                }

                worker.setHideLowCounts(Integer.parseInt(args.next()));
            } else if (nextArg.equalsIgnoreCase("--granulation")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing granulation specification");
                }

                worker.setGranulation(Integer.parseInt(args.next()));
            } else {
                throw new UnsupportedOperationException("Unrecognized option: " + nextArg);
            }
        }

        return worker.doJob();
    }
}
