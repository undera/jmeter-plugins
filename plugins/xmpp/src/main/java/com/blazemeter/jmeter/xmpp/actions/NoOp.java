package com.blazemeter.jmeter.xmpp.actions;


import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class NoOp extends AbstractXMPPAction implements PacketListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private Queue<Packet> incomingPackets = new LinkedBlockingQueue<>();

    @Override
    public String getLabel() {
        return "Collect Incoming Packets";
    }

    @Override
    public void addUI(JComponent panel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        panel.add(new JLabel("Generates no sample if there was no incoming packets."));
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        long counter = 0;
        for (Packet packet : incomingPackets) {
            incomingPackets.remove(packet);
            SampleResult subRes = new SampleResult();
            subRes.setSuccessful(true);
            subRes.setResponseCode("200");
            subRes.setResponseMessage("OK");
            subRes.setSampleLabel(packet.getClass().getSimpleName().isEmpty() ? packet.getClass().getName() : packet.getClass().getSimpleName());
            subRes.setResponseData(packet.toXML().toString().getBytes());

            if ((packet instanceof Presence) && (((Presence) packet).getType() == Presence.Type.error)) {
                subRes.setSuccessful(false);
                subRes.setResponseCode("500");
                subRes.setResponseMessage(packet.getError().toString());
            } else if ((packet instanceof Message) && (((Message) packet).getType() == Message.Type.error)) {
                subRes.setSuccessful(false);
                subRes.setResponseCode("500");
                subRes.setResponseMessage(packet.getError().toString());
            } else if ((packet instanceof IQ) && (((IQ) packet).getType() == IQ.Type.ERROR)) {
                subRes.setSuccessful(false);
                subRes.setResponseCode("500");
                subRes.setResponseMessage(packet.getError().toString());
            }

            res.addSubResult(subRes);
            counter++;
        }
        res.setResponseData(("Received packets: " + counter).getBytes());
        return counter > 0 ? res : null;
    }

    @Override
    public void clearGui() {

    }

    @Override
    public void setSamplerProperties(JMeterXMPPSampler sampler) {

    }

    @Override
    public void setGuiFieldsFromSampler(JMeterXMPPSampler sampler) {

    }

    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {
        log.debug("Adding pending packet: " + packet.toXML());
        //log.debug("Extensions: " + Arrays.toString(packet.getExtensions().toArray()));
        incomingPackets.add(packet);
    }

    @Override
    public PacketFilter getPacketFilter() {
        return new OrFilter(PacketTypeFilter.MESSAGE, PacketTypeFilter.PRESENCE, new PacketTypeFilter(IQ.class));
    }
}
