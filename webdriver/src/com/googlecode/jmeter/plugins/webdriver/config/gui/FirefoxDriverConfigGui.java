package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.FirefoxDriverConfig;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.Grid;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;

public class FirefoxDriverConfigGui extends WebDriverConfigGui implements ItemListener {

    private static final long serialVersionUID = 100L;
    static final String OVERRIDEN_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A4449d Safari/9537.53";
    static final String OVERRIDEN_NTLM = "";
    JTextField userAgentOverrideText;
    JCheckBox userAgentOverrideCheckbox;
    JCheckBox ntlmOverrideCheckbox;
    private Grid extensions;
    private Grid preferences;

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

            JMeterProperty ext = config.getExtensions();
            if (!(ext instanceof NullProperty)) {
                JMeterPluginsUtils.collectionPropertyToTableModelRows((CollectionProperty) ext, extensions.getModel());
            }

            JMeterProperty pref = config.getPreferences();
            if (!(ext instanceof NullProperty)) {
                JMeterPluginsUtils.collectionPropertyToTableModelRows((CollectionProperty) pref, preferences.getModel());
            }
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if (element instanceof FirefoxDriverConfig) {
            FirefoxDriverConfig config = (FirefoxDriverConfig) element;
            config.setUserAgentOverridden(userAgentOverrideCheckbox.isSelected());
            config.setNtlmSetting(ntlmOverrideCheckbox.isSelected());
            if (userAgentOverrideCheckbox.isSelected()) {
                config.setUserAgentOverride(userAgentOverrideText.getText());
            }
            config.setExtensions(extensions.getModel());
            config.setPreferences(preferences.getModel());
        }
    }

    private JPanel createProfilePanel() {
        final JPanel firefoxPanel = new VerticalPanel();
        userAgentOverrideCheckbox = new JCheckBox("Override User Agent");
        userAgentOverrideCheckbox.setSelected(false);
        userAgentOverrideCheckbox.setEnabled(true);
        userAgentOverrideCheckbox.addItemListener(this);
        firefoxPanel.add(userAgentOverrideCheckbox);

        ntlmOverrideCheckbox = new JCheckBox("Enable NTLM");
        ntlmOverrideCheckbox.setSelected(false);
        ntlmOverrideCheckbox.setEnabled(true);
        ntlmOverrideCheckbox.addItemListener(this);
        firefoxPanel.add(ntlmOverrideCheckbox);

        userAgentOverrideText = new JTextField(OVERRIDEN_USER_AGENT);
        userAgentOverrideText.setEnabled(false);
        firefoxPanel.add(userAgentOverrideText);

        extensions = new Grid("Load Extensions", new String[]{"Path to XPI File"}, new Class[]{String.class}, new String[]{""});
        firefoxPanel.add(extensions);

        preferences = new Grid("Set Preferences", new String[]{"Name", "Value"}, new Class[]{String.class, String.class}, new String[]{"", ""});
        firefoxPanel.add(preferences);

        final JPanel browserPanel = new VerticalPanel();
        browserPanel.add(firefoxPanel);
        return browserPanel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        userAgentOverrideCheckbox.setSelected(false);
        userAgentOverrideText.setText(OVERRIDEN_USER_AGENT);
        ntlmOverrideCheckbox.setSelected(false);
        extensions.getModel().clearData();
        preferences.getModel().clearData();
    }

    public void itemStateChangedUserAgent(ItemEvent itemEvent) {
        if (itemEvent.getSource() == userAgentOverrideCheckbox) {
            userAgentOverrideText.setEnabled(userAgentOverrideCheckbox.isSelected());
        }
    }

    @Override
    protected boolean isProxyEnabled() {
        return true;
    }

    @Override
    protected boolean isExperimentalEnabled() {
        return true;
    }

}