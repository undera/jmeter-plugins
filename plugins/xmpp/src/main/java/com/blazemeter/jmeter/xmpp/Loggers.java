package com.blazemeter.jmeter.xmpp;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;

public class Loggers {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public static class LogRecv implements PacketListener {
        private final XMPPConnection conn;

        public LogRecv(XMPPConnection conn) {
            this.conn = conn;
        }

        @Override
        public void processPacket(Packet packet) throws SmackException.NotConnectedException {
            try {
                log.debug("Packet recv [" + conn.getConnectionID() + "]: " + packet.toXML());
            } catch (IllegalArgumentException e) {
                log.debug("Failed to log packet", e);
                log.debug("Packet recv [" + conn.getConnectionID() + "]: " + packet.getError());
            }
        }
    }

    public static class LogSent implements PacketListener {
        private final XMPPConnection conn;

        public LogSent(XMPPConnection conn) {
            this.conn = conn;
        }

        @Override
        public void processPacket(Packet packet) throws SmackException.NotConnectedException {
            log.debug("Packet sent [" + conn.getConnectionID() + "]: " + packet.toXML());
        }
    }

    public static class LogConn implements ConnectionListener {
        private final XMPPConnection conn;

        public LogConn(XMPPConnection conn) {
            this.conn = conn;
        }

        @Override
        public void connected(XMPPConnection connection) {
            log.debug("Connected: " + connection.getConnectionID());
        }

        @Override
        public void authenticated(XMPPConnection connection) {
            log.debug("Authenticated: " + connection.getConnectionID());
        }

        @Override
        public void connectionClosed() {
            log.debug("Connection closed: " + conn.getConnectionID());
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            log.error("Connection closed with error: " + conn.getConnectionID(), e);
        }

        @Override
        public void reconnectingIn(int seconds) {
            log.debug("Reconnecting in: " + seconds);
        }

        @Override
        public void reconnectionSuccessful() {
            log.debug("Reconnection successfull");
        }

        @Override
        public void reconnectionFailed(Exception e) {
            log.error("Reconnection failed: ", e);

        }
    }
}
