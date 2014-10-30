package com.googlecode.jmeter.plugins.webdriver.config.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import com.googlecode.jmeter.plugins.webdriver.config.RemoteDriverConfig;

public class RemoteDriverConfigGui extends WebDriverConfigGui implements ItemListener {

    private static final long serialVersionUID = 100L;
    static final String OVERRIDEN_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A4449d Safari/9537.53";
    JTextField userAgentOverrideText;
    JTextField remoteSeleniumGridText;
    JCheckBox userAgentOverrideCheckbox;

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Remote Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    protected JPanel createBrowserPanel() {
        return createProfilePanel();
    }

    @Override
    protected String browserName() {
        return "Remote";
    }

    @Override
    protected String getWikiPage() {
        return "RemoteDriverConfig";
    }

    @Override
    public TestElement createTestElement() {
        RemoteDriverConfig element = new RemoteDriverConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof RemoteDriverConfig) {
        	RemoteDriverConfig config = (RemoteDriverConfig) element;
            userAgentOverrideCheckbox.setSelected(config.isUserAgentOverridden());
            userAgentOverrideText.setText(config.getUserAgentOverride());
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if (element instanceof RemoteDriverConfig) {
        	RemoteDriverConfig config = (RemoteDriverConfig) element;
            config.setUserAgentOverridden(userAgentOverrideCheckbox.isSelected());
            config.setSeleniumGridUrl(remoteSeleniumGridText.getText());
            if(userAgentOverrideCheckbox.isSelected()) {
                config.setUserAgentOverride(userAgentOverrideText.getText());
            }
        }
    }

    private JPanel createProfilePanel() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel remotePanel = new VerticalPanel();
        final JLabel remoteUrlLabel = new JLabel();
        
        
        userAgentOverrideCheckbox = new JCheckBox("Override User Agent");
        userAgentOverrideCheckbox.setSelected(false);
        userAgentOverrideCheckbox.setEnabled(true);
        userAgentOverrideCheckbox.addItemListener(this);
        remotePanel.add(userAgentOverrideCheckbox);

        userAgentOverrideText = new JTextField(OVERRIDEN_USER_AGENT);
        userAgentOverrideText.setEnabled(false);
        remotePanel.add(userAgentOverrideText);
        
        remoteUrlLabel.setText("Remote Selenium Grid URL");
        remoteSeleniumGridText = new JTextField();
        remoteSeleniumGridText.setEnabled(true);
        remotePanel.add(remoteUrlLabel);
        remotePanel.add(remoteSeleniumGridText);
        
        browserPanel.add(remotePanel);
        
        return browserPanel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        remoteSeleniumGridText.setText(StringUtils.EMPTY);
        userAgentOverrideCheckbox.setSelected(false);
        userAgentOverrideText.setText(OVERRIDEN_USER_AGENT);
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource() == userAgentOverrideCheckbox) {
            userAgentOverrideText.setEnabled(userAgentOverrideCheckbox.isSelected());
        }
    }
}