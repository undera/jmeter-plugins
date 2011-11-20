
package kg.apc.perfmon.client;

import java.io.IOException;

/**
 *
 * @author undera
 */
public interface Transport {

    void disconnect();

    String[] readMetrics();

    String readln();

    void setInterval(long interval);

    void shutdownAgent();

    void startWithMetrics(String[] metricsArray) throws IOException;

    boolean test();

    void writeln(String line) throws IOException;
    
}
