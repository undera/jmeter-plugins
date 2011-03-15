package kg.apc.emulators;


import kg.apc.jmeter.cmd.PluginsCMD;

    public class PluginsCMDEmul extends PluginsCMD {
        public static void main(String[] args) {
            PluginsCMDEmul cmd = new PluginsCMDEmul();
            cmd.exitWithCode(cmd.processParams(args));
        }

        @Override
        protected void exitWithCode(int i) {
            System.out.println("Exit with "+i);
            if (i!=0)
                throw new RuntimeException("Non-zero exit code "+i);
        }
    }
