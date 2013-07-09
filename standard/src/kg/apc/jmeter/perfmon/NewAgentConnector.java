package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private Map<String, String> metrics = new HashMap<String, String>();
    private String[] metricLabels;

    public void setTransport(Transport atransport) {
        transport = atransport;
    }

    public void connect() throws IOException {
        log.debug(metrics.toString());

        ArrayList<String> labels = new ArrayList<String>(metrics.keySet());
        metricLabels = labels.toArray(new String[0]);

        ArrayList<String> arr = new ArrayList<String>(metrics.values());
        String[] m = arr.toArray(new String[0]);
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
                } catch (NumberFormatException e) {
                    collector.generateErrorSample(metricLabels[n], e.toString());
                } catch (ArrayIndexOutOfBoundsException e) {
                    collector.generateErrorSample(metricLabels[n], e.toString());
                }
            }
        }
    }

    public void addMetric(String metric, String params, String label) {
        metrics.put(label, metric.toLowerCase() + PerfMonMetricGetter.DVOETOCHIE + params);
    }
}
