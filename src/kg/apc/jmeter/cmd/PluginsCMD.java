package kg.apc.jmeter.cmd;

/**
 * This class used to handle all command-line stuff
 * like parameter processing etc. All real work
 * made by CMDWorker
 * @author undera
 * @see CMDWorker
 */
public class PluginsCMD {

    public static void main(String[] args) {
        PluginsCMD cmd = new PluginsCMD();
        System.exit(cmd.processParams(args));
    }

    private static void showHelp() {
        System.out.println("Usage:\n <cmd> --help --generate-png <filename> --generate-csv <filename> --input-jtl <data file> --plugin-type <type>");

    }

    protected void exitWithCode(int i) {
    }

    public int processParams(String[] args) {
        if (args == null) {
            args = new String[]{"--help"};
        }

        CMDWorker worker=new CMDWorker();

        for (int n = 0; n < args.length; n++) {
            if (args[n].equals("-?") || args[n].equals("--help")) {
                showHelp();
                return 0;
            } else if (args[n].equalsIgnoreCase("--generate-png")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing PNG file name");
                }

                worker.addExportMode(CMDWorker.EXPORT_PNG);
                worker.setOutputPNGFile(args[n]);
            } else if (args[n].equalsIgnoreCase("--generate-csv")) {
                n++;
                if (n >= args.length) {
                    throw new IllegalArgumentException("Missing CSV file name");
                }

                worker.addExportMode(CMDWorker.EXPORT_CSV);
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
            } else {
                showHelp();
                return 1;
            }
        }
        return worker.doJob();
    }
}
