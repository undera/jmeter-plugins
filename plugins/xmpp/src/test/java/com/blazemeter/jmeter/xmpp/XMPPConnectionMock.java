package com.blazemeter.jmeter.xmpp;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Packet;

import java.io.IOException;

public class XMPPConnectionMock extends XMPPConnection {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public boolean isConnected = true;
    public boolean isAuthenticated = true;

    public XMPPConnectionMock() {
        super(new ConnectionConfiguration("unitTest"));
    }

    @Override
    public String getUser() {
        return "test@unitTest";
    }

    @Override
    public String getConnectionID() {
        return "ConnID";
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public boolean isSecureConnection() {
        return false;
    }

    @Override
    protected void sendPacketInternal(Packet packet) throws SmackException.NotConnectedException {
        log.debug("Emul sending packet: " + packet.toXML());
    }

    @Override
    public boolean isUsingCompression() {
        return false;
    }

    @Override
    protected void connectInternal() throws SmackException, IOException, XMPPException {
        log.debug("Emul connect");
    }

    @Override
    public void login(String username, String password, String resource) throws XMPPException, SmackException, IOException {
        log.debug("Emul login");
    }

    @Override
    public void loginAnonymously() throws XMPPException, SmackException, IOException {
        log.debug("Emul login");
    }

    @Override
    protected void shutdown() {
        log.debug("Emul Shutdown");
    }

    @Override
    public void processPacket(Packet packet) {
        super.processPacket(packet);
    }


    public java.util.Collection<PacketCollector> getCollectors() {
        return collectors;
    }
}
