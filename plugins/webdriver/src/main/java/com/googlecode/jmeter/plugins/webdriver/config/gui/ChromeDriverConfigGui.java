package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.ChromeDriverConfig;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

public class ChromeDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 100L;
    JTextField chromeServicePath;
    JCheckBox androidEnabled;
    private JCheckBox headlessEnabled;

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Chrome Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if(element instanceof ChromeDriverConfig) {
            ChromeDriverConfig config = (ChromeDriverConfig)element;
            chromeServicePath.setText(config.getChromeDriverPath());
            androidEnabled.setSelected(config.isAndroidEnabled());
            getHeadlessEnabled().setSelected(config.isHeadlessEnabled());
        }
    }

    @Override
    public TestElement createTestElement() {
        ChromeDriverConfig element = new ChromeDriverConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if(element instanceof ChromeDriverConfig) {
            ChromeDriverConfig config = (ChromeDriverConfig)element;
            config.setChromeDriverPath(chromeServicePath.getText());
            config.setAndroidEnabled(androidEnabled.isSelected());
            config.setHeadlessEnabled(getHeadlessEnabled().isSelected());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        chromeServicePath.setText("");
        androidEnabled.setSelected(false);
        getHeadlessEnabled().setSelected(false);
    }

    @Override
    protected JPanel createBrowserPanel() {
        return createServicePanel();
    }

    @Override
    protected String browserName() {
        return "Chrome";
    }

    @Override
    protected String getWikiPage() {
        return "ChromeDriverConfig";
    }

    private JPanel createServicePanel() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel chromeServicePanel = new HorizontalPanel();
        final JLabel chromeDriverServiceLabel = new JLabel("Path to Chrome Driver");
        chromeServicePanel.add(chromeDriverServiceLabel);

        chromeServicePath = new JTextField();
        chromeServicePanel.add(chromeServicePath);
        browserPanel.add(chromeServicePanel);

        androidEnabled = new JCheckBox("Use Chrome on Android");
        browserPanel.add(androidEnabled);

        headlessEnabled = new JCheckBox("Use Chrome headless mode");
        browserPanel.add(getHeadlessEnabled());
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

    public JCheckBox getHeadlessEnabled() {
        return headlessEnabled;
    }
}
