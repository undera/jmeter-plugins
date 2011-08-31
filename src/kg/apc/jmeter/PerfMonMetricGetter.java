package kg.apc.jmeter;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class PerfMonMetricGetter {

    private final PerfMonWorker controller;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private String commandString = "";
    private final SelectableChannel channel;

    public PerfMonMetricGetter(PerfMonWorker aController, SelectableChannel aChannel) {
        controller = aController;
        channel = aChannel;
    }

    private void processCommand(String toString) throws IOException {
        log.debug("Got command line: " + toString);

        String cmd = toString.trim();
        if (toString.indexOf(":") >= 0) {
            cmd = toString.substring(0, toString.indexOf(":")).trim();
        }

        if (cmd.equals("exit")) {
            controller.shutdownConnections();
        } else if (cmd.equals("stop")) {
            channel.close();
        } else if (cmd.equals("start")) {
            controller.registerWritingChannel(channel, this);
        } else if (cmd.equals("test")) {
            log.debug("Yep, we received the 'test'");
        } else if (cmd.equals("")) {
        } else {
            throw new UnsupportedOperationException("Unknown command [" + cmd.length() + "]: '" + cmd + "'");
        }
    }

    public void addCommandString(String byteBufferToString) {
        commandString += byteBufferToString;
    }

    public boolean processNextCommand() throws IOException {
        log.debug("Command line is: " + commandString);
        if (commandString.indexOf("\n") >= 0) {
            int pos = commandString.indexOf("\n");
            String cmd = commandString.substring(0, pos);
            commandString = commandString.substring(pos + 1);
            processCommand(cmd);
            return true;
        } else {
            return false;
        }
    }

    public void sendMetrics() {
        log.debug("Sending metrics!");
    }
}
