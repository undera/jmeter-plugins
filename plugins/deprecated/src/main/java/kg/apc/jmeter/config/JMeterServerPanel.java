package kg.apc.jmeter.config;

import javax.swing.*;
import java.awt.*;

public class JMeterServerPanel extends JPanel {

    private final JTextField serverName;
    private ServersListPanel owner;

    public JMeterServerPanel(String srvName, ServersListPanel aOwner) {
        super();
        owner = aOwner;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        setBorder(BorderFactory.createEtchedBorder());
        add(new JLabel("Server Address: "));
        add(serverName = new JTextField(srvName, 32));
        add(new JLabel("Status: "));
        add(new JLabel("unknown"));
        add(new JButton("Get Status"));
        JButton btnRemove = new JButton("Remove");
        btnRemove.addActionListener(new RemoveServerAction(this));
        add(btnRemove);
    }

    public String getServerName() {
        return serverName.getText();
    }

    public void remove() {
        owner.removeServer(this);
    }
}
