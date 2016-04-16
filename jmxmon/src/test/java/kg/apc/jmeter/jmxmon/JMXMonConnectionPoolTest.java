package kg.apc.jmeter.jmxmon;

import java.util.Hashtable;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnectorFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import kg.apc.emulators.TestJMeterUtils;

import static org.junit.Assert.*;

public class JMXMonConnectionPoolTest {
	
	private static String jmxUrlOk = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";
	private static String jmxUrlOk2 = "service:jmx:rmi:///jndi/rmi://xxxxx:9999/jmxrmi";
	private static String jmxUrlOk3 = "service:jmx:rmi:///jndi/rmi://yyyyyy:9999/jmxrmi";
	private static String jmxUrlOk4 = "service:jmx:rmi:///jndi/rmi://zzzzz:9999/jmxrmi";
	
	private static String jmxUrlKo = "servicjmxrmi";
	@BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    	System.setProperty(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, this.getClass().getPackage().getName());
    }

    @After
    public void tearDown() {
    	System.setProperty(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, 
    			"com.sun.jmx.remote.protocol");
    }
    
	private JMXMonConnectionPool createTestSubject() {
		return new JMXMonConnectionPool();
	}

	@Test
	public void testGetConnection() throws Exception {
		JMXMonConnectionPool testSubject;
		Hashtable attributes = null;
		MBeanServerConnection result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getConnection(jmxUrlOk, attributes);
		
		result = testSubject.getConnection(jmxUrlKo, attributes);
	}

	@Test
	public void testGetConnection_1() throws Exception {
		JMXMonConnectionPool testSubject;
		Hashtable attributes = null;
		boolean wait = true;
		MBeanServerConnection result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getConnection(jmxUrlOk, attributes, wait);
		assertNotNull(result);
	}
	
	@Test
	public void testGetConnection_1NoWait() throws Exception {
		JMXMonConnectionPool testSubject;
		Hashtable attributes = null;
		boolean wait = true;
		MBeanServerConnection result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getConnection(jmxUrlOk, attributes, false);
	}

	@Test
	public void testCloseAllNoConnection() throws Exception {
		JMXMonConnectionPool testSubject;

		// default test
		testSubject = createTestSubject();
		testSubject.closeAll();
	}
	@Test
	public void testCloseAll() throws Exception {
		Hashtable attributes = new Hashtable();
        String[] buffer = {"", ""};
        attributes.put("jmx.remote.credentials", (String[]) buffer);
        
		JMXMonConnectionPool testSubject;

		// default test
		testSubject = createTestSubject();
		testSubject.getConnection(jmxUrlOk, null);
		testSubject.getConnection(jmxUrlOk2, attributes);
		testSubject.getConnection(jmxUrlOk3, null);
		testSubject.getConnection(jmxUrlOk4, attributes);
		
		testSubject.getConnection(jmxUrlOk, null);
		testSubject.getConnection(jmxUrlOk, attributes);
		testSubject.getConnection(jmxUrlOk, null);
		testSubject.getConnection(jmxUrlOk, attributes);
		
		testSubject.notifyConnectionDirty("test");
		testSubject.notifyConnectionDirty("test2");
		
		testSubject.closeAll();
	}

	@Test
	public void testNotifyConnectionDirty() throws Exception {
		JMXMonConnectionPool testSubject;
		String url = "";

		// default test
		testSubject = createTestSubject();
		testSubject.notifyConnectionDirty(url);
	}
}