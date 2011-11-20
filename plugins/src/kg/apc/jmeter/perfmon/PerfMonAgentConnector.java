package kg.apc.jmeter.perfmon;

import java.io.IOException;

/**
 *
 * @author undera
 */
public interface PerfMonAgentConnector {

    public void connect() throws IOException;

    public void disconnect();

    public void generateSamples(PerfMonSampleGenerator collector) throws IOException;

    public void addMetric(String metric, String params, String label);
}
