package kg.apc.jmeter.perfmon.agent;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * The main Agent class which starts the socket server and listen to
 * incoming connections. It is a console application, so we will use
 * System.out to print messages.
 * @author Stephane Hoblingre
 */
public class ServerAgent implements Runnable {

    /**
     * The version of the Agent
     */
    private static String version = "1.3.2";
    /**
     * The default port
     */
    public static int DEFAULT_PORT = 4444;
    private int port = -1;
    /**
     * listening loop controller
     */
    private boolean listening = true;
    private static boolean autoStop = false;

    /**
     * Constructor
     * @param port the port to run the agent
     */
    public ServerAgent(int port) {
        this.port = port;
    }

    public static boolean isAutoStop() {
        return autoStop;
    }

    /**
     * One simple method to log message
     * @param message
     */
    public static void logMessage(String message) {
        System.out.println(message);
    }

    /**
     * stop the service
     */
    public void stopService() {
        listening = false;
    }

    /**
     * For Unit tests only
     */
    public void startServiceAsThread() {
        Thread t = new Thread(this);
        t.start();
    }

    public void startServie() {
        listening = true;
        ServerSocket serverSocket = null;
        MetricsGetter.getInstance().getValues(MetricsGetter.CPU);

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logMessage("Could not listen on port: " + port + ". Please specify another port...");
            System.exit(-1);
        }

        logMessage("Waiting for incoming connections...");

        // For now, to stop the agent we must end the pocess (ctrl+c, kill, etc.)
        while (listening) {
            try {
                new ConnectionThread(serverSocket.accept()).start();
            } catch (IOException e) {
                logMessage("Impossible to create the connection with the client. Error is:");
                logMessage(e.getMessage());
            }
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            // do nothing...
        }
    }

    /**
     * The main method to start the agent
     * @param args [optional] the port on which the agent will start. 4444 is used if nothing is specified.
     */
    public static void main(String[] args) {
        ServerAgent.logMessage("JMeterPlugins Agent version " + version);

        int port = ServerAgent.DEFAULT_PORT;
        boolean isPortSpecified = false;

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--autostop")) {
                    logMessage("The agent will automatically stop at end of the test.");
                    autoStop = true;
                }
                if (!args[i].startsWith("--")) {
                    try {
                        isPortSpecified = true;
                        port = Integer.valueOf(args[i]).intValue();
                        ServerAgent.logMessage("The Agent will use port: " + port);
                    } catch (Exception e) {
                        ServerAgent.logMessage("No valid port specified (" + args[i] + "), the default value is used: " + port);
                    }
                }
            }
        }
        if(!isPortSpecified) logMessage("No port specified, the default value is used: " + port);

        ServerAgent agent = new ServerAgent(port);
        agent.startServie();

    }
    //Need to remove annotation for Java 1.4 compilation
    //@Override

    public void run() {
        startServie();
    }
}
