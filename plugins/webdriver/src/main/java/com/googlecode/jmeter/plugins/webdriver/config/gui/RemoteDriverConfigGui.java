package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.FileDetectorOption;
import com.googlecode.jmeter.plugins.webdriver.config.RemoteCapability;
import com.googlecode.jmeter.plugins.webdriver.config.RemoteDriverConfig;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteDriverConfigGui extends WebDriverConfigGui implements ItemListener, FocusListener {

    private static final long serialVersionUID = 100L;
    JTextField remoteSeleniumGridText;
    JComboBox capabilitiesComboBox;
    JLabel errorMsg;

    private JComboBox fileDetectorsComboBox;

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
            remoteSeleniumGridText.setText(config.getSeleniumGridUrl());
            capabilitiesComboBox.setSelectedItem(config.getCapability());
            fileDetectorsComboBox.setSelectedItem(config.getFileDetectorOption());
        }
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if (element instanceof RemoteDriverConfig) {
        	RemoteDriverConfig config = (RemoteDriverConfig) element;
            config.setSeleniumGridUrl(remoteSeleniumGridText.getText());
            config.setCapability((RemoteCapability)capabilitiesComboBox.getSelectedItem());
            config.setFileDetectorOption((FileDetectorOption)fileDetectorsComboBox.getSelectedItem());
        }
    }

    private JPanel createProfilePanel() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel remotePanel = new VerticalPanel();
        final JLabel remoteUrlLabel = new JLabel();
        final JLabel capabilitiesLabel = new JLabel();
        final JLabel fileDetectorLabel = new JLabel();

        remoteUrlLabel.setText("Selenium Grid URL");
        remoteSeleniumGridText = new JTextField();
        remoteSeleniumGridText.setEnabled(true);
        remoteSeleniumGridText.addFocusListener(this);

        capabilitiesLabel.setText("Capability");
        capabilitiesComboBox = new JComboBox(RemoteCapability.values());

        fileDetectorLabel.setText("File detector");
        fileDetectorsComboBox = new JComboBox(FileDetectorOption.values());

        remotePanel.add(remoteUrlLabel);
        remotePanel.add(remoteSeleniumGridText);
        remotePanel.add(errorMsg=new JLabel());
        remotePanel.add(capabilitiesLabel);
        remotePanel.add(capabilitiesComboBox);
        remotePanel.add(fileDetectorLabel);
        remotePanel.add(fileDetectorsComboBox);

        errorMsg.setForeground(Color.red);
        
        browserPanel.add(remotePanel);
        
        return browserPanel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        remoteSeleniumGridText.setText(StringUtils.EMPTY);
        capabilitiesComboBox.setSelectedIndex(2);
        fileDetectorsComboBox.setSelectedIndex(1);
    }

	@Override
	public void focusGained(FocusEvent e) {
		// Nothing to do
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(remoteSeleniumGridText.equals(e.getComponent()) && !isValidUrl(remoteSeleniumGridText.getText())){
			errorMsg.setText("The selenium grid URL is malformed");
		} else {
            errorMsg.setText("");
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

	@Override
	protected boolean isProxyEnabled() {
		return false;
	}

	@Override
	protected boolean isExperimentalEnabled() {
		return false;
	}
}