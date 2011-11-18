package kg.apc.jmeter.perfmon;

import kg.apc.perfmon.client.AbstractTransport;
import java.io.IOException;
import java.net.InetSocketAddress;
import kg.apc.perfmon.PerfMonMetricGetter;
import kg.apc.perfmon.client.TransportFactory;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class NewAgentConnector implements PerfMonAgentConnector {

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected AbstractTransport transport;
    private String metricsStr;
    private String[] metricLabels;

    public NewAgentConnector(String host, int port, TransportFactory factory) throws IOException {
        transport = factory.getTransport(new InetSocketAddress(host, port));
    }

    public void setMetricType(String metric) {
        metricsStr = metric.toLowerCase();
        metricLabels=metric.split("\t");
    }

    public void setParams(String params) {
        metricsStr += PerfMonMetricGetter.DVOETOCHIE + params;
    }

    public void connect() throws IOException {
        String[] m = {metricsStr};
        transport.startWithMetrics(m);
    }

    public void disconnect() {
        transport.disconnect();
    }

    public void generateSamples(PerfMonSampleGenerator collector) throws IOException {
        String[] data = transport.readMetrics();
        try {
            collector.generateSample(Double.parseDouble(data[0]), metricLabels[0]);
        } catch (NumberFormatException e) {
            collector.generateErrorSample(metricLabels[0], e.getMessage());
        }
    }
}
