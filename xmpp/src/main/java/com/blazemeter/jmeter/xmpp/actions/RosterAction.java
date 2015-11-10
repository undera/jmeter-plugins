package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import javax.swing.*;
import java.awt.*;

public class RosterAction extends AbstractXMPPAction {
    private static final java.lang.String ENTRY = "roster_entry";
    private static final java.lang.String ACTION = "roster_action";
    private JComboBox<Action> rosterAction;
    private JTextField rosterItem;

    @Override
    public String getLabel() {
        return "Roster Actions";
    }

    @Override
    public void addUI(JComponent mainPanel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Type: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, rosterAction = new JComboBox<>());
        rosterAction.addItem(Action.get_roster);
        rosterAction.addItem(Action.add_item);
        rosterAction.addItem(Action.delete_item);

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("JID: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, rosterItem = new JTextField(20));
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        Action action = Action.valueOf(sampler.getPropertyAsString(ACTION, Action.get_roster.toString()));
        Roster roster = sampler.getXMPPConnection().getRoster();
        String entry = sampler.getPropertyAsString(ENTRY);
        res.setSamplerData(action.toString() + ": " + entry);
        if (action == Action.get_roster) {
            res.setResponseData(rosterToString(roster).getBytes());
        } else if (action == Action.add_item) {
            roster.createEntry(entry, entry, new String[0]);
        } else if (action == Action.delete_item) {
            RosterEntry rosterEntry = roster.getEntry(entry);
            if (rosterEntry != null) {
                roster.removeEntry(rosterEntry);
            }
        }

        return res;
    }

    @Override
    public void clearGui() {
        rosterAction.setSelectedIndex(0);
        rosterItem.setText("");
    }

    @Override
    public void setSamplerProperties(JMeterXMPPSampler sampler) {
        sampler.setProperty(ACTION, rosterAction.getSelectedItem().toString());
        sampler.setProperty(ENTRY, rosterItem.getText());
    }

    @Override
    public void setGuiFieldsFromSampler(JMeterXMPPSampler sampler) {
        rosterItem.setText(sampler.getPropertyAsString(ENTRY));
        rosterAction.setSelectedItem(Action.valueOf(sampler.getPropertyAsString(ACTION, Action.get_roster.toString())));
    }

    private String rosterToString(Roster roster) {
        StringBuilder res = new StringBuilder();
        for (RosterEntry entry : roster.getEntries()) {
            res.append(entry.toString());
            res.append(':');
            res.append(roster.getPresence(entry.getUser()).toString());
            res.append('\n');
        }
        return res.toString();
    }

    public enum Action {
        get_roster, add_item, delete_item
    }
}
