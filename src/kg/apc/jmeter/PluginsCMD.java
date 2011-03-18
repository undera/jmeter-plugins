package kg.apc.jmeter;

/**
 * This class used to handle all command-line stuff
 * like parameter processing etc. All real work
 * made by CMDWorker
 * @author undera
 * @see CMDWorker
 */
public class PluginsCMD {

    private static void showHelp() {
        System.out.println("JMeter Plugins at Google Code Command-Line Tool "+JMeterPluginsUtils.PLUGINS_VERSION);
        System.out.println("Usage:\n JMeterPluginsCMD "
                + "--help "
                + "--generate-png <filename> "
                + "--generate-csv <filename> "
                + "--input-jtl <data file> "
                + "--plugin-type <type> "
                + "["
                + "--width <graph width> "
                + "--height <graph height> "
                + "--aggregate-rows "
                // TODO: add more options
                + "]");
        System.out.println("For help and support please visit "+JMeterPluginsUtils.WIKI_BASE+"JMeterPluginsCMD");
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
                worker.setAggregate(true);
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
            if (args.length<2)
            {
                showHelp();
            }
            return 1;
        }
    }
}
