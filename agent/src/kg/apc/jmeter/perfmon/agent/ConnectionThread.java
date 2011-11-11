package kg.apc.jmeter.perfmon.agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class will handle one connection with the remote JMeter plugin AgentConnector
 * @author Stephane Hoblingre
 */
public class ConnectionThread
        extends Thread {

    /**
     * The socket to use with the AgentConnector
     */
    private Socket socket = null;
    /**
     * The last allocated id, only used for logging purpose
     */
    private static int currentId = 0;
    /**
     * The Connection ID, only used for logging purpose
     */
    private int id;
    /**
     * Count the number of active connections for auto stop feature
     */
    private static int nbOfCnx = 0;

    /**
     * The constructor.
     * @param socket The socket used for communication with the AgentConnector
     */
    public ConnectionThread(Socket socket) {
        super("ConnectionThread");
        this.socket = socket;
        id = currentId++;
    }

    /**
     * The main loop which will wait for AgentConnector orders
     */
    public void run() {
        nbOfCnx++;
        MetricsGetter data = MetricsGetter.getInstance();
        ServerAgent.logMessage("Client id=" + id + " connected!");
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals(MetricsGetter.BYE)) {
                    break;
                } else {
                    out.println(data.getValues(inputLine));
                }
            }

            out.close();
            in.close();
            socket.close();

            ServerAgent.logMessage("Client id=" + id + " disconnected!");

        } catch (IOException e) {
            ServerAgent.logMessage(e.getMessage());
        } finally {
            nbOfCnx--;
        }

        if (ServerAgent.isAutoStop() && nbOfCnx == 0) {
            ServerAgent.logMessage("No more connections, the Agent will stop now.");
            //kill the process
            exit(0);
        }
    }

    protected void exit(int i) {
        System.exit(i);
    }
}
