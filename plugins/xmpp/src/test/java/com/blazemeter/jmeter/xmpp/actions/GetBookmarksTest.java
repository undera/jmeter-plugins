package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSamplerMock;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.bookmarks.Bookmarks;
import org.jivesoftware.smackx.iqprivate.packet.PrivateData;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class GetBookmarksTest {
    private static final Logger log = LoggingManager.getLoggerForClass();


    public void testPerform() throws Exception {
        GetBookmarks obj = new GetBookmarks();
        final JMeterXMPPSamplerMock sampler = new JMeterXMPPSamplerMock();
        Thread thr = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!sampler.conn.getCollectors().isEmpty())
                        sampler.conn.processPacket(new PrivateDataResult(new Bookmarks()));
                }
            }
        };
        thr.start();
        Thread.sleep(sampler.conn.getPacketReplyTimeout() / 10);
        obj.perform(sampler, new SampleResult());

    }

    @Test
    public void testAddUI() throws Exception {
        GetBookmarks obj = new GetBookmarks();
        obj.addUI(new JPanel(), new GridBagConstraints(), new GridBagConstraints());
    }

    /**
     * An IQ packet to hold PrivateData GET results.
     */
    private static class PrivateDataResult extends IQ {

        private PrivateData privateData;

        PrivateDataResult(PrivateData privateData) {
            this.privateData = privateData;
        }

        public PrivateData getPrivateData() {
            return privateData;
        }

        public String getChildElementXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<query xmlns=\"jabber:iq:private\">");
            if (privateData != null) {
                buf.append(privateData.toXML());
            }
            buf.append("</query>");
            return buf.toString();
        }
    }
}