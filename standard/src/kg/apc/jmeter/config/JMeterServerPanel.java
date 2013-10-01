package kg.apc.jmeter.config;

import javax.swing.*;

public class JMeterServerPanel extends JPanel {

    private final JTextField serverName;

    public JMeterServerPanel(String srvName) {
        super();
        add(serverName = new JTextField(srvName));
    }
}
