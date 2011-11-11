package kg.apc.jmeter.perfmon;

import java.io.IOException;

/**
 *
 * @author undera
 */
public interface PerfMonAgentConnector {

    public void setMetricType(String metric);

    public void setParams(String params);

    public void connect() throws IOException;

    public void disconnect();

    public String getLabel(boolean translateHost);

    public void generateSamples(PerfMonSampleGenerator collector) throws IOException;
}
