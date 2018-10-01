package kg.apc.logging;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.lang.reflect.Constructor;

public class LoggingUtils {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public static void addLoggingConfig() {
        if (isJMeter32orLater()) {
            configureCMDLogging();
        }
    }

    private static void configureCMDLogging() {
        try {
            Class cls = Class.forName("kg.apc.logging.LoggingConfigurator");
            Constructor constructor = cls.getConstructor();
            constructor.newInstance();
        } catch (Throwable ex) {
            System.out.println("Fail to configure logging " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    public static boolean isJMeter32orLater() {
        try {
            Class<?> cls = LoggingUtils.class.getClassLoader().loadClass("org.apache.jmeter.gui.logging.GuiLogEventBus");
            if (cls != null) {
                return true;
            }
        } catch (ClassNotFoundException ex) {
            log.debug("Class 'org.apache.jmeter.gui.logging.GuiLogEventBus' not found", ex);
        } catch (Throwable ex) {
            log.warn("Fail to detect JMeter version", ex);
        }
        return false;
    }
}
