package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Factory class that manages SSH sessions for Apache Commons connection pool.
 * TODO known_hosts validation
 */
public class SSHSessionFactory extends BaseKeyedPooledObjectFactory<ConnectionDetails, Session> {

    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    public Session create(ConnectionDetails connectionDetails) throws Exception {
        log.debug("Creating session for "+connectionDetails);
        Session session = null;
        try {
            JSch jsch = new JSch();
            byte[] privateKey = connectionDetails.getPrivateKey();
            if (privateKey != null) {
                jsch.addIdentity(connectionDetails.getUsername(), privateKey, null, connectionDetails.getPassword().getBytes());
            }
            session = jsch.getSession(connectionDetails.getUsername(), connectionDetails.getHost(), connectionDetails.getPort());
            session.setPassword(connectionDetails.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setDaemonThread(true);
            session.setTimeout(60000);
            session.connect();
        } catch (Exception e) {
            log.error("Failed to connect to "+connectionDetails);
            throw e;
        }
        return session;
    }

    @Override
    public PooledObject<Session> wrap(Session session) {
        return new DefaultPooledObject(session);
    }

    @Override
    public void destroyObject(ConnectionDetails connectionDetails, PooledObject<Session> sessionObject) {
        log.debug("Destroying session for "+connectionDetails);
        if (sessionObject != null) {
            Session session = sessionObject.getObject();
            if (session != null) {
                session.disconnect();
            }
        }
    }

    @Override
    public boolean validateObject(ConnectionDetails connectionDetails, PooledObject<Session> sessionObject) {
        log.debug("Validating session for "+connectionDetails);
        Session session = (sessionObject == null) ? null : sessionObject.getObject();
        return session != null && session.isConnected();
    }
}
