package kg.apc.jmeter.config;

import org.apache.jmeter.testelement.property.CollectionProperty;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class ServersListPanel extends JPanel {
    private List<String> serversList;

    public ServersListPanel() {
        super();
        serversList = new LinkedList<String>();
    }

    public void clear() {
        serversList.clear();
        removeAll();
    }

    public void saveToTestElement(DistributedTestControl te) {
        CollectionProperty data = new CollectionProperty(DistributedTestControl.DATA_PROP, serversList);
        te.setData(data);
    }

    public void loadFromTestElement(DistributedTestControl te) {
        CollectionProperty servers = te.getData();
        clear();
        for (int n = 0; n < servers.size(); n++) {
            add(servers.get(n).getStringValue());
        }
    }

    public void add(String stringValue) {
        serversList.add(stringValue);
        add(new JMeterServerPanel(stringValue));
    }

    public int getCount() {
        return serversList.size();
    }
}
