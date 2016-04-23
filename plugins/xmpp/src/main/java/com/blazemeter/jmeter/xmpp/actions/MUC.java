package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.jivesoftware.smackx.muc.MultiUserChat;

import javax.swing.*;
import java.awt.*;

public class MUC extends AbstractXMPPAction {
    private static final java.lang.String ROOM = "muc_room";
    private static final java.lang.String NICKNAME = "muc_nickname";
    private JTextField roomname;
    private JTextField nickname;

    @Override
    public String getLabel() {
        return "Join Multi-User Chat (XEP-0045)";
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        String room = sampler.getPropertyAsString(ROOM);
        String nick = sampler.getPropertyAsString(NICKNAME);
        res.setSamplerData("Join Room: " + room + "/" + nick);
        MultiUserChat muc = new MultiUserChat(sampler.getXMPPConnection(), room);
        muc.join(nick);
        return res;
    }

    @Override
    public void addUI(JComponent panel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        addToPanel(panel, labelConstraints, 0, 0, new JLabel("Room: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 0, roomname = new JTextField(20));

        addToPanel(panel, labelConstraints, 0, 1, new JLabel("Nickname: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 1, nickname = new JTextField(20));
    }

    @Override
    public void clearGui() {
        roomname.setText("");
        nickname.setText("");
    }

    @Override
    public void setSamplerProperties(JMeterXMPPSampler sampler) {
        sampler.setProperty(ROOM, roomname.getText());
        sampler.setProperty(NICKNAME, nickname.getText());
    }

    @Override
    public void setGuiFieldsFromSampler(JMeterXMPPSampler sampler) {
        roomname.setText(sampler.getPropertyAsString(ROOM));
        nickname.setText(sampler.getPropertyAsString(NICKNAME));
    }
}
