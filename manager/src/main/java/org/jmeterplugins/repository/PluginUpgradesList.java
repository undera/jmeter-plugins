package org.jmeterplugins.repository;

import javax.swing.event.ChangeListener;
import java.util.Set;

public class PluginUpgradesList extends PluginsList {
    public PluginUpgradesList(Set<Plugin> plugins, ChangeListener checkboxNotifier, GenericCallback<Object> dialogRefresh) {
        super(plugins, checkboxNotifier, dialogRefresh);
    }

    @Override
    protected PluginCheckbox getCheckboxItem(Plugin plugin, ChangeListener changeNotifier) {
        PluginCheckbox checkboxItem = super.getCheckboxItem(plugin, changeNotifier);
        plugin.setCandidateVersion(plugin.getMaxVersion());
        checkboxItem.setSelected(true);
        return checkboxItem;
    }

    @Override
    protected void setUpVersionsList(PluginCheckbox cb) {
        super.setUpVersionsList(cb);
        version.setEnabled(false);
    }

    @Override
    protected String getCbVersion(PluginCheckbox cb) {
        if (cb.isSelected()) {
            return cb.getPlugin().getMaxVersion();
        } else {
            return cb.getPlugin().getInstalledVersion();
        }
    }
}
