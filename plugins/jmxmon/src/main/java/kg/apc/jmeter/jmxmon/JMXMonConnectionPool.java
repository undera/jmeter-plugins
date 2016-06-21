package kg.apc.jmeter.jmxmon;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class store jmx connections
 * It is not a complete pool, it will not manage lease on connection
 * It only provide the same connection for the same jmx url
 *
 */
public class JMXMonConnectionPool {
	
	/**
	 * the logger
	 */
	private static final Logger log = LoggingManager.getLoggerForClass();

	/**
	 * Store {@link JMXMonConnection}
	 */
	private Map<String, JMXMonConnection> pool;
	
	/**
	 * The constructor
	 */
	public JMXMonConnectionPool() {
		super();
		pool = new HashMap<String, JMXMonConnection>();
	}
	
	/**
	 * Try to get a connection to the specified jmx url
	 * @param jmxUrl the jmx url
	 * @param attributes jmx connection attributes
	 * @return a jmx connection
	 */
	public MBeanServerConnection getConnection(String jmxUrl, Hashtable attributes)
	{
		return getConnection(jmxUrl, attributes, false);
	}
	
	/**
	 * Try to get a connection to the specified jmx url
	 * @param jmxUrl the jmx url
	 * @param attributes jmx connection attributes
	 * @param wait if true wait the current thread until the end of the connection attempt
	 * @return a jmx connection
	 */
	public MBeanServerConnection getConnection(String jmxUrl, Hashtable attributes, boolean wait)
	{
		JMXMonConnection connection = (JMXMonConnection)pool.get(jmxUrl);
		if (connection == null) {
			connection = new JMXMonConnection(jmxUrl);
			pool.put(jmxUrl, connection);
		}
		return connection.connect(attributes, wait);

	}
	

	/**
	 * Close all active connections by closing linked {@link JMXConnector}
	 */
	public void closeAll() {
		for (Object connection : pool.values()) {
			JMXMonConnection jmxcon = (JMXMonConnection)connection;
            if (jmxcon.connector != null) {
                try {
                	jmxcon.connector.close();
                    log.debug("jmx connector is closed");
                } catch (Exception ex) {
                    log.debug("Can't close jmx connector, but continue");
                }
            } else {
                log.debug("jmxConnector == null, don't try to close connection");
            }
            
            jmxcon.connector = null;
            jmxcon = null;
            
        }
		/* erase pool informations for this run but will be recreate for the next run */
		pool.clear();
	}

	/**
	 * This class store jmx connection data
	 * and ensure that only one connection attempt
	 * is made simultaneously
	 *
	 */
	private class JMXMonConnection {
		
		/**
		 * jmx url
		 */
		private String jmxUrl;
		
		/**
		 * jmx connector or null if not connected
		 */
		private JMXConnector connector;
		/**
		 * jmx connection or null if not connected
		 */
		private MBeanServerConnection connection;
		/**
		 * thread used to handle connection attempt
		 */
		private Thread connectionAttemptThread;
		/**
		 * semaphore : true if a connection attempt is running
		 */
		private boolean connectionAttemptFlag;
		
		/**Construtor
		 * 
		 * @param jmxUrl jmx url
		 */
		private JMXMonConnection(String jmxUrl){
			this.jmxUrl = jmxUrl;
		}
		
		
		/**
		 * Start a connection thread if none are running
		 * @param attributes jmx connection attributes
		 * @param wait if true wait the current thread until the end of the connection attempt
		 */
		protected synchronized void tryConnect(final Hashtable attributes, boolean wait)
		{
			connectionAttemptFlag = true;
			
			connectionAttemptThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						JMXServiceURL u = new JMXServiceURL(jmxUrl);
						log.debug("Create new connection url = " + jmxUrl);
			            connector = JMXConnectorFactory.connect(u, attributes);
			            connection = connector.getMBeanServerConnection();
			            
		            } catch (MalformedURLException ex) {
		                //throw new RuntimeException(ex);
		                log.error("Malformed JMX url", ex);
		            } catch (IOException ex) {
		                log.error("IOException reading JMX", ex);
		            }
					finally {
						connectionAttemptFlag = false;
					}
				}
			});
			
			connectionAttemptThread.start();
			
			if (wait)
				try {
					connectionAttemptThread.join();
				} catch (InterruptedException e) {
					log.warn("Connection thread has been interrupted", e);
				}
			
		}
		
		/**
		 * Check if a connection already exists if true it will return the
		 * existing connection else it will attempt a new connection
		 * @param attributes jmx connection attributes
		 * @param wait if true wait the current thread until the end of the connection attempt
		 * @return a jmx connection or null
		 */
		protected MBeanServerConnection connect(Hashtable attributes, boolean wait) {

			if (connection != null){
				log.debug("Reused the same connection for url = " + jmxUrl);
				return connection;
			}
			
			if (!connectionAttemptFlag)
				tryConnect(attributes, wait);
			
			return connection;
		}
        
		/**
		 * Store a new jmx connection
		 * @param connector the jmx connector
		 * @param connection the jmx connection
		 */
		protected void setNewActiveConnection(JMXConnector connector, MBeanServerConnection connection)
		{
			this.connector = connector;
			this.connection = connection;
		}

	}

	/**
	 * Allow external class to notify the pool that the connection identified by the provided
	 * jmx url is closed/in error 
	 * @param url the jmx url
	 */
	public void notifyConnectionDirty(String url) {
		JMXMonConnection connection = (JMXMonConnection)pool.get(url);
		
		if (connection != null){
			connection.setNewActiveConnection(null, null);
		}
	}

}
