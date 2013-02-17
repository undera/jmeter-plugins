package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 * @deprecated in favor of new agent
 */
public class OldAgentConnector implements PerfMonAgentConnector {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static boolean isTranslate = false;
    AgentConnector connector;
    private boolean metricWasSet = false;

    static {
        String cfgTranslateHostName = JMeterUtils.getProperty("jmeterPlugin.perfmon.translateHostName");
        if (cfgTranslateHostName != null) {
            isTranslate = "true".equalsIgnoreCase(cfgTranslateHostName.trim());
        }
    }

    public OldAgentConnector(String host, int port) {
        connector = new AgentConnector(host, port);
    }

    public void connect() throws IOException {
        Socket sock = createSocket(connector.getHost(), connector.getPort());
        connector.connect(sock);
    }

    public void disconnect() {
        connector.disconnect();
    }

    protected Socket createSocket(String host, int port) throws UnknownHostException, IOException {
        return new Socket(host, port);
    }

    // TODO: cache it to be efficient
    private String getLabel(boolean translate) {
        String hostName;
        if (translate) {
            hostName = connector.getRemoteServerName();
        } else {
            hostName = connector.getHost();
        }
        return hostName + " - " + AgentConnector.metrics.get(connector.getMetricType());
    }

    public void generateSamples(PerfMonSampleGenerator collector) throws IOException {
        String label = getLabel(isTranslate);
        switch (connector.getMetricType()) {
            case AgentConnector.PERFMON_CPU:
                collector.generateSample(100 * connector.getCpu(), label + ", %");
                break;
            case AgentConnector.PERFMON_MEM:
                collector.generateSample((double) connector.getMem() / PerfMonCollector.MEGABYTE, label + ", MB");
                break;
            case AgentConnector.PERFMON_SWAP:
                collector.generate2Samples(connector.getSwap(), label + " page in", label + " page out");
                break;
            case AgentConnector.PERFMON_DISKS_IO:
                collector.generate2Samples(connector.getDisksIO(), label + " reads", label + " writes");
                break;
            case AgentConnector.PERFMON_NETWORKS_IO:
                collector.generate2Samples(connector.getNetIO(), label + " recv, KB", label + " sent, KB", 1024d);
                break;
            default:
                throw new IOException("Unknown metric index: " + connector.getMetricType());
        }
    }

    // TODO: label currently ignored - maybe use it instead of calculated?
    public void addMetric(String metric, String params, String label) {
        if (metricWasSet) {
            throw new RuntimeException("Old connector don't support multiple metrics");
        }
        metricWasSet = true;
        connector.setMetricType(metric);
    }
}
