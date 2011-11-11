package kg.apc.jmeter.perfmon;

import kg.apc.jmeter.perfmon.client.UDPTransport;
import kg.apc.jmeter.perfmon.client.TCPTransport;
import kg.apc.jmeter.perfmon.client.AbstractTransport;
import java.io.IOException;

/**
 *
 * @author undera
 */
public class NewAgentConnector implements PerfMonAgentConnector {

    public static final int PROTO_TCP = 0;
    public static final int PROTO_UDP = 1;
    protected AbstractTransport transport;
    private String metricsStr;

    public NewAgentConnector(int protocol, String host, int port) {
        if (protocol == PROTO_UDP) {
            transport = new UDPTransport(host, port);
        } else {
            transport = new TCPTransport(host, port);
        }
    }

    public void setMetricType(String metric) {
        metricsStr = metric;
    }

    public boolean test() {
        transport.writeln("test");
        String testResult = transport.readln();
        return testResult.startsWith("Yep");
    }

    public void setParams(String params) {
        metricsStr += ":" + params;
    }

    public void connect() throws IOException {
        transport.writeln(metricsStr);
    }

    public void disconnect() {
        transport.disconnect();
    }

    public void generateSamples(PerfMonSampleGenerator collector) throws IOException {
        String data = transport.readln();
        collector.generateSample(PROTO_TCP, metricsStr);
    }
}
