package kg.apc.jmeter.jmxmon.rmi;

import java.io.IOException;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;

import kg.apc.jmeter.jmxmon.JMXConnectorEmul;

public class ClientProvider implements JMXConnectorProvider {

	@Override
	public JMXConnector newJMXConnector(JMXServiceURL serviceURL, Map<String, ?> environment) throws IOException {
		return new JMXConnectorEmul();
	}

}
