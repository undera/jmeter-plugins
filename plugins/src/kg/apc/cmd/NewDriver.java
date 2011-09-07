package kg.apc.cmd;

// N.B. this must only use standard Java packages
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Main class for CLI - sets up initial classpath and the loader.
 * I took it from JMeter, yes
 */
public final class NewDriver {

    private static final String CLASSPATH_SEPARATOR = System.getProperty("path.separator");// $NON-NLS-1$
    private static final String OS_NAME = System.getProperty("os.name");// $NON-NLS-1$
    private static final String OS_NAME_LC = OS_NAME.toLowerCase(java.util.Locale.ENGLISH);
    private static final String JAVA_CLASS_PATH = "java.class.path";// $NON-NLS-1$
    /** The class loader to use for loading JMeter classes. */
    private static final DynamicClassLoader loader;
    /** The directory JMeter is installed in. */
    private static final String jmDir;

    static {
        //System.setProperty("user.dir", new File(System.getProperty("user.dir")).getParent());
        List<URL> jars = new LinkedList<URL>();
        final String initial_classpath = System.getProperty(JAVA_CLASS_PATH);

        // Find JMeter home dir from the initial classpath
        String tmpDir = null;
        StringTokenizer tok = new StringTokenizer(initial_classpath, File.pathSeparator);
        if (tok.countTokens() == 1
                || (tok.countTokens() == 2 // Java on Mac OS can add a second entry to the initial classpath
                && OS_NAME_LC.startsWith("mac os x")// $NON-NLS-1$
                )) {
            File jar = new File(tok.nextToken());
            try {
                tmpDir = jar.getCanonicalFile().getParentFile().getParent();
            } catch (IOException e) {
            }
        } else {// e.g. started from IDE with full classpath
            tmpDir = System.getProperty("jmeter.home", "");// Allow override $NON-NLS-1$ $NON-NLS-2$
            if (tmpDir.length() == 0) {
                File userDir = new File(System.getProperty("user.dir"));// $NON-NLS-1$
                tmpDir = userDir.getAbsoluteFile().getParent();
            }
        }
        //jmDir=tmpDir;

        jmDir = new File(tmpDir).getParent();

        /*
         * Does the system support UNC paths? If so, may need to fix them up
         * later
         */
        boolean usesUNC = OS_NAME_LC.startsWith("windows");// $NON-NLS-1$

        // Add standard jar locations to initial classpath
        StringBuilder classpath = new StringBuilder();
        File[] libDirs = new File[]{new File(jmDir + File.separator + "lib"),// $NON-NLS-1$ $NON-NLS-2$
            new File(jmDir + File.separator + "lib" + File.separator + "ext"),// $NON-NLS-1$ $NON-NLS-2$
            new File(jmDir + File.separator + "lib" + File.separator + "junit")};// $NON-NLS-1$ $NON-NLS-2$
        for (int a = 0; a < libDirs.length; a++) {
            File[] libJars = libDirs[a].listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {// only accept jar files
                    return name.endsWith(".jar");// $NON-NLS-1$
                }
            });
            if (libJars == null) {
                new Throwable("Could not access " + libDirs[a]).printStackTrace(System.err);
                continue;
            }
            for (int i = 0; i < libJars.length; i++) {
                try {
                    String s = libJars[i].getPath();

                    // Fix path to allow the use of UNC URLs
                    if (usesUNC) {
                        if (s.startsWith("\\\\") && !s.startsWith("\\\\\\")) {// $NON-NLS-1$ $NON-NLS-2$
                            s = "\\\\" + s;// $NON-NLS-1$
                        } else if (s.startsWith("//") && !s.startsWith("///")) {// $NON-NLS-1$ $NON-NLS-2$
                            s = "//" + s;// $NON-NLS-1$
                        }
                    } // usesUNC

                    jars.add(new File(s).toURI().toURL());// See Java bug 4496398
                    classpath.append(CLASSPATH_SEPARATOR);
                    classpath.append(s);
                } catch (MalformedURLException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        // ClassFinder needs the classpath
        System.setProperty(JAVA_CLASS_PATH, initial_classpath + classpath.toString());
        loader = new DynamicClassLoader(jars.toArray(new URL[0]));
    }

    /**
     * Prevent instantiation.
     */
    private NewDriver() {
    }

    /**
     * Add a URL to the loader classpath only; does not update the system classpath.
     *
     * @param url
     */
    public static void addURL(URL url) {
        loader.addURL(url);
    }

    /**
     * Add a directory or jar to the loader and system classpaths.
     *
     * @param path to add to the loader and system classpath
     * @throws MalformedURLException
     */
    public static void addPath(String path) throws MalformedURLException {
        File file = new File(path);
        // Ensure that directory URLs end in "/"
        if (file.isDirectory() && !path.endsWith("/")) {// $NON-NLS-1$
            file = new File(path + "/");// $NON-NLS-1$
        }
        loader.addURL(file.toURI().toURL()); // See Java bug 4496398
        StringBuilder sb = new StringBuilder(System.getProperty(JAVA_CLASS_PATH));
        sb.append(CLASSPATH_SEPARATOR);
        sb.append(path);
        // ClassFinder needs this
        System.setProperty(JAVA_CLASS_PATH, sb.toString());
    }

    /**
     * Get the directory where JMeter is installed. This is the absolute path
     * name.
     *
     * @return the directory where JMeter is installed.
     */
    public static String getJMeterDir() {
        return jmDir;
    }

    /**
     * The main program which actually runs JMeter.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> initialClass;
            // make it independent - get class name & method from props/manifest
            initialClass = loader.loadClass("kg.apc.jmeter.PluginsCMD");// $NON-NLS-1$
            Object instance = initialClass.newInstance();
            Method startup = initialClass.getMethod("processParams", new Class[]{(new String[0]).getClass()});// $NON-NLS-1$
            Object res=startup.invoke(instance, new Object[]{args});
            int rc=(Integer) res;
            if (rc!=0) System.exit(rc);
        } catch (Throwable e) {
            System.err.println("JMeter home directory was detected as: " + jmDir);
            throw new RuntimeException(e);
        }
    }
}
