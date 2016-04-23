package kg.apc.cmdtools;

import kg.apc.cmd.UniversalRunner;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.log.Priority;
import org.jmeterplugins.tools.FilterResults;

import java.io.PrintStream;
import java.util.ListIterator;

public class FilterResultsTool extends AbstractCMDTool {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private FilterResults filterResults = null;

    public FilterResultsTool() {
        super();
        JMeterPluginsUtils.prepareJMeterEnv(UniversalRunner.getJARLocation());
        filterResults = new FilterResults();

    }

    @Override
    protected void showHelp(PrintStream os) {
        os.println("Options for tool 'FilterResults': --input-file <filenameIn> --output-file <filenameFilteredOut> "
                + " [ "
                + "--success-filter <true/false> "
                + "--include-labels <labels list> "
                + "--exclude-labels <labels list> "
                + "--include-label-regex <true/false> "
                + "--exclude-label-regex <true/false> "
                + "--start-offset <sec> "
                + "--end-offset <sec> "
                + "--save-as-xml <true/false> (false : CSV format by default) "
                + " ]");
    }

    @Override
    protected int processParams(ListIterator args)
            throws UnsupportedOperationException, IllegalArgumentException {


        String outputFile = "out.res";

        LoggingManager.setPriority(Priority.INFO);
        // first process params without worker created
        while (args.hasNext()) {
            String nextArg = (String) args.next();
            if (nextArg.equals("--loglevel")) {
                args.remove();
                String loglevelStr = (String) args.next();
                args.remove();
                LoggingManager.setPriority(loglevelStr);
            }
        }

        // rewind it
        while (args.hasPrevious()) {
            args.previous();
        }


        CorrectedResultCollector collector = filterResults.getCollector();
        SampleSaveConfiguration saveConfig = collector.getSaveConfig();

        while (args.hasNext()) {
            String nextArg = (String) args.next();
            log.debug("Arg: " + nextArg);
            if (nextArg.equalsIgnoreCase("--input-file")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing input file JTL (or CSV) file name");
                }
                collector.setProperty("filename", (String) args.next());
                log.info("--input-file " + collector.getFilename());
            } else if (nextArg.equalsIgnoreCase("--output-file")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing Output file name");
                }
                // outputfile is a parameter to FilterResults
                outputFile = (String) args.next();
                log.info("--output-file " + outputFile);
            } else if (nextArg.equalsIgnoreCase("--include-labels")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing include labels list");
                }

                collector
                        .setIncludeLabels((String) args.next());
                log.info("--include-labels "
                        + collector
                        .getList(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS));
            } else if (nextArg.equalsIgnoreCase("--exclude-labels")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing exclude labels list");
                }

                collector
                        .setExcludeLabels((String) args.next());
                log.info("--exclude-labels "
                        + collector
                        .getList(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS));
            } else if (nextArg.equalsIgnoreCase("--success-filter")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing success filter flag (true/false)");
                }

                collector.setSuccessOnlyLogging(Boolean.valueOf((String) args
                        .next()));
                log.info("--success-filter " + collector.isSuccessOnlyLogging());
            } else if (nextArg.equalsIgnoreCase("--include-label-regex")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing include label regex flag (true/false)");
                }

                collector
                        .setEnabledIncludeRegex(Boolean.valueOf((String) args
                                .next()));
                log.info("--include-label-regex "
                        + collector
                        .getRegexChkboxState(CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE));
            } else if (nextArg.equalsIgnoreCase("--exclude-label-regex")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing exclude label regex flag (true/false)");
                }

                collector
                        .setEnabledExcludeRegex(Boolean.valueOf((String) args
                                .next()));
                log.info("--exclude-label-regex "
                        + collector
                        .getRegexChkboxState(CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE));
            } else if (nextArg.equalsIgnoreCase("--start-offset")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing start offset flag (sec)");
                }

                collector
                        .setStartOffset((String) args.next());
                log.info("--start-offset "
                        + collector
                        .getPropertyAsString(CorrectedResultCollector.START_OFFSET));
            } else if (nextArg.equalsIgnoreCase("--end-offset")) {

                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing end offset flag (sec)");
                }

                collector
                        .setEndOffset((String) args.next());
                log.info("--end-offset "
                        + collector
                        .getPropertyAsString(CorrectedResultCollector.END_OFFSET));
            } else if (nextArg.equalsIgnoreCase("--save-as-xml")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException(
                            "Missing save as xml flag (true/false, true = XML/false = CSV)");
                }

                saveConfig.setAsXml(Boolean.valueOf((String) args.next()));
                log.info("--save-as-xml " + saveConfig.saveAsXml());
            } else {
                throw new UnsupportedOperationException("Unrecognized option: "
                        + nextArg);
            }
        }
        collector.setSaveConfig(saveConfig);
        return doJob(collector, outputFile);
    }

    private int doJob(CorrectedResultCollector collector, String outputFile) {

        return filterResults.doJob(collector, outputFile);

    }
}

