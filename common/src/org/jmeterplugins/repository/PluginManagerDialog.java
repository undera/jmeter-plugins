package org.jmeterplugins.repository;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jorphan.gui.ComponentUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PluginManagerDialog extends JDialog {
    private final PluginManager manager;

    public PluginManagerDialog(PluginManager aManager) {
        super((JFrame) null, "Plugins Manager", true);
        this.manager = aManager;
        this.setSize(new Dimension(640, 480));
        this.setIconImage(JMeterPluginsUtils.getIcon().getImage());
        ComponentUtil.centerComponentInWindow(this, 50);
        try {
            this.manager.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(getBottomPanel());
    }

    private JPanel getBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea modifs = new JTextArea();
        modifs.setEditable(false);
        panel.add(modifs, BorderLayout.CENTER);

        JButton apply = new JButton("Apply");
        panel.add(apply, BorderLayout.SOUTH);

        apply.addActionListener(new ApplyAction());
        return panel;
    }

    private class ApplyAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // FIXME: what to do when user presses "cancel" on save test plan dialog?
            Plugin test = manager.getPluginByID("jmeter-tcp");

            final Set<Plugin> deletions = new HashSet<>();
            if (test.isInstalled()) {
                deletions.add(test);
            }

            final Set<Plugin> additions = new HashSet<>();
            if (!test.isInstalled()) {
                additions.add(test);
            }

            manager.modifierHook(deletions, additions);
            // query updates for installed
            ActionRouter.getInstance().actionPerformed(new ActionEvent(this, 0, ActionNames.EXIT));
        }
    }
}
