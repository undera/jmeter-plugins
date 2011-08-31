package kg.apc.jmeter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.WritableByteChannel;
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

    private void processCommand(String command) throws IOException {
        log.debug("Got command line: " + command);

        String cmdType = command.trim();
        String params = "";
        if (command.indexOf(":") >= 0) {
            cmdType = command.substring(0, command.indexOf(":")).trim();
            params = command.substring(command.indexOf(":") + 1).trim();
        }

        if (cmdType.equals("shutdown")) {
            controller.shutdownConnections();
        } else if (cmdType.equals("metrics")) {
            setUpMetrics(params);
        } else if (cmdType.equals("exit")) {
            channel.close();
        } else if (cmdType.equals("go")) {
            controller.registerWritingChannel(channel, this);
        } else if (cmdType.equals("test")) {
            log.debug("Yep, we received the 'test'");
        } else if (cmdType.equals("")) {
        } else {
            throw new UnsupportedOperationException("Unknown command [" + cmdType.length() + "]: '" + cmdType + "'");
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

    public void sendMetrics() throws IOException {
        log.debug("Building metrics");
        String res = "some\tmetrics\n";
        ((WritableByteChannel) channel).write(ByteBuffer.wrap(res.getBytes()));
    }

    private void setUpMetrics(String params) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
