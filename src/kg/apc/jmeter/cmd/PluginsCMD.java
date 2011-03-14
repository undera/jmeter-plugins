package kg.apc.jmeter.cmd;

/**
 *
 * @author undera
 */
public class PluginsCMD {

    public static void main(String[] args) {
        PluginsCMD cmd = new PluginsCMD();
        System.exit(cmd.processParams(args));
    }

    private static void showHelp() {
        System.out.println("Usage:\n <cmd> --help");

    }

    protected void exitWithCode(int i) {
        
    }

    public int processParams(String[] args) {
        if (args==null)
        {
            args=new String[] {"--help"};
        }

        for (int n = 0; n < args.length; n++) {
            if (args[n].equals("-?") || args[n].equals("--help")) {
                showHelp();
                return 0;
            } else {
                showHelp();
                return 1;
            }
        }
        return 0;
    }
}
