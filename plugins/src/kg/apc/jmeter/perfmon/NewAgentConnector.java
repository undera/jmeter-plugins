package kg.apc.jmeter.perfmon;

import java.io.IOException;
import kg.apc.perfmon.PerfMonMetricGetter;
import kg.apc.perfmon.client.Transport;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class NewAgentConnector implements PerfMonAgentConnector {

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected Transport transport;
    private String metricsStr;
    private String[] metricLabels;

    public void setTransport(Transport atransport) {
        transport = atransport;
    }

    public void setMetricType(String metric) {
        metricsStr = metric.toLowerCase();
        metricLabels = metric.split(PerfMonMetricGetter.TAB);
    }

    public void setParams(String params) {
        metricsStr += PerfMonMetricGetter.DVOETOCHIE + params;
    }

    public void connect() throws IOException {
        log.info(metricsStr);
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
        } catch (ArrayIndexOutOfBoundsException e) {
            collector.generateErrorSample(metricLabels[0], e.getMessage());
        }
    }
}
