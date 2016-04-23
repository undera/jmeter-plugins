package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import javax.swing.*;
import java.awt.*;

public class SendPresence extends AbstractXMPPAction implements PacketListener {
    public static final String RECIPIENT = "recipient";
    public static final String STATUS_TEXT = "text";
    public static final String TYPE = "type";
    public static final String MODE = "mode";
    private JTextField recipient;
    private JTextField text;
    private JComboBox<Presence.Type> type;
    private JComboBox<Presence.Mode> mode;

    @Override
    public String getLabel() {
        return "Send Presence";
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        Presence.Type typeVal = Presence.Type.valueOf(sampler.getPropertyAsString(TYPE, Presence.Type.available.toString()));
        Presence.Mode modeVal = Presence.Mode.valueOf(sampler.getPropertyAsString(MODE, Presence.Mode.available.toString()));

        Presence presence = new Presence(typeVal);
        presence.setMode(modeVal);

        String to = sampler.getPropertyAsString(RECIPIENT);
        if (!to.isEmpty()) {
            presence.setTo(to);
        }

        String text = sampler.getPropertyAsString(STATUS_TEXT);
        if (!text.isEmpty()) {
            presence.setStatus(text);
        }

        sampler.getXMPPConnection().sendPacket(presence);
        res.setSamplerData(presence.toXML().toString());
        return res;
    }

    @Override
    public void addUI(JComponent mainPanel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Type: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, type = new JComboBox<>());
        type.addItem(Presence.Type.available);
        type.addItem(Presence.Type.unavailable);
        type.addItem(Presence.Type.subscribe);
        type.addItem(Presence.Type.unsubscribe);

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Status: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, mode = new JComboBox<>());
        mode.addItem(Presence.Mode.available);
        mode.addItem(Presence.Mode.away);
        mode.addItem(Presence.Mode.chat);
        mode.addItem(Presence.Mode.dnd);
        mode.addItem(Presence.Mode.xa);

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Text: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, text = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Recipient: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, recipient = new JTextField(20));
    }

    @Override
    public void clearGui() {
        recipient.setText("");
        text.setText("");
        type.setSelectedIndex(0);
        mode.setSelectedIndex(0);
    }

    @Override
    public void setSamplerProperties(JMeterXMPPSampler sampler) {
        sampler.setProperty(RECIPIENT, recipient.getText());
        sampler.setProperty(STATUS_TEXT, text.getText());
        sampler.setProperty(TYPE, type.getSelectedItem().toString());
        sampler.setProperty(MODE, mode.getSelectedItem().toString());
    }

    @Override
    public void setGuiFieldsFromSampler(JMeterXMPPSampler sampler) {
        recipient.setText(sampler.getPropertyAsString(RECIPIENT));
        text.setText(sampler.getPropertyAsString(STATUS_TEXT));
        type.setSelectedItem(Presence.Type.valueOf(sampler.getPropertyAsString(TYPE, Presence.Type.available.toString())));
        mode.setSelectedItem(Presence.Mode.valueOf(sampler.getPropertyAsString(MODE, Presence.Mode.available.toString())));
    }

    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {
        /** TODO: do we need to respond?
         if (packet instanceof Presence) {
         Presence presence = (Presence) packet;
         if (presence.getType() == Presence.Type.subscribe) {
         try {
         conn.getRoster().createEntry(presence.getFrom(), presence.getFrom(), new String[0]);
         } catch (SmackException.NotLoggedInException | SmackException.NoResponseException | XMPPException.XMPPErrorException e) {
         log.error("Failed to add to roster", e);
         }
         }
         }
         */
    }
}
