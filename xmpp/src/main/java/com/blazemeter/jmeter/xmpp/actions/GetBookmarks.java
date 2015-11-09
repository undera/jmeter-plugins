package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.bookmarks.BookmarkedConference;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class GetBookmarks extends AbstractXMPPAction {
    @Override
    public String getLabel() {
        return "Get Bookmarked Conferences (XEP-0048)";
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        BookmarkManager manager = BookmarkManager.getBookmarkManager(sampler.getXMPPConnection());
        Collection<BookmarkedConference> confs = manager.getBookmarkedConferences();
        StringBuilder text = new StringBuilder();
        for (BookmarkedConference room : confs) {
            text.append(room.getJid());
            text.append("\r\n");
        }
        res.setResponseData(text.toString().getBytes());
        return res;
    }

    @Override
    public void addUI(JComponent panel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        panel.add(new JLabel("Request list of bookmarked chat rooms from the server"));
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
}
