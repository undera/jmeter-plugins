package com.blazemeter.jmeter.xmpp;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.XMPPConnection;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public abstract class JMeterXMPPConnectionBase extends ConfigTestElement implements TestStateListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String ADDRESS = "address";
    private static final String PORT = "port";
    private static final String SERVICE_NAME = "service_name";
    private static final String TIMEOUT = "timeout";
    private static final String TYPE = "class";
    private static final String BOSH_IS_SSL = "bosh_is_ssl";
    private static final String BOSH_URL = "bosh_url";
    private static final String FROM_MODE = "from_mode";

    public String getAddress() {
        return getPropertyAsString(ADDRESS);
    }

    public void setAddress(String value) {
        setProperty(ADDRESS, value);
    }

    public String getPort() {
        return getPropertyAsString(PORT, "5222");
    }

    public void setPort(String value) {
        setProperty(PORT, value);
    }

    public String getServiceName() {
        return getPropertyAsString(SERVICE_NAME);
    }

    public void setServiceName(String value) {
        setProperty(SERVICE_NAME, value);
    }

    public boolean isBOSHSSL() {
        return getPropertyAsBoolean(BOSH_IS_SSL);
    }

    public void setBOSHSSL(boolean value) {
        setProperty(BOSH_IS_SSL, value);
    }

    public String getBOSHURL() {
        return getPropertyAsString(BOSH_URL, "/http-bind/");
    }

    public void setBOSHURL(String value) {
        setProperty(BOSH_URL, value);
    }

    public String getPacketReplyTimeout() {
        return getPropertyAsString(TIMEOUT, "1000");
    }

    public void setPacketReplyTimeout(String value) {
        setProperty(TIMEOUT, value);
    }

    public String getConnectionType() {
        return getPropertyAsString(TYPE, JMeterXMPPConnectionBase.Type.TCP.toString());
    }

    public void setConnectionType(String value) {
        setProperty(TYPE, value);
    }

    public XMPPConnection.FromMode getFromMode() {
        String str = getPropertyAsString(FROM_MODE, XMPPConnection.FromMode.USER.toString());
        if (str.equals(XMPPConnection.FromMode.USER.toString())) {
            return XMPPConnection.FromMode.USER;
        } else if (str.equals(XMPPConnection.FromMode.UNCHANGED.toString())) {
            return XMPPConnection.FromMode.UNCHANGED;
        } else if (str.equals(XMPPConnection.FromMode.OMITTED.toString())) {
            return XMPPConnection.FromMode.OMITTED;
        } else {
            throw new IllegalArgumentException("Unhandled value for fromMode: " + str);
        }
    }

    public void setFromMode(String value) {
        setProperty(FROM_MODE, value);
    }

    @Override
    public void testStarted(String host) {

    }

    protected SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(this.getKS(), this.getTM(), new java.security.SecureRandom());
        return ctx;
    }


    private KeyManager[] getKS() {
        return new KeyManager[0];
    }

    private TrustManager[] getTM() {
        TrustManager[] res = new TrustManager[1];
        res[0] = new DummyTrustManager();
        return res;
    }

    @Override
    public void testEnded() {
        testEnded("");
    }

    @Override
    public void testStarted() {
        testStarted("");
    }

    public abstract void resetConnection();

    public enum Type {TCP, BOSH}


    private class DummyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            log.debug("checkClientTrusted " + s + ": " + Arrays.toString(x509Certificates));
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            log.debug("checkServerTrusted " + s);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            log.debug("getAcceptedIssuers");
            return new X509Certificate[0];
        }
    }

}
