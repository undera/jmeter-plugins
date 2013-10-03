package kg.apc.jmeter.config;

import javax.swing.*;
import java.awt.*;

public class JMeterServerPanel extends JPanel {

    private final JTextField serverName;

    public JMeterServerPanel(String srvName) {
        super();
        setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        setBorder(BorderFactory.createEtchedBorder());
        add(serverName = new JTextField(srvName, 32));
    }

    public String getServerName() {
        return serverName.getText();
    }
}
