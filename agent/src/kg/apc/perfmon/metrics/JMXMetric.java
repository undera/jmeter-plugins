package kg.apc.perfmon.metrics;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
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
public class JMXMetric extends AbstractPerfMonMetric {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private final JMXConnector connector;

    public JMXMetric(MetricParams params) {
        super(null);
        String url = "";
        String user = "";
        String pwd = "";

        for (int i = 0; i < params.params.length; i++) {
            if (params.params[i].startsWith("url=")) {
                url = MetricParams.getParamValue(params.params[i]);
            } else if (params.params[i].startsWith("user=")) {
                user = MetricParams.getParamValue(params.params[i]);
            } else if (params.params[i].startsWith("password=")) {
                pwd = MetricParams.getParamValue(params.params[i]);
            }
        }

        try {
            connector = getJMXConnector(url, user, pwd);
        } catch (Exception ex) {
            log.error("", ex);
            throw new RuntimeException("Failed to get JMX Connector", ex);
        }
    }

    public void getValue(StringBuffer res) throws Exception {
    }

    private static JMXConnector getJMXConnector(String url, String usr, String pwd)
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
