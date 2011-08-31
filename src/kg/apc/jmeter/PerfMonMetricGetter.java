package kg.apc.jmeter;

import java.io.IOException;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
class PerfMonMetricGetter {

    private final PerfMonWorker controller;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private String commandString = "";

    public PerfMonMetricGetter(PerfMonWorker aController) {
        controller = aController;
    }

    private void processCommand(String toString) throws IOException {
        log.debug("Got command line: " + toString);

        String cmd = toString.trim();
        if (toString.indexOf(":") >= 0) {
            cmd = toString.substring(0, toString.indexOf(":")).trim();
        }

        if (cmd.equals("exit")) {
            controller.shutdownConnections();
        } else if (cmd.equals("test")) {
            log.debug("Yep, we received the 'test'");
        } else {
            throw new UnsupportedOperationException("Unknown command [" + cmd.length() + "]: '" + cmd + "'");
        }
    }

    public void addCommandString(String byteBufferToString) {
        commandString += byteBufferToString;
    }

    public void processNextCommand() throws IOException {
        log.debug("Command line is: " + commandString);
        if (commandString.indexOf("\n") >= 0) {
            int pos = commandString.indexOf("\n");
            String cmd = commandString.substring(0, pos);
            commandString = commandString.substring(pos + 1);
            processCommand(cmd);
        }
    }
}
