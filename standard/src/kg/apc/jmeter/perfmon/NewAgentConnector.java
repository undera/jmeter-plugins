package kg.apc.jmeter.perfmon;

import kg.apc.perfmon.PerfMonMetricGetter;
import kg.apc.perfmon.client.Transport;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewAgentConnector implements PerfMonAgentConnector {

    private static final Logger log = LoggingManager.getLoggerForClass();
    protected Transport transport;
    private Map<String, String> metrics = new HashMap<>();
    private String[] metricLabels;

    public void setTransport(Transport atransport) {
        transport = atransport;
    }

    public void connect() throws IOException {
        log.debug(metrics.toString());

        ArrayList<String> labels = new ArrayList<>(metrics.keySet());
        metricLabels = labels.toArray(new String[labels.size()]);

        ArrayList<String> arr = new ArrayList<>(metrics.values());
        String[] m = arr.toArray(new String[arr.size()]);
        transport.startWithMetrics(m);
    }

    public void disconnect() {
        transport.disconnect();
    }

    public void generateSamples(PerfMonSampleGenerator collector) throws IOException {
        String[] data = transport.readMetrics();
        for (int n = 0; n < data.length; n++) {
            if (!data[n].isEmpty()) {
                try {
                    collector.generateSample(Double.parseDouble(data[n]), metricLabels[n]);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    collector.generateErrorSample(metricLabels[n], e.toString());
                }
            }
        }
    }

    public void addMetric(String metric, String params, String label) {
        metrics.put(label, metric.toLowerCase() + PerfMonMetricGetter.DVOETOCHIE + params);
    }
}
