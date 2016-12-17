package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.delay.packet.DelayInfo;
import org.jivesoftware.smackx.delay.packet.DelayInformation;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class SendMessage extends AbstractXMPPAction implements PacketListener, ConnectionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String RECIPIENT = "msg_w_resp_addressee";
    public static final String BODY = "msg_w_resp_body";
    public static final String WAIT_RESPONSE = "wait_response";
    public static final String TYPE = "msg_type";

    public static final String NEED_RESPONSE_MARKER = "ExpectedResponseMarker";
    public static final String RESPONSE_MARKER = "ProvidedResponseMarker";
    private final static String NS_DELAYED = (new DelayInfo(new DelayInformation(new Date()))).getNamespace();

    private JTextField msgRecipient;
    private JTextArea msgBody;
    private JCheckBox waitResponse;
    private JComboBox<Message.Type> msgType;
    private Queue<Message> responseMessages = new LinkedBlockingQueue<>();
    private XMPPConnection conn;

    @Override
    public String getLabel() {
        return "Send Message";
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        // sending message
        String recipient = sampler.getPropertyAsString(RECIPIENT);
        String body = sampler.getPropertyAsString(BODY);
        boolean wait_response = sampler.getPropertyAsBoolean(WAIT_RESPONSE);
        if (wait_response) {
            body += "\r\n" + System.currentTimeMillis() + "@" + NEED_RESPONSE_MARKER;
        }

        Message msg = new Message(recipient);
        msg.setType(Message.Type.fromString(sampler.getPropertyAsString(TYPE, Message.Type.normal.toString())));
        msg.addBody("", body);
        res.setSamplerData(msg.toXML().toString());
        sampler.getXMPPConnection().sendPacket(msg);
        res.setSamplerData(msg.toXML().toString()); // second time to reflect the changes made to packet by conn

        if (wait_response) {
            return waitResponse(res, recipient);
        }
        return res;
    }

    private SampleResult waitResponse(SampleResult res, String recipient) throws InterruptedException, SmackException {
        long time = 0;
        do {
            Iterator<Message> packets = responseMessages.iterator();
            Thread.sleep(conn.getPacketReplyTimeout() / 100); // optimistic
            while (packets.hasNext()) {
                Packet packet = packets.next();
                Message response = (Message) packet;
                if (StringUtils.parseBareAddress(response.getFrom()).equals(recipient)) {
                    packets.remove();
                    res.setResponseData(response.toXML().toString().getBytes());
                    if (response.getError() != null) {
                        res.setSuccessful(false);
                        res.setResponseCode("500");
                        res.setResponseMessage(response.getError().toString());
                    }
                    return res;
                }
            }
            time += conn.getPacketReplyTimeout() / 10;
            Thread.sleep(conn.getPacketReplyTimeout() / 10);
        } while (time < conn.getPacketReplyTimeout());
        throw new SmackException.NoResponseException();
    }

    @Override
    public void addUI(JComponent mainPanel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Type: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, msgType = new JComboBox<>());
        msgType.addItem(Message.Type.normal);
        msgType.addItem(Message.Type.chat);
        msgType.addItem(Message.Type.groupchat);
        msgType.addItem(Message.Type.headline);
        msgType.addItem(Message.Type.error);

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Recipient: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, msgRecipient = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Message Text: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, msgBody = new JTextArea(5, 20));

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Wait for Response: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, waitResponse = new JCheckBox("(message that expects response should be auto-responded by another JMeter thread)"));
    }

    @Override
    public void clearGui() {
        msgRecipient.setText("");
        msgBody.setText("");
        waitResponse.setSelected(false);
        msgType.setSelectedIndex(0);
    }

    @Override
    public void setSamplerProperties(JMeterXMPPSampler sampler) {
        sampler.setProperty(RECIPIENT, msgRecipient.getText());
        sampler.setProperty(BODY, msgBody.getText());
        sampler.setProperty(WAIT_RESPONSE, waitResponse.isSelected());
        sampler.setProperty(TYPE, msgType.getSelectedItem().toString());
    }

    @Override
    public void setGuiFieldsFromSampler(JMeterXMPPSampler sampler) {
        msgRecipient.setText(sampler.getPropertyAsString(RECIPIENT));
        msgBody.setText(sampler.getPropertyAsString(BODY));
        waitResponse.setSelected(sampler.getPropertyAsBoolean(WAIT_RESPONSE));
        msgType.setSelectedItem(Message.Type.fromString(sampler.getPropertyAsString(TYPE, Message.Type.normal.toString())));
    }

    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {
        if (packet instanceof Message) {
            Message inMsg = (Message) packet;
            if (inMsg.getBody() != null) {
                if (inMsg.getBody().endsWith(NEED_RESPONSE_MARKER)) {
                    if (inMsg.getExtension(NS_DELAYED) == null) {
                        log.debug("Will respond to message: " + inMsg.toXML());
                        sendResponseMessage(inMsg);
                    } else {
                        log.debug("Will not consider history message: " + inMsg.toXML());
                    }
                } else if (inMsg.getBody().endsWith(RESPONSE_MARKER)) {
                    responseMessages.add(inMsg);
                }
            }
        }
    }

    private void sendResponseMessage(Message inMsg) {
        Message outMsg = new Message(inMsg.getFrom());
        outMsg.setType(inMsg.getType());
        outMsg.addBody("", inMsg.getBody() + "\r\n" + System.currentTimeMillis() + "@" + RESPONSE_MARKER);
        log.debug("Responding to message: " + outMsg.toXML());
        try {
            conn.sendPacket(outMsg);
        } catch (SmackException e) {
            log.error("Failed to send response", e);
        }
    }

    @Override
    public void connected(XMPPConnection connection) {
        this.conn = connection;
    }

    @Override
    public void authenticated(XMPPConnection connection) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }
}
