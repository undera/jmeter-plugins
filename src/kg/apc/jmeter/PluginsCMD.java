package kg.apc.jmeter;
// TODO: add rows enabling/disabling function
// TODO: add SummaryReport support
// TODO: add --version support here, in JMeterPlugins.jar and in agent

/**
 * This class used to handle all command-line stuff
 * like parameter processing etc. All real work
 * made by CMDWorker
 * @author undera
 * @see CMDWorker
 */
public class PluginsCMD {

    private static void showHelp() {
        System.out.println("JMeter Plugins at Google Code Command-Line Tool " + JMeterPluginsUtils.PLUGINS_VERSION);
        System.out.println("Usage:\n JMeterPluginsCMD "
                + "--help "
                + "--generate-png <filename> "
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
                // TODO: add more options
                + "]");
        System.out.println("For help and support please visit " + JMeterPluginsUtils.WIKI_BASE + "JMeterPluginsCMD");
    }

    public int processParams(String[] args) {
        if (args == null) {
            args = new String[]{"--help"};
        }

        PluginsCMDWorker worker = new PluginsCMDWorker();

        for (int n = 0; n < args.length; n++) {
            if (args[n].equals("-?") || args[n].equals("--help")) {
                showHelp();
                return 0;
            } else if (args[n].equalsIgnoreCase("--generate-png")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing PNG file name");
                }

                worker.addExportMode(PluginsCMDWorker.EXPORT_PNG);
                worker.setOutputPNGFile(args[n]);
            } else if (args[n].equalsIgnoreCase("--generate-csv")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing CSV file name");
                }

                worker.addExportMode(PluginsCMDWorker.EXPORT_CSV);
                worker.setOutputCSVFile(args[n]);
            } else if (args[n].equalsIgnoreCase("--input-jtl")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing input JTL file name");
                }

                worker.setInputFile(args[n]);
            } else if (args[n].equalsIgnoreCase("--plugin-type")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing plugin type");
                }

                worker.setPluginType(args[n]);
            } else if (args[n].equalsIgnoreCase("--width")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing width specification");
                }

                worker.setGraphWidth(Integer.parseInt(args[n]));
            } else if (args[n].equalsIgnoreCase("--height")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing height specification");
                }

                worker.setGraphHeight(Integer.parseInt(args[n]));
            } else if (args[n].equalsIgnoreCase("--aggregate-rows")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing aggregate flag");
                }

                worker.setAggregate(getLogicValue(args[n]));
            } else if (args[n].equalsIgnoreCase("--paint-zeroing")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing zeroing flag");
                }

                worker.setZeroing(getLogicValue(args[n]));
            } else if (args[n].equalsIgnoreCase("--relative-times")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing rel time flag");
                }

                worker.setRelativeTimes(getLogicValue(args[n]));
            } else if (args[n].equalsIgnoreCase("--paint-gradient")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing gradient flag");
                }

                worker.setGradient(getLogicValue(args[n]));
            } else if (args[n].equalsIgnoreCase("--prevent-outliers")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing outliers flag");
                }

                worker.setPreventOutliers(getLogicValue(args[n]));
            } else if (args[n].equalsIgnoreCase("--limit-rows")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing limit rows specification");
                }

                worker.setRowsLimit(Integer.parseInt(args[n]));
            } else if (args[n].equalsIgnoreCase("--force-y")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing limit Y specification");
                }

                worker.setForceY(Integer.parseInt(args[n]));
            } else if (args[n].equalsIgnoreCase("--hide-low-counts")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing low counts specification");
                }

                worker.setHideLowCounts(Integer.parseInt(args[n]));
            } else if (args[n].equalsIgnoreCase("--granulation")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing granulation specification");
                }

                worker.setGranulation(Integer.parseInt(args[n]));
            } else if (args[n].trim().length() == 0) {
            } else {
                System.out.println("Unrecognized option: " + args[n]);
                showHelp();
                return 1;
            }
        }

        try {
            return worker.doJob();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            if (args.length < 2) {
                showHelp();
            }
            return 1;
        }
    }

    private int getLogicValue(String string) {
        if (string.equalsIgnoreCase("on")) {
            return 1;
        }
        if (string.equalsIgnoreCase("1")) {
            return 1;
        }
        if (string.equalsIgnoreCase("yes")) {
            return 1;
        }
        return 0;
    }
}
