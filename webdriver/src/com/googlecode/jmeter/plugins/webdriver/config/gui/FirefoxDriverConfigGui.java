package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.FirefoxDriverConfig;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class FirefoxDriverConfigGui extends WebDriverConfigGui implements ItemListener {

    private static final long serialVersionUID = 100L;
    static final String OVERRIDEN_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A4449d Safari/9537.53";
    JTextField userAgentOverrideText;
    JCheckBox userAgentOverrideCheckbox;

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Firefox Driver Config");
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
        return "Firefox";
    }

    @Override
    protected String getWikiPage() {
        return "FirefoxDriverConfig";
    }

    @Override
    public TestElement createTestElement() {
        FirefoxDriverConfig element = new FirefoxDriverConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof FirefoxDriverConfig) {
            FirefoxDriverConfig config = (FirefoxDriverConfig) element;
            userAgentOverrideCheckbox.setSelected(config.isUserAgentOverridden());
            userAgentOverrideText.setText(config.getUserAgentOverride());
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if (element instanceof FirefoxDriverConfig) {
            FirefoxDriverConfig config = (FirefoxDriverConfig) element;
            config.setUserAgentOverridden(userAgentOverrideCheckbox.isSelected());
            if(userAgentOverrideCheckbox.isSelected()) {
                config.setUserAgentOverride(userAgentOverrideText.getText());
            }
        }
    }

    private JPanel createProfilePanel() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel firefoxPanel = new VerticalPanel();
        userAgentOverrideCheckbox = new JCheckBox("Override User Agent");
        userAgentOverrideCheckbox.setSelected(false);
        userAgentOverrideCheckbox.setEnabled(true);
        userAgentOverrideCheckbox.addItemListener(this);
        firefoxPanel.add(userAgentOverrideCheckbox);

        userAgentOverrideText = new JTextField(OVERRIDEN_USER_AGENT);
        userAgentOverrideText.setEnabled(false);
        firefoxPanel.add(userAgentOverrideText);
        browserPanel.add(firefoxPanel);
        return browserPanel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
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