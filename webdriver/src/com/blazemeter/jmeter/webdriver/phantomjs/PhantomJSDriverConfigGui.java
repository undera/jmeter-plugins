package com.blazemeter.jmeter.webdriver.phantomjs;

import com.googlecode.jmeter.plugins.webdriver.config.gui.WebDriverConfigGui;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

public class PhantomJSDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 100L;
    JTextField phantomJsExecutablePath;

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("PhantomJS Driver Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof PhantomJSDriverConfig) {
            PhantomJSDriverConfig config = (PhantomJSDriverConfig) element;
            phantomJsExecutablePath.setText(config.getPhantomJsExecutablePath());
        }
    }

    @Override
    public TestElement createTestElement() {
        PhantomJSDriverConfig element = new PhantomJSDriverConfig();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        if (element instanceof PhantomJSDriverConfig) {
            PhantomJSDriverConfig config = (PhantomJSDriverConfig) element;
            config.setPhantomJsExecutablePath(phantomJsExecutablePath.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        phantomJsExecutablePath.setText("");
    }

    @Override
    protected JPanel createBrowserPanel() {
        return buildGui();
    }

    @Override
    protected String browserName() {
        return "PhantomJS";
    }

    @Override
    protected String getWikiPage() {
        return "PhantomJSDriverConfig";
    }

    @Override
    protected boolean isProxyEnabled() {
        return true;
    }

    @Override
    protected boolean isExperimentalEnabled() {
        return true;
    }

    private JPanel buildGui() {
        final JPanel browserPanel = new VerticalPanel();
        final JPanel phantomJsExecutablePanel = new HorizontalPanel();
        final JLabel phantomJsExecutableLabel = new JLabel("Path to PhantomJS executable");
        phantomJsExecutablePanel.add(phantomJsExecutableLabel);

        phantomJsExecutablePath = new JTextField();
        phantomJsExecutablePanel.add(phantomJsExecutablePath);
        browserPanel.add(phantomJsExecutablePanel);
        return browserPanel;
    }

}
