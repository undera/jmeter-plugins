package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHSessionFactoryIT {
    public static ConnectionDetails localConnection = new ConnectionDetails("0.0.0.0", Integer.valueOf(System.getProperty("sshmon.sshd.port")));
    public SSHSessionFactory instance;
    public Session session;

    @Before
    public void setUp() {
        instance = new SSHSessionFactory();
    }

    @After
    public void tearDown() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            session = null;
        }
    }

    @Test
    public void testWrap() throws Exception {
        session = new JSch().getSession(""); // dummy
        PooledObject<Session> actual = instance.wrap(session);
        assertTrue(actual instanceof DefaultPooledObject);
        assertEquals(session, actual.getObject());
    }

    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        session = instance.create(localConnection);
        assertTrue(session.isConnected());
    }

    @Test
    public void testDestroyObject() throws Exception {
        System.out.println("destroyObject");
        session = instance.create(localConnection);
        instance.destroyObject(localConnection, instance.wrap(session));
        assertFalse(session.isConnected());
    }

    @Test
    public void testValidateObjectTrue() throws Exception {
        System.out.println("validateObject/true");
        session = instance.create(localConnection);
        assertTrue(instance.validateObject(localConnection, instance.wrap(session)));
    }

    @Test
    public void testValidateObjectFalse() throws Exception {
        System.out.println("validateObject/false");
        session = null;
        assertFalse(instance.validateObject(localConnection, instance.wrap(session)));
    }
}
