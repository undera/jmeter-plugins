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
    JTextField chromeServiceArgs;

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
            chromeServiceArgs.setText(config.getChromeDriverArgs());
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
            config.setChromeDriverArgs(chromeServiceArgs.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        chromeServicePath.setText("");
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
        final JLabel chromeDriverServiceArgsLabel = new JLabel("Arguments of Chrome Driver");
        chromeServicePanel.add(chromeDriverServiceArgsLabel);

        chromeServiceArgs = new JTextField();
        chromeServicePanel.add(chromeServiceArgs);

        browserPanel.add(chromeServicePanel);
        return browserPanel;
    }

}
