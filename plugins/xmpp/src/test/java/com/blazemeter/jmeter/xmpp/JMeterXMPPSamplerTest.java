package com.blazemeter.jmeter.xmpp;

import com.blazemeter.jmeter.xmpp.actions.*;
import junit.framework.AssertionFailedError;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.iqprivate.packet.PrivateData;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class JMeterXMPPSamplerTest {
    private static final Logger log = LoggingManager.getLoggerForClass();

    @BeforeClass
    public static void setUpClass()
            throws Exception {
    }

    @Test
    public void testSample_noConfig() throws Exception {
        JMeterXMPPSampler obj = new JMeterXMPPSampler();
        try {
            doAction(obj, Login.class);
            fail();
        } catch (AssertionError ignored) {
        }
    }

    @Test
    public void testSample1() throws Exception {
        JMeterXMPPSampler obj = getjMeterXMPPSampler();

        obj.setProperty(Login.LOGIN, "user1");
        obj.setProperty(Login.PASSWORD, "1");
        doAction(obj, Login.class);
    }

    @Test
    public void sendMessageFrom() throws Exception {
        JMeterXMPPSampler obj = getjMeterXMPPSampler();
        obj.getXMPPConnection().setFromMode(XMPPConnection.FromMode.USER);

        obj.setProperty(SendMessage.RECIPIENT, "user2@undera-desktop");
        obj.setProperty(SendMessage.BODY, "body");
        SampleResult res = doAction(obj, SendMessage.class);
        assertTrue(res.getSamplerData().contains("from"));
    }


    public void testLoginLogout() throws Exception {
        TestJMeterUtils.createJmeterEnv();
        JMeterXMPPSampler obj = new JMeterXMPPSampler();
        JMeterXMPPConnection conn = new JMeterXMPPConnection();
        fillActionClasses(conn.getActions());
        conn.setConnectionType(JMeterXMPPConnectionBase.Type.BOSH.toString());
        conn.setAddress("view.webcast.cisco.com");
        conn.setPort("443");
        conn.setBOSHSSL(true);
        conn.setServiceName("cisco.com");
        conn.setPacketReplyTimeout("0");
        conn.setBOSHURL("/httpbinding/");

        conn.setAddress("192.168.5.4");
        conn.setPort("7070");
        conn.setBOSHSSL(false);
        conn.setServiceName("example.com");
        conn.setPacketReplyTimeout("5000");
        conn.setBOSHURL("/http-bind/");


        conn.testStarted();
        obj.addTestElement(conn);
        obj.setProperty(Login.LOGIN, "dkollava@cisco.com");
        obj.setProperty(Login.PASSWORD, "cisco123");
        obj.setProperty(Login.RESOURCE, "qanda-panelist");

        for (int a = 0; a < 2; a++) {
            doAction(obj, Connect.class);
            SASLAuthentication.getRegisterSASLMechanisms();
            doAction(obj, Login.class);
            //Thread.sleep(500);
            doAction(obj, Disconnect.class);
            //Thread.sleep(500);
        }
    }

    //@Test
    public void testSample() throws Exception {
        JMeterXMPPSampler obj = getjMeterXMPPSampler();

        obj.setProperty(Login.LOGIN, "user1");
        obj.setProperty(Login.PASSWORD, "1");
        doAction(obj, Login.class);

        doAction(obj, SendPresence.class);
        doAction(obj, RosterAction.class);
        doAction(obj, GetBookmarks.class);


        obj.setProperty(SendPresence.RECIPIENT, "user1@localhost");
        obj.setProperty(SendPresence.TYPE, Presence.Type.subscribe.toString());
        doAction(obj, SendPresence.class);

        obj.setProperty(SendMessage.RECIPIENT, "user1@localhost");
        obj.setProperty(SendMessage.BODY, "test");
        doAction(obj, SendMessage.class);

        while (true) {
            try {
                if (doAction(obj, NoOp.class) == null) {
                    break;
                }
            } catch (AssertionFailedError ignored) {
            }
        }

        obj.setProperty(SendMessage.RECIPIENT, "user1@localhost");
        doAction(obj, SendMessage.class);

        obj.setProperty(SendMessage.RECIPIENT, "user1@localhost");
        obj.setProperty(SendMessage.BODY, "need response");
        obj.setProperty(SendMessage.WAIT_RESPONSE, true);
        doAction(obj, SendMessage.class);

        Thread.sleep(1000);
        doAction(obj, Disconnect.class);
        Thread.sleep(1000);

        JMeterXMPPSampler obj2 = getjMeterXMPPSampler();
        obj2.setProperty(Login.LOGIN, "user2");
        obj2.setProperty(Login.PASSWORD, "1");
        doAction(obj2, Login.class);
        doAction(obj2, SendPresence.class);
        while (true) {
            try {
                if (doAction(obj2, NoOp.class) == null) {
                    break;
                }
            } catch (AssertionFailedError ignored) {
            }
        }
        doAction(obj2, Disconnect.class);
    }

    //@Test
    public void testSample_FileTransfer() throws Exception {
        JMeterXMPPSampler obj = getjMeterXMPPSampler();

        obj.setProperty(Login.LOGIN, "user1");
        obj.setProperty(Login.PASSWORD, "1");
        doAction(obj, Login.class);

        doAction(obj, SendPresence.class);

        obj.setProperty(SendFileXEP0096.FILE_PATH, "/bin/mv");
        obj.setProperty(SendFileXEP0096.FILE_RECIPIENT, "user1@localhost/Smack");
        doAction(obj, SendFileXEP0096.class);
        doAction(obj, Disconnect.class);
    }

    private JMeterXMPPSampler getjMeterXMPPSampler() {
        JMeterXMPPSampler obj = new JMeterXMPPSampler();
        JMeterXMPPConnection conn = new JMeterXMPPConnectionMock();
        fillActionClasses(conn.getActions());
        conn.setAddress("192.168.5.3");
        conn.setServiceName("localhost");
        conn.setPacketReplyTimeout("1000");

        conn.testStarted();
        obj.addTestElement(conn);
        return obj;
    }

    private SampleResult doAction(JMeterXMPPSampler obj, Class action) {
        obj.setAction(action.getCanonicalName());
        SampleResult res = obj.sample(null);
        if (res != null) {
            log.debug("Action " + action + " response: " + res.getResponseDataAsString());
            assertTrue(res.isSuccessful());
        }
        return res;
    }

    public static void fillActionClasses(Map<String, AbstractXMPPAction> list) {
        list.put(RosterAction.class.getCanonicalName(), new RosterAction());
        list.put(Login.class.getCanonicalName(), new Login());
        list.put(NoOp.class.getCanonicalName(), new NoOp());
        list.put(SendFileXEP0096.class.getCanonicalName(), new SendFileXEP0096());
        list.put(SendMessage.class.getCanonicalName(), new SendMessage());
        list.put(SendPresence.class.getCanonicalName(), new SendPresence());
        list.put(GetBookmarks.class.getCanonicalName(), new GetBookmarks());
        list.put(Disconnect.class.getCanonicalName(), new Disconnect());
        list.put(Connect.class.getCanonicalName(), new Connect());
    }

    private class JMeterXMPPConnectionMock extends JMeterXMPPConnection {
        public XMPPConnectionMock conn = new XMPPConnectionMock();

        @Override
        public XMPPConnection getConnection() throws NoSuchAlgorithmException, KeyManagementException, SmackException {
            return conn;
        }
    }

    private static class PrivateDataResult extends IQ {

        private PrivateData privateData;

        PrivateDataResult(PrivateData privateData) {
            this.privateData = privateData;
        }

        public PrivateData getPrivateData() {
            return privateData;
        }

        public String getChildElementXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<query xmlns=\"jabber:iq:private\">");
            if (privateData != null) {
                buf.append(privateData.toXML());
            }
            buf.append("</query>");
            return buf.toString();
        }
    }
}