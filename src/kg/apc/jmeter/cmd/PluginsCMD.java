package kg.apc.jmeter.cmd;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.DynamicClassLoader;
import org.apache.jmeter.NewDriver;

/**
 * This class used to handle all command-line stuff
 * like parameter processing etc. All real work
 * made by CMDWorker
 * @author undera
 * @see CMDWorker
 */
public class PluginsCMD {
    private static final ClassLoader loader;

    static {
        System.setProperty("user.dir", new File(System.getProperty("user.dir")).getParent());

        try {
            NewDriver.addPath("");
            loader=new MyDynamicClassLoader(getURLs());
            Thread.currentThread().setContextClassLoader(loader);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        PluginsCMD cmd = new PluginsCMD();
        System.exit(cmd.processParams(args));
    }

    private static void showHelp() {
        System.out.println("Usage:\n JMeterPluginsCMD "
                + "--help "
                + "--generate-png <filename> "
                + "--generate-csv <filename> "
                + "--input-jtl <data file> "
                + "--plugin-type <type> "
                + "["
                + "--width <graph width> "
                + "--height <graph height> "
                //+ "--aggregate-rows " //TODO: implement option setting
                + "]");

    }

    protected void exitWithCode(int i) {
    }

    public int processParams(String[] args) {
        if (args == null) {
            args = new String[]{"--help"};
        }
        
        CMDWorker worker = new CMDWorker();

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
            } else {
                System.out.println("Unrecognized option: " + args[n]);
                showHelp();
                return 1;
            }
        }
        return worker.doJob();
    }

    private static URL[] getURLs() throws MalformedURLException {
        List<URL> jars = new LinkedList<URL>();
        String[] ss=System.getProperty("java.class.path").split(System.getProperty("path.separator"));

        for (int n=0; n<ss.length; n++)
        {
            //System.out.println(ss[n]);
            jars.add(new File(ss[n]).toURI().toURL());
        }
        return jars.toArray(new URL[0]);
    }

    private static class MyDynamicClassLoader extends DynamicClassLoader {

        public MyDynamicClassLoader(URL[] uRLs) {
            super(uRLs);
            System.out.println("Created class loader with " + uRLs.length);
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            System.out.println("Loading class " + name);
            return super.loadClass(name);
        }

        @Override
        protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            System.out.println("Loading class " + name);
            return super.loadClass(name, resolve);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            System.out.println("Finding class " + name);
            return super.findClass(name);
        }


    }
}
