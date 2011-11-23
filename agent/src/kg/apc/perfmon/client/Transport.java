package kg.apc.perfmon.client;

import java.io.IOException;

/**
 * Interface delcares client capabilities. PerfMon clients must implement it.
 * @author undera
 * @see TransportFactory
 */
public interface Transport {

    /**
     * Disconnects client from server agent
     */
    public void disconnect();

    /**
     * Method to be used after startWithMetrics
     * Returns array containing collected metric values
     * @return 
     */
    public String[] readMetrics();

    /**
     * Basic primitive for reading server agent output
     * @return next line read from the agent
     */
    public String readln();

    /**
     * Sets metrics collection interval
     * @param interval 
     */
    public void setInterval(long interval);

    /**
     * Commands server agent to close all connections and shutdown the process
     */
    public void shutdownAgent();

    /**
     * Asks agent to start metrics transmission.
     * @param metricsArray String array containing metric description
     * @throws IOException 
     */
    public void startWithMetrics(String[] metricsArray) throws IOException;

    /**
     * Method to test if transport connection established with applicable server agent
     * @return 
     */
    public boolean test();

    /**
     * Basic agent communication primitive, send one command string
     * @param line command string to send
     * @throws IOException 
     */
    public void writeln(String line) throws IOException;

    /**
     * Returns address string if one was set by setAddressLabel
     * @return 
     */
    public String getAddressLabel();

    /**
     * Set connected address string to be used in labels, since SocketAddress 
     * does not provide any info.
     * @param label the label to set
     */
    public void setAddressLabel(String label);
}
