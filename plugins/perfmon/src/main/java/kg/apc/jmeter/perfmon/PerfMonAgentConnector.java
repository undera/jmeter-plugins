package kg.apc.jmeter.perfmon;

import java.io.IOException;

public interface PerfMonAgentConnector {

    void connect() throws IOException;

    void disconnect();

    void generateSamples(PerfMonSampleGenerator collector) throws IOException;

    void addMetric(String metric, String params, String label);
}
