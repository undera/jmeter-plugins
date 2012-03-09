package kg.apc.perfmon.metrics.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class JMXConnectorHelper {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public MBeanServerConnection getServerConnection(String url, String user, String pwd) {
        try {
            JMXConnector connector = getJMXConnector(url, user, pwd);
            return connector.getMBeanServerConnection();
        } catch (Exception ex) {
            log.error("Failed to get JMX Connector", ex);
            throw new RuntimeException("Failed to get JMX Connector", ex);
        }
    }

    private JMXConnector getJMXConnector(String url, String usr, String pwd)
            throws MalformedURLException, IOException {
        String serviceUrl = "service:jmx:rmi:///jndi/rmi://" + url + "/jmxrmi";
        if (usr == null || usr.trim().length() <= 0 || pwd == null || pwd.trim().length() <= 0) {
            JMXServiceURL surl = new JMXServiceURL(serviceUrl);
            return JMXConnectorFactory.connect(surl);
        }
        Map envMap = new HashMap();
        envMap.put("jmx.remote.credentials", new String[]{usr, pwd});
        envMap.put(Context.SECURITY_PRINCIPAL, usr);
        envMap.put(Context.SECURITY_CREDENTIALS, pwd);
        return JMXConnectorFactory.connect(new JMXServiceURL(serviceUrl), envMap);
    }
}
