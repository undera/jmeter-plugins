/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.perfmon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import kg.apc.jmeter.perfmon.agent.MetricsGetter;

/**
 * This class is used to connect to the remote server Agent and get the metrics
 * @author Stephane Hoblingre
 */
public class AgentConnector
{

    private String host;
    private int port;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String remoteServerName = null;

    /**
     * The constructor.
     * @param host the hostname or ip address of the server where the agent is running.
     * @param port the port to connect to the agent. Agent default port is 4444.
     */
    public AgentConnector(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    /**
     * Connect to the Agent and create an outputStream to send orders and
     * an inputStream to receive the response.
     * @throws UnknownHostException id the server cannot be located.
     * @throws IOException if the communication channel cannot be created.
     */
    public void connect() throws UnknownHostException, IOException
    {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        remoteServerName = getData("name");
    }

    /**
     * Disconnect from the Agent. We send "bye" so the Agent knows he can close the socket on his side.
     */
    public void disconnect()
    {
        try
        {   out.println("bye");
            out.close();
            in.close();
            socket.close();
        } catch (IOException e)
        {
            //do nothing
        }
    }

    /**
     * Generic method to query the Agent.
     * @param data the element to retrieve, eg "mem" or "cpu".
     * @return a String containing the numbered value
     * @throws IOException if a communication problem occurred
     */
    private String getData(String data) throws IOException
    {
        out.println(data);
        return in.readLine();
    }

    /**
     * Get the current total memory used on the server
     * @return the total memory in bytes or -2 if any error occurred
     */
    public long getMem() {
        long ret;

        try
        {
            String value = getData("mem");
            if(value != null) {
                ret = Long.parseLong(value);
            } else {
                ret = MetricsGetter.AGENT_ERROR;
            }
        } catch (IOException ioex)
        {
            ret = MetricsGetter.AGENT_ERROR;
        }

        return ret;
    }

    /**
     * Get the current cpu load on the server
     * @return the current cpu load % on the server or -1 if a problem occurred.
     */
    public double getCpu() {
        double ret;

        try
        {
            String value = getData("cpu");
            if(value != null) {
                ret = Double.parseDouble(value);
            } else {
                ret = MetricsGetter.AGENT_ERROR;
            }
        } catch (IOException ioex)
        {
            ret = MetricsGetter.AGENT_ERROR;
        }

        return ret;
    }

    public long[] getSwap() {
        long[] ret = new long[2];
        try
        {
            String value = getData("swp");
            if(value != null) {
                ret[0] = Long.parseLong(value.substring(0, value.indexOf(':')));
                ret[1] = Long.parseLong(value.substring(value.indexOf(':')+1));
            } else {
                ret = MetricsGetter.AGENT_ERROR_ARRAY;
            }
        } catch (IOException ioex)
        {
            ret = MetricsGetter.AGENT_ERROR_ARRAY;
        }

        return ret;
    }

     public long[] getDisksIO() {
        long[] ret = new long[2];
        try
        {
            String value = getData("dio");
            if(value != null) {
                ret[0] = Long.parseLong(value.substring(0, value.indexOf(':')));
                ret[1] = Long.parseLong(value.substring(value.indexOf(':')+1));
            } else {
                ret = MetricsGetter.AGENT_ERROR_ARRAY;
            }
        } catch (IOException ioex)
        {
            ret = MetricsGetter.AGENT_ERROR_ARRAY;
        }

        return ret;
    }

     public long[] getNetIO() {
        long[] ret = new long[2];
        try
        {
            String value = getData("nio");
            if(value != null) {
                ret[0] = Long.parseLong(value.substring(0, value.indexOf(':')));
                ret[1] = Long.parseLong(value.substring(value.indexOf(':')+1));
            } else {
                ret = MetricsGetter.AGENT_ERROR_ARRAY;
            }
        } catch (IOException ioex)
        {
            ret = MetricsGetter.AGENT_ERROR_ARRAY;
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
}
