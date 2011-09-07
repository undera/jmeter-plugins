package kg.apc.perfmon;

import kg.apc.perfmon.PerfMonWorker;
import kg.apc.perfmon.InvalidPerfMonMetric;
import kg.apc.perfmon.AbstractCPUMetric;
import kg.apc.perfmon.AbstractPerfMonMetric;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

/**
 *
 * @author undera
 */
public class PerfMonMetricGetter {

    public static final String TAB = "\t";
    private static final String DVOETOCHIE = ":";
    private static final String NEWLINE = "\n";
    private final PerfMonWorker controller;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private String commandString = "";
    private final SelectableChannel channel;
    private AbstractPerfMonMetric[] metrics = new AbstractPerfMonMetric[0];
    private final SigarProxy sigarProxy;// TODO: move up to share between all getters
    private boolean started = false;

    public PerfMonMetricGetter(PerfMonWorker aController, SelectableChannel aChannel) {
        controller = aController;
        channel = aChannel;
        sigarProxy = SigarProxyCache.newInstance(new Sigar(), 500);
    }

    private void processCommand(String command) throws IOException {
        log.debug("Got command line: " + command);

        String cmdType = command.trim();
        String params = "";
        if (command.indexOf(DVOETOCHIE) >= 0) {
            cmdType = command.substring(0, command.indexOf(DVOETOCHIE)).trim();
            params = command.substring(command.indexOf(DVOETOCHIE) + 1).trim();
        }

        if (cmdType.equals("shutdown")) {
            controller.shutdownConnections();
        } else if (cmdType.equals("metrics")) {
            setUpMetrics(params.split(TAB));
            controller.registerWritingChannel(channel, this);
        } else if (cmdType.equals("exit")) {
            metrics = new AbstractPerfMonMetric[0];
            if (channel instanceof SocketChannel) {
                channel.close();
            }
        } else if (cmdType.equals("test")) {
            log.info("Yep, we received the 'test'");
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
        if (commandString.indexOf(NEWLINE) >= 0) {
            int pos = commandString.indexOf(NEWLINE);
            String cmd = commandString.substring(0, pos);
            commandString = commandString.substring(pos + 1);
            processCommand(cmd);
            return true;
        } else {
            return false;
        }
    }

    public ByteBuffer getMetricsLine() throws IOException {
        log.debug("Building metrics");
        StringBuilder res = new StringBuilder();
        for (int n = 0; n < metrics.length; n++) {
            try {
                metrics[n].getValue(res);
            } catch (SigarException ex) {
                log.error("Error getting metric", ex);
            }
            res.append(TAB);
        }
        res.append(NEWLINE);

        return (ByteBuffer.wrap(res.toString().getBytes()));
    }

    private void setUpMetrics(String[] params) {
        metrics = new AbstractPerfMonMetric[params.length];
        String metricParams = "";
        for (int n = 0; n < params.length; n++) {
            String metricType = params[n];
            if (metricType.indexOf(DVOETOCHIE) >= 0) {
                metricParams = metricType.substring(metricType.indexOf(DVOETOCHIE) + 1).trim();
                metricType = metricType.substring(0, metricType.indexOf(DVOETOCHIE)).trim();
            }

            AbstractPerfMonMetric metric;
            try {
                if (metricType.equals("cpu")) {
                    metric = AbstractCPUMetric.getMetric(sigarProxy, metricParams);
                } else {
                    throw new SigarException("No SIGAR object for metric type " + metricType);
                }
            } catch (SigarException ex) {
                log.error("Invalid metric specified: " + params[n], ex);
                metric = new InvalidPerfMonMetric();
            }

            metrics[n] = metric;
        }
    }

    public boolean isStarted() {
        return metrics.length > 0;
    }
}
