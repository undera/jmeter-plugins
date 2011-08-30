package kg.apc.jmeter;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
class PerfMonMetricGetter {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public void processCommand(String toString) {
        log.debug("Got command line: " + toString);

        String cmd = toString.substring(toString.indexOf(":") >= 0 ? toString.indexOf(":") : 0);
        if (cmd.equals("test")) {
            log.debug("Yep, we received the 'test'");
        } else {
            throw new UnsupportedOperationException("Unknown command: " + cmd);
        }
    }
}
