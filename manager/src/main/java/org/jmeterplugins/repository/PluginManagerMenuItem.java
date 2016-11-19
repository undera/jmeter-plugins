package org.jmeterplugins.repository;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class PluginManagerMenuItem extends JMenuItem implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8708638472918746046L;
	private static final Logger log = LoggingManager.getLoggerForClass();
    private static PluginManagerDialog dialog;
    private final PluginManager mgr;

    public PluginManagerMenuItem() {
        super("Plugins Manager", getPluginsIcon());
        addActionListener(this);

        mgr = new PluginManager(); // don't delay startup for longer that 1 second
        try {
            mgr.load();
        } catch (Throwable e) {
            log.warn("Failed to load plugin updates info", e);
        }

        if (mgr.hasAnyUpdates()) {
            setText("Plugins Manager (has upgrades)");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (dialog == null) {
            int timeout = Integer.parseInt(JMeterUtils.getPropDefault("jpgc.repo.timeout", "30000"));
            mgr.setTimeout(timeout);
            dialog = new PluginManagerDialog(mgr);
        }

        dialog.pack();
        dialog.setVisible(true);
    }

    public static ImageIcon getPluginsIcon() {
        return new ImageIcon(PluginManagerMenuItem.class.getResource("/org/jmeterplugins/logo.png"));
    }
}
