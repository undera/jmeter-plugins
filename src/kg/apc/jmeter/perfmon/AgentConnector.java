package kg.apc.jmeter.perfmon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import kg.apc.jmeter.perfmon.agent.AgentCommandsInterface;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class is used to connect to the remote server Agent and get the metrics
 * @author Stephane Hoblingre
 * @deprecated in favor of new agent
 */
public class AgentConnector implements AgentCommandsInterface {

    public static final List<String> metrics = Arrays.asList(new String[]{"CPU", "Memory", "Swap", "Disks I/O", "Network I/O"});
    //must be sync with the metrics array indexes
    public final static int PERFMON_CPU = 0;
    public final static int PERFMON_MEM = 1;
    public final static int PERFMON_SWAP = 2;
    public final static int PERFMON_DISKS_IO = 3;
    public final static int PERFMON_NETWORKS_IO = 4;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private String host;
    private int port;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String remoteServerName = null;
    private int metricType;

    /**
     * The constructor.
     * @param host the hostname or ip address of the server where the agent is running.
     * @param port the port to connect to the agent. Agent default port is 4444.
     */
    public AgentConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connect to the Agent and create an outputStream to send orders and
     * an inputStream to receive the response.
     * @throws UnknownHostException id the server cannot be located.
     * @throws IOException if the communication channel cannot be created.
     */
    public void connect(Socket aSocket) throws UnknownHostException, IOException, PerfMonException {
        socket = aSocket;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        remoteServerName = getData(NAME);
    }

    /**
     * Disconnect from the Agent. We send "bye" so the Agent knows he can close the socket on his side.
     */
    public void disconnect() {
        try {
            //this is a command sent to the server agent, must not be changed
            //it is used to exit the thread loop

            //we need to test if out is null, in case of connection broke during test
            //or agent was not started
            if (out != null) {
                out.println(BYE);
                out.close();
                in.close();
                socket.close();
            }
        } catch (IOException e) {
            log.error("Exception disconnecting agent:", e);
        }
    }

    /**
     * Generic method to query the Agent.
     * @param data the element to retrieve, eg "mem" or "cpu".
     * @return a String containing the numbered value
     * @throws IOException if a communication problem occurred
     */
    private String getData(String data) throws PerfMonException {
        if (out == null) {
            throw new PerfMonException("Not yet connected");
        }

        out.println(data);
        String ret = null;
        try {
            ret = in.readLine();
        } catch (IOException ex) {
            log.error("Error receiving data", ex);
            throw new PerfMonException("Connection lost with '" + host + "'!", ex);
        }
        log.debug("Read " + data + "=" + ret);
        return ret;
    }

    private void throwNotSupportedMetricException(String metric) throws PerfMonException {
        throw new PerfMonException("Getting " + metric + " metrics is not supported by Sigar API on this operating system...");
    }

    /**
     * Get the current total memory used on the server
     * @return the total memory in bytes or -2 if any error occurred
     */
    public long getMem() throws PerfMonException {
        long ret = -1;

        String value = getData(MEMORY);
        if (value != null) {
            ret = Long.parseLong(value);
        }
        if (ret <= 0) {
            throwNotSupportedMetricException("memory");
        }

        return ret;
    }

    /**
     * Get the current cpu load on the server
     * @return the current cpu load % on the server or -1 if a problem occurred.
     */
    public double getCpu() throws PerfMonException {
        double ret = -1;

        String value = getData(CPU);
        if (value != null) {
            ret = Double.parseDouble(value);
        }
        if (ret < 0) {
            throwNotSupportedMetricException("cpu");
        }

        return ret;
    }

    public long[] getSwap() throws PerfMonException {
        long[] ret = {-1L, -1L};
        String value = getData(SWAP);
        if (value != null) {
            ret[0] = Long.parseLong(value.substring(0, value.indexOf(':')));
            ret[1] = Long.parseLong(value.substring(value.indexOf(':') + 1));
        }
        if (ret[0] < 0 || ret[1] < 0) {
            throwNotSupportedMetricException("swap");
        }

        return ret;
    }

    public long[] getDisksIO() throws PerfMonException {
        long[] ret = {-1L, -1L};
        String value = getData(DISKIO);
        if (value != null) {
            ret[0] = Long.parseLong(value.substring(0, value.indexOf(':')));
            ret[1] = Long.parseLong(value.substring(value.indexOf(':') + 1));
        }
        if (ret[0] < 0 || ret[1] < 0) {
            throwNotSupportedMetricException("disks I/O");
        }
        return ret;
    }

    public long[] getNetIO() throws PerfMonException {
        long[] ret = {-1L, -1L};
        String value = getData(NETWORK);
        if (value != null) {
            ret[0] = Long.parseLong(value.substring(0, value.indexOf(':')));
            ret[1] = Long.parseLong(value.substring(value.indexOf(':') + 1));
        }
        if (ret[0] < 0 || ret[1] < 0) {
            throwNotSupportedMetricException("network I/O");
        }

        return ret;
    }

    /**
     * Get the remote server name.
     * @return the name of the remote server.
     */
    public String getRemoteServerName() {
        return remoteServerName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setMetricType(String metric) {
        metricType = metrics.indexOf(metric);
    }

    public int getMetricType() {
        return metricType;
    }
}
