package com.googlecode.jmeter.plugins.webdriver.config.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import com.googlecode.jmeter.plugins.webdriver.config.InternetExplorerDriverConfig;

public class InternetExplorerDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 100L;
    JTextField ieServicePath;

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Internet Explorer Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if(element instanceof InternetExplorerDriverConfig) {
            InternetExplorerDriverConfig config = (InternetExplorerDriverConfig)element;
            ieServicePath.setText(config.getInternetExplorerDriverPath());
        }
    }

    @Override
    public TestElement createTestElement() {
        InternetExplorerDriverConfig element = new InternetExplorerDriverConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if(element instanceof InternetExplorerDriverConfig) {
            InternetExplorerDriverConfig config = (InternetExplorerDriverConfig)element;
            config.setInternetExplorerDriverPath(ieServicePath.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        ieServicePath.setText("");
    }

    @Override
    protected JPanel createBrowserPanel() {
        return createServicePanel();
    }

    @Override
    protected String browserName() {
        return "Internet Explorer";
    }

    @Override
    protected String getWikiPage() {
        return "InternetExplorerConfig";
    }

    private JPanel createServicePanel() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel ieServicePanel = new HorizontalPanel();
        final JLabel ieDriverServiceLabel = new JLabel("Path to Internet Explorer Driver");
        ieServicePanel.add(ieDriverServiceLabel);

        ieServicePath = new JTextField();
        ieServicePanel.add(ieServicePath);
        browserPanel.add(ieServicePanel);
        return browserPanel;
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