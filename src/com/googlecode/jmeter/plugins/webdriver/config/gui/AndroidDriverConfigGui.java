package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.AndroidDriverConfig;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

/**
 * @author Sergey Marakhov
 * @author Linh Pham
 */
public class AndroidDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 100L;

    private static final String DEFAULT_ANDROID_DRIVER_HOST_PORT = "8080";
    
    JTextField androidDriverHostPort;
    
    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Android Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    protected JPanel createBrowserPanel() {
        return createAndroidDriverHostPortPanel();
    }
    
    @Override
    protected String browserName() {
        return "Android";
    }

    @Override
    protected String getWikiPage() {
        return "AndroidDriverConfig";
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if(element instanceof AndroidDriverConfig) {
            AndroidDriverConfig config = (AndroidDriverConfig) element;
            androidDriverHostPort.setText(config.getAndroidDriverHostPort());
        }
    }

    @Override
    public TestElement createTestElement() {
        AndroidDriverConfig element = new AndroidDriverConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if(element instanceof AndroidDriverConfig) {
            AndroidDriverConfig config = (AndroidDriverConfig)element;
            config.setAndroidDriverHostPort(androidDriverHostPort.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        androidDriverHostPort.setText(DEFAULT_ANDROID_DRIVER_HOST_PORT);
    }

    private JPanel createAndroidDriverHostPortPanel() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel androidDriverHostPortPanel = new HorizontalPanel();
        final JLabel androidDriverHostPortLabel = new JLabel("Android driver host port:");
        androidDriverHostPortPanel.add(androidDriverHostPortLabel);

        androidDriverHostPort = new JTextField();
        androidDriverHostPort.setText(DEFAULT_ANDROID_DRIVER_HOST_PORT);

        androidDriverHostPortPanel.add(androidDriverHostPort);
        browserPanel.add(androidDriverHostPortPanel);
        return browserPanel;
    }
}
