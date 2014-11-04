package com.googlecode.jmeter.plugins.webdriver.config.gui;

import java.awt.TrayIcon.MessageType;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import com.googlecode.jmeter.plugins.webdriver.config.RemoteCapability;
import com.googlecode.jmeter.plugins.webdriver.config.RemoteDriverConfig;

public class RemoteDriverConfigGui extends WebDriverConfigGui implements ItemListener, FocusListener {

    private static final long serialVersionUID = 100L;
    static final String OVERRIDEN_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A4449d Safari/9537.53";
    JTextField userAgentOverrideText;
    JTextField remoteSeleniumGridText;
    JCheckBox userAgentOverrideCheckbox;
    JComboBox capabilitiesComboBox;
    MessageDialog messageDialog = new MessageDialog();

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
            config.setCapability((RemoteCapability)capabilitiesComboBox.getSelectedItem());
            if(userAgentOverrideCheckbox.isSelected()) {
                config.setUserAgentOverride(userAgentOverrideText.getText());
            }
        }
    }

    private JPanel createProfilePanel() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel remotePanel = new VerticalPanel();
        final JLabel remoteUrlLabel = new JLabel();
        final JLabel capabilitiesLabel = new JLabel();
        
        
        userAgentOverrideCheckbox = new JCheckBox("Override User Agent");
        userAgentOverrideCheckbox.setSelected(false);
        userAgentOverrideCheckbox.setEnabled(true);
        userAgentOverrideCheckbox.addItemListener(this);
        remotePanel.add(userAgentOverrideCheckbox);

        userAgentOverrideText = new JTextField(OVERRIDEN_USER_AGENT);
        userAgentOverrideText.setEnabled(false);
        remotePanel.add(userAgentOverrideText);
        
        remoteUrlLabel.setText("Selenium Grid URL");
        remoteSeleniumGridText = new JTextField();
        remoteSeleniumGridText.setEnabled(true);
        remoteSeleniumGridText.addFocusListener(this);
        capabilitiesLabel.setText("Capability");
        capabilitiesComboBox = new JComboBox(RemoteCapability.values());
        
        remotePanel.add(remoteUrlLabel);
        remotePanel.add(remoteSeleniumGridText);
        remotePanel.add(capabilitiesLabel);
        remotePanel.add(capabilitiesComboBox);
        
        browserPanel.add(remotePanel);
        
        return browserPanel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        remoteSeleniumGridText.setText(StringUtils.EMPTY);
        capabilitiesComboBox.setSelectedIndex(2);
        userAgentOverrideCheckbox.setSelected(false);
        userAgentOverrideText.setText(OVERRIDEN_USER_AGENT);
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource() == userAgentOverrideCheckbox) {
            userAgentOverrideText.setEnabled(userAgentOverrideCheckbox.isSelected());
        }
    }

	@Override
	public void focusGained(FocusEvent e) {
		// Nothing to do
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(remoteSeleniumGridText.equals(e.getComponent()) && !isValidUrl(remoteSeleniumGridText.getText())){
			messageDialog.show(this, "The selenium grid URL is malformed", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean isValidUrl(String urlStr) {
		try {
		    new URL(urlStr);
		    return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}
}