package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;

import javax.swing.*;
import java.awt.*;

public class ServiceDiscovery extends AbstractXMPPAction {
    private static final java.lang.String ENTITY_ID = "entity_id";
    private static final java.lang.String TYPE = "discovery_type";
    private JTextField entityID;
    private JComboBox<String> discoType;

    @Override
    public String getLabel() {
        return "Service Discovery (XEP-0030)";
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        String entID = sampler.getPropertyAsString(ENTITY_ID);
        res.setSamplerData("Entity ID: " + entID);
        ServiceDiscoveryManager discoMgr = ServiceDiscoveryManager.getInstanceFor(sampler.getXMPPConnection());
        IQ info;
        if (Type.valueOf(sampler.getPropertyAsString(TYPE)) == Type.info) {
            info = discoMgr.discoverInfo(entID);
        } else {
            info = discoMgr.discoverItems(entID);
        }
        res.setResponseData(info.toXML().toString().getBytes());
        return res;
    }

    @Override
    public void addUI(JComponent panel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        addToPanel(panel, labelConstraints, 0, 0, new JLabel("Entity ID: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 0, entityID = new JTextField(20));

        addToPanel(panel, labelConstraints, 0, 1, new JLabel("Type: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 1, discoType = new JComboBox<>());
        discoType.addItem(Type.items.toString());
        discoType.addItem(Type.info.toString());
    }

    @Override
    public void clearGui() {
        entityID.setText("");
        discoType.setSelectedIndex(0);
    }

    @Override
    public void setSamplerProperties(JMeterXMPPSampler sampler) {
        sampler.setProperty(ENTITY_ID, entityID.getText());
        sampler.setProperty(TYPE, discoType.getSelectedItem().toString());
    }

    @Override
    public void setGuiFieldsFromSampler(JMeterXMPPSampler sampler) {
        entityID.setText(sampler.getPropertyAsString(ENTITY_ID));
        discoType.setSelectedItem(Type.valueOf(sampler.getPropertyAsString(TYPE, Type.items.toString())));
    }

    public enum Type {info, items}
}
