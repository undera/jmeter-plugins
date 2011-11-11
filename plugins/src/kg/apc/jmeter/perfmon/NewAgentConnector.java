package kg.apc.jmeter.perfmon;

import java.io.IOException;

/**
 *
 * @author undera
 */
public class NewAgentConnector implements PerfMonAgentConnector {

    public NewAgentConnector(String host, int port) {
        
    }

    public void setMetricType(String metric) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean test() {
        return false;
    }

    public void setParams(String params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void connect() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLabel(boolean translateHost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void generateSamples(PerfMonSampleGenerator collector) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
