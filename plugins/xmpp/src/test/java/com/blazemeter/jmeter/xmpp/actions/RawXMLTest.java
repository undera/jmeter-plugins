package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSamplerMock;
import org.apache.jmeter.samplers.Sampler;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.assertFalse;

public class RawXMLTest {

    private RawXML obj;

    @Before
    public void setUp() throws Exception {
        this.obj = new RawXML();
    }

    @Test
    public void testGetLabel() throws Exception {
        assertFalse(obj.getLabel().isEmpty());
    }

    @Test
    public void testPerform() throws Exception {
        Sampler s = new JMeterXMPPSamplerMock();
        s.setProperty(RawXML.XML, "<iq type=\"test\" />");
    }

    @Test
    public void testAddUI() throws Exception {
        obj.addUI(new JPanel(), new GridBagConstraints(), new GridBagConstraints());
        obj.setSamplerProperties(new JMeterXMPPSamplerMock());
        obj.setGuiFieldsFromSampler(new JMeterXMPPSamplerMock());
        obj.clearGui();
    }

}