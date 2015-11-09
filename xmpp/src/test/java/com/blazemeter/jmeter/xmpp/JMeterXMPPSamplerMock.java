package com.blazemeter.jmeter.xmpp;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class JMeterXMPPSamplerMock extends JMeterXMPPSampler {

    public XMPPConnectionMock conn = new XMPPConnectionMock();

    @Override
    public XMPPConnection getXMPPConnection() throws KeyManagementException, NoSuchAlgorithmException, SmackException {
        return conn;
    }
}
