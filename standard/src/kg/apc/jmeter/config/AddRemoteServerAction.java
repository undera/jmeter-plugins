package kg.apc.jmeter.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddRemoteServerAction implements ActionListener {
    private ServersListPanel servers;

    public AddRemoteServerAction(ServersListPanel serversPanel) {
        servers = serversPanel;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        servers.add("");
    }
}
