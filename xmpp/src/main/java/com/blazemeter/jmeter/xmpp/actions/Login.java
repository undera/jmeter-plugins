package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import java.awt.GridBagConstraints;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.XMPPConnection;

public class Login extends AbstractXMPPAction {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String RESOURCE = "resource";
    private JTextField login;
    private JTextField password;
    private JTextField resource;

    @Override
    public String getLabel() {
        return "Log In";
    }

    @Override
    public void addUI(JComponent panel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        addToPanel(panel, labelConstraints, 0, 0, new JLabel("Username: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 0, login = new JTextField(20));

        addToPanel(panel, labelConstraints, 0, 1, new JLabel("Password: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 1, password = new JTextField(20));

        addToPanel(panel, labelConstraints, 0, 2, new JLabel("Resource: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 2, resource = new JTextField(20));
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        XMPPConnection conn = sampler.getXMPPConnection();
        String loginStr = sampler.getPropertyAsString(LOGIN);
        String pwdStr = sampler.getPropertyAsString(PASSWORD);
        String resStr = sampler.getPropertyAsString(RESOURCE);
        res.setSamplerData("Username: " + loginStr + "\nPassword: " + pwdStr + "\nResource: " + resStr);
        if (loginStr.isEmpty()) {
            conn.loginAnonymously();
        } else {
            conn.login(loginStr, pwdStr, resStr);
        }
        return res;
    }

    @Override
    public void clearGui() {
        login.setText("");
        password.setText("");
        resource.setText("JMeter");
    }

    @Override
    public void setSamplerProperties(JMeterXMPPSampler sampler) {
        sampler.setProperty(LOGIN, login.getText());
        sampler.setProperty(PASSWORD, password.getText());
        sampler.setProperty(RESOURCE, resource.getText());
    }

    @Override
    public void setGuiFieldsFromSampler(JMeterXMPPSampler sampler) {
        login.setText(sampler.getPropertyAsString(LOGIN));
        password.setText(sampler.getPropertyAsString(PASSWORD));
        resource.setText(sampler.getPropertyAsString(RESOURCE));
    }

}
