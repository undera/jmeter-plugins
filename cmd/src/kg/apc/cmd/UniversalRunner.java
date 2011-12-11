package kg.apc.cmd;

// N.B. this must only use standard Java packages
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Main class for CLI - sets up initial classpath and the loader.
 * I took it from JMeter, yes, but I changed it a lot.
 */
public final class UniversalRunner {

    private static final String CLASSPATH_SEPARATOR = System.getProperty("path.separator");// $NON-NLS-1$
    private static final String OS_NAME = System.getProperty("os.name");// $NON-NLS-1$
    private static final String OS_NAME_LC = OS_NAME.toLowerCase(java.util.Locale.ENGLISH);
    private static final String JAVA_CLASS_PATH = "java.class.path";// $NON-NLS-1$
    /** The directory JMeter is installed in. */
    private static final String jarDirectory;

    static {
        //System.setProperty("user.dir", new File(System.getProperty("user.dir")).getParent());
        List jars = new LinkedList();
        final String initial_classpath = System.getProperty(JAVA_CLASS_PATH);
        jarDirectory = getJarDirectory(initial_classpath);

        // Add standard jar locations to initial classpath
        StringBuilder classpath = buildUpdatedClassPath(jars);

        // ClassFinder needs the classpath
        System.setProperty(JAVA_CLASS_PATH, initial_classpath + classpath.toString() + ":");

        URL[] urls = (URL[]) jars.toArray(new URL[0]);
        URLClassLoader loader = new URLClassLoader(urls);
        Thread.currentThread().setContextClassLoader(loader);
    }

    private static String getJarDirectory(final String initial_classpath) {
        // Find JMeter home dir from the initial classpath
        String tmpDir = null;
        StringTokenizer tok = new StringTokenizer(initial_classpath, File.pathSeparator);
        //System.err.println("CP: "+initial_classpath);
        if (tok.countTokens() == 1
                || (tok.countTokens() == 2 // Java on Mac OS can add a second entry to the initial classpath
                && OS_NAME_LC.startsWith("mac os x")// $NON-NLS-1$
                )) {
            File jar = new File(tok.nextToken());
            try {
                tmpDir = jar.getCanonicalFile().getParent();
                //System.err.println("Can: "+tmpDir);
            } catch (IOException e) {
            }
        } else {// e.g. started from IDE with full classpath
            File userDir = new File(System.getProperty("user.dir"));// $NON-NLS-1$
            tmpDir = userDir.getAbsolutePath();
        }
        return tmpDir;
    }

    private static StringBuilder buildUpdatedClassPath(List jars) {
        StringBuilder classpath = new StringBuilder();
        /*
         * Does the system support UNC paths? If so, may need to fix them up
         * later
         */
        boolean usesUNC = OS_NAME_LC.startsWith("windows");// $NON-NLS-1$

        List libDirs = new LinkedList();
        File f = new File(jarDirectory);
        while (f != null) {
            libDirs.add(f.getAbsoluteFile());
            f = f.getParentFile();
        }

        Iterator it = libDirs.iterator();

        while (it.hasNext()) {
            File libDir = (File) it.next();
            File[] libJars = libDir.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {// only accept jar files
                    return name.endsWith(".jar");// $NON-NLS-1$
                }
            });
            if (libJars == null) {
                new Throwable("Could not access " + libDir).printStackTrace(System.err);
                continue;
            }
            for (int i = 0; i < libJars.length; i++) {
                try {
                    String s = libJars[i].getPath();

                    // Fix path to allow the use of UNC URLs
                    if (usesUNC) {
                        if (s.startsWith("\\\\") && !s.startsWith("\\\\\\")) {
                            s = "\\\\" + s;
                        } else if (s.startsWith("//") && !s.startsWith("///")) {
                            s = "//" + s;
                        }
                    } // usesUNC

                    jars.add(new File(s).toURI().toURL());// See Java bug 4496398
                    classpath.append(CLASSPATH_SEPARATOR);
                    classpath.append(s);
                    //System.err.println(s);
                } catch (MalformedURLException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
        return classpath;
    }

    /**
     * Prevent instantiation.
     */
    private UniversalRunner() {
    }

    /**
     * Get the directory where CMD jar is placed. This is the absolute path
     * name.
     *
     * @return the directory where JMeter is installed.
     */
    public static String getJMeterDir() {
        return jarDirectory;
    }

    /**
     * The main program which actually runs JMeter.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) throws Throwable {
        try {
            Class initialClass;
            // make it independent - get class name & method from props/manifest
            initialClass = Thread.currentThread().getContextClassLoader().loadClass("kg.apc.cmdtools.PluginsCMD");// $NON-NLS-1$
            Object instance = initialClass.newInstance();
            Method startup = initialClass.getMethod("processParams", new Class[]{(new String[0]).getClass()});// $NON-NLS-1$
            Object res = startup.invoke(instance, new Object[]{args});
            int rc = ((Integer) res).intValue();
            if (rc != 0) {
                System.exit(rc);
            }
        } catch (Throwable e) {
            if (e.getCause() != null) {
                System.err.println("ERROR: " + e.getCause().toString());
                System.err.println("Problem's technical details go below:");
                System.err.println("Home directory was detected as: " + jarDirectory);
                throw e.getCause();
            } else {
                System.err.println("Home directory was detected as: " + jarDirectory);
                throw e;
            }
        }
    }
}
