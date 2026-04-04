package kg.apc.jmeter.plugins.importer;

import org.apache.jmeter.gui.plugin.MenuCreator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Registers the "Swagger / Postman Importer" entry under the JMeter Tools menu.
 * JMeter 5.x discovers this class via the service-loader file for
 * {@code org.apache.jmeter.gui.plugin.MenuCreator}.
 */
public class SwaggerPostmanImporterMenuCreator implements MenuCreator {

    @Override
    public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION location) {
        if (location == MENU_LOCATION.TOOLS) {
            JMenuItem item = new JMenuItem("Swagger / Postman Importer");
            item.setActionCommand(SwaggerPostmanImporterAction.COMMAND);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    org.apache.jmeter.gui.action.ActionRouter.getInstance()
                            .actionPerformed(e);
                }
            });
            return new JMenuItem[]{item};
        }
        return new JMenuItem[0];
    }

    @Override
    public JMenu[] getTopLevelMenus() {
        return new JMenu[0];
    }

    @Override
    public boolean localeChanged(MenuElement menu) {
        return false;
    }

    @Override
    public void localeChanged() {
        // no-op
    }
}
