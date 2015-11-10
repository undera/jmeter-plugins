package com.blazemeter.jmeter.xmpp;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;

public class JMeterXMPPConnectionGui extends AbstractConfigGui {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private JTextField address;
    private JTextField port;
    private JTextField serviceName;
    private JTextField timeout;
    private JComboBox<JMeterXMPPConnection.Type> connectionClass;

    public JMeterXMPPConnectionGui() {
        super();
        init();
        initFields();
    }

    private void initFields() {
        address.setText("localhost");
        port.setText("5222");
        serviceName.setText("localhost");
        timeout.setText("1000");
        connectionClass.setSelectedItem(JMeterXMPPConnectionBase.Type.TCP);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), getWikiPage()), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        editConstraints.insets = new Insets(2, 0, 0, 0);
        labelConstraints.insets = new Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Server Address: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, address = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Port: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, port = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Service Name: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, serviceName = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Timeout, ms: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, timeout = new JTextField(20));

        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Transport: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, connectionClass = new JComboBox<>());
        connectionClass.addItem(JMeterXMPPConnectionBase.Type.TCP);
        connectionClass.addItem(JMeterXMPPConnectionBase.Type.BOSH);

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private String getWikiPage() {
        return "XMPPConnection";
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("XMPP Connection");
    }

    @Override
    public TestElement createTestElement() {
        TestElement el = new JMeterXMPPConnection();
        modifyTestElement(el);
        el.setComment("This plugin was developed by www.blazemeter.com");
        return el;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof JMeterXMPPConnection) {
            JMeterXMPPConnection conn = (JMeterXMPPConnection) element;
            // fill controls
            address.setText(conn.getAddress());
            port.setText(conn.getPort());
            serviceName.setText(conn.getServiceName());
            timeout.setText(conn.getPacketReplyTimeout());
            connectionClass.setSelectedItem(conn.getConnectionType());
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
        if (element instanceof JMeterXMPPConnection) {
            JMeterXMPPConnection conn = (JMeterXMPPConnection) element;
            conn.setAddress(address.getText());
            conn.setPort(port.getText());
            conn.setServiceName(serviceName.getText());
            conn.setPacketReplyTimeout(timeout.getText());
            conn.setConnectionType(connectionClass.getSelectedItem().toString());
        }
    }


}
