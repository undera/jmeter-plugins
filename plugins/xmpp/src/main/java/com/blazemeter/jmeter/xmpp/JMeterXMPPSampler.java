package com.blazemeter.jmeter.xmpp;

import com.blazemeter.jmeter.xmpp.actions.AbstractXMPPAction;
import com.blazemeter.jmeter.xmpp.actions.Connect;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class JMeterXMPPSampler extends AbstractSampler {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String ACTION = "action";
    protected JMeterXMPPConnection connConfig;

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.setDataType(SampleResult.TEXT);
        res.setSuccessful(true);
        res.setResponseCode("200");
        res.setResponseMessage("OK");

        res.sampleStart();
        try {
            if (connConfig == null) {
                throw new RuntimeException("Cannot sample XMPP without XMPP Connection component");
            }

            XMPPConnection conn = getXMPPConnection();

            if (conn == null) {
                throw new RuntimeException("No XMPP Connection available");
            }

            String headers = "Connection ID: " + conn.getConnectionID() + "\r\n";

            String action = getAction();
            if (!conn.isConnected() && !action.equals(Connect.class.getCanonicalName())) {
                log.error("Please call Connect before calling other actions");
                throw new SmackException.NotConnectedException();
            }

            headers += "User: " + conn.getUser() + "\r\n";

            res.setRequestHeaders(headers);

            AbstractXMPPAction actObject = connConfig.getActions().get(action);
            if (actObject.perform(this, res) == null) {
                return null;
            }
        } catch (Exception e) {
            log.error("Error in XMPP Sampler: ", e);
            res.setSuccessful(false);
            res.setResponseCode("500");
            res.setResponseMessage((e.getMessage() == null || e.getMessage().isEmpty()) ? e.toString() : e.getMessage());
            res.setResponseData(ExceptionUtils.getStackTrace(e).getBytes());
        }

        res.sampleEnd();
        return res;
    }

    public String getAction() {
        return getPropertyAsString(ACTION);
    }

    public void setAction(String value) {
        setProperty(ACTION, value);
    }

    public XMPPConnection getXMPPConnection() throws KeyManagementException, NoSuchAlgorithmException, SmackException, InterruptedException {
        return connConfig.getConnection();
    }

    @Override
    public void addTestElement(TestElement el) {
        super.addTestElement(el);

        if (el instanceof JMeterXMPPConnection) {
            this.connConfig = (JMeterXMPPConnection) el;
        }
    }

    public JMeterXMPPConnection getXMPPConnectionConfig() {
        return connConfig;
    }
}
