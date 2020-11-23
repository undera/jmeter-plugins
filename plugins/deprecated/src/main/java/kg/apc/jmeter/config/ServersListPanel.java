package kg.apc.jmeter.config;

import org.apache.jmeter.testelement.property.CollectionProperty;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ServersListPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(ServersListPanel.class);

    private List<JMeterServerPanel> serversList;

    public ServersListPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        serversList = new LinkedList<JMeterServerPanel>();
    }

    public void clear() {
        serversList.clear();
        removeAll();
        getParent().repaint();
    }

    public void saveToTestElement(DistributedTestControl te) {
        ArrayList<String> names = new ArrayList<String>(getCount());
        for(JMeterServerPanel srv:serversList) {
            names.add(srv.getServerName());
        }

        log.debug("Saving: " + names.toString());
        te.setData(names);
    }

    public void loadFromTestElement(DistributedTestControl te) {
        CollectionProperty servers = te.getData();
        log.debug("Loading: " + servers.toString());
        clear();
        for (int n = 0; n < servers.size(); n++) {
            log.debug("Adding: " + servers.get(n).toString());
            add(servers.get(n).getStringValue());
        }
    }

    public void add(String stringValue) {
        JMeterServerPanel panel = new JMeterServerPanel(stringValue, this);
        serversList.add(panel);
        add(panel);
        getParent().repaint();
    }

    public int getCount() {
        return serversList.size();
    }

    public void removeServer(JMeterServerPanel jMeterServerPanel) {
        serversList.remove(jMeterServerPanel);
        getParent().repaint();
    }
}
