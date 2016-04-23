package com.blazemeter.jmeter.xmpp.actions;


import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * All subclasses of this class will be displayed as actions in XMPP Sampler.
 * You can just create JAR with such subclasses and put it into lib/ext of JMeter and they will become available.
 * If class will implement PacketListener, it will be added as listener to XMPPConnection.
 * Every JMeterXMPPConnection has its own set of available actions
 */
public abstract class AbstractXMPPAction implements Cloneable, Serializable {
    /**
     * @return label that will be shown in action block title, near radio button
     */
    public abstract String getLabel();

    /**
     * Perfrom action, fill necessary SampleResult fields
     *
     * @param sampler sampler instance
     * @param res     sample to fill fields. Note: do not call sampleEnd, it will be called automatically
     * @return if SampleResult present, it will be used. If null returned, sampler will not generate sample
     * @throws Exception
     */
    public abstract SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception;

    /**
     * GUI initialization method. Create and add to panel UI elements for action
     *
     * @param panel            container panel, has GridBagLayout
     * @param labelConstraints Constraints for labels in GridBagLayout
     * @param editConstraints  Constraints for edit controls in GridBagLayout
     */
    public abstract void addUI(JComponent panel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints);

    /**
     * Helper method to add Component to panel with GridBagLayout
     *
     * @param panel       container panel
     * @param constraints constraints to use when adding
     * @param col         column number
     * @param row         row number
     * @param component   component to add
     */
    protected void addToPanel(JComponent panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    /**
     * Resets controls to default state (empty fields)
     */
    public abstract void clearGui();

    /**
     * Record data from UI controls into Sampler. You must use Sampler.setPropery* methods to record it.
     *
     * @param sampler Sampler instance
     */
    public abstract void setSamplerProperties(JMeterXMPPSampler sampler);

    /**
     * Fill UI controls from Sampler properties
     *
     * @param sampler Sampler instance
     */
    public abstract void setGuiFieldsFromSampler(JMeterXMPPSampler sampler);

    public PacketFilter getPacketFilter() {
        return new AndFilter();
    }
}
