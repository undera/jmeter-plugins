package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import com.blazemeter.jmeter.xmpp.JMeterXMPPSamplerMock;
import com.blazemeter.jmeter.xmpp.XMPPConnectionMock;
import org.apache.jmeter.samplers.SampleResult;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.junit.Assert;
import org.junit.Test;


public class SendMessageTest {
    @Test
    public void perform() throws Exception {
        SendMessage action = new SendMessage();
        XMPPConnectionMock connection = new XMPPConnectionMock();
        action.connected(connection);
        Message resp = new Message();
        resp.setFrom("test@test.com");
        resp.setBody(SendMessage.RESPONSE_MARKER);
        action.processPacket(resp);
        JMeterXMPPSampler sampler = new JMeterXMPPSamplerMock();
        sampler.getXMPPConnection().setFromMode(XMPPConnection.FromMode.USER);
        sampler.setProperty(SendMessage.RECIPIENT, "test@test.com");
        sampler.setProperty(SendMessage.WAIT_RESPONSE, true);
        SampleResult res = new SampleResult();
        action.perform(sampler, res);
        Assert.assertTrue(res.getResponseDataAsString().contains(SendMessage.RESPONSE_MARKER));
        Assert.assertTrue(res.getSamplerData().contains("from"));
    }

}