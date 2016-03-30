package kg.apc.jmeter.jmxmon;

import java.io.IOException;
import java.util.Map;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.remote.JMXConnector;
import javax.security.auth.Subject;

public class JMXConnectorEmul implements JMXConnector {

	@Override
	public void connect() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void connect(Map<String, ?> env) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public MBeanServerConnection getMBeanServerConnection() throws IOException {
		return new MBeanServerConnectionEmul(null);
	}

	@Override
	public MBeanServerConnection getMBeanServerConnection(Subject delegationSubject) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addConnectionNotificationListener(NotificationListener listener, NotificationFilter filter,
			Object handback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeConnectionNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeConnectionNotificationListener(NotificationListener l, NotificationFilter f, Object handback)
			throws ListenerNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getConnectionId() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
