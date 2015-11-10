package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSamplerMock;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class DisconnectTest {

    @Test
    public void testPerform() throws Exception {
        Disconnect obj = new Disconnect();
        obj.perform(new JMeterXMPPSamplerMock(), new SampleResult());
    }

    @Test
    public void testAddUI() throws Exception {
        Disconnect obj = new Disconnect();
        obj.addUI(new JPanel(), new GridBagConstraints(), new GridBagConstraints());
    }
}