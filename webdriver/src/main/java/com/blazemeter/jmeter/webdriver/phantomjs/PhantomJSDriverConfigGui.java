package com.blazemeter.jmeter.webdriver.phantomjs;

import com.googlecode.jmeter.plugins.webdriver.config.gui.WebDriverConfigGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

public class PhantomJSDriverConfigGui extends WebDriverConfigGui {

    private static final long serialVersionUID = 100L;
    JTextField phantomJsExecutablePath;
    JTextField phantomJsCliArgs;
    JTextField phantomJsGhostdriverCliArgs;

    @Override
    public String getStaticLabel() {
        return "bzm - PhantomJS Driver Config";
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
            phantomJsCliArgs.setText(config.getPhantomJsCliArgs());
            phantomJsGhostdriverCliArgs.setText(config.getPhantomJsGhostdriverCliArgs());
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
            config.setPhantomJsCliArgs(phantomJsCliArgs.getText());
            config.setPhantomJsGhostdriverCliArgs(phantomJsGhostdriverCliArgs.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        phantomJsExecutablePath.setText("");
        phantomJsCliArgs.setText("");
        phantomJsGhostdriverCliArgs.setText("");
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
        
        final JPanel phantomJsCliArgsPanel = new VerticalPanel();
        final JLabel phantomJsCliArgsLabel = new JLabel("value for phantomjs.cli.args (comma separator) likes --web-security=false, --ignore-ssl-errors=true");
        phantomJsCliArgsPanel.add(phantomJsCliArgsLabel);
        
        phantomJsCliArgs = new JTextField();
        phantomJsCliArgsPanel.add(phantomJsCliArgs);
        browserPanel.add(phantomJsCliArgsPanel);
        
        
        final JPanel phantomJsGhostdriverCliArgsPanel = new VerticalPanel();
        final JLabel phantomJsGhostdriverCliArgsLabel = new JLabel("value for phantomjs.ghostdriver.cli.args (comma separator)");
        phantomJsGhostdriverCliArgsPanel.add(phantomJsGhostdriverCliArgsLabel);
        
        phantomJsGhostdriverCliArgs = new JTextField();
        phantomJsGhostdriverCliArgsPanel.add(phantomJsGhostdriverCliArgs);
        browserPanel.add(phantomJsGhostdriverCliArgsPanel);
        return browserPanel;
    }

}
