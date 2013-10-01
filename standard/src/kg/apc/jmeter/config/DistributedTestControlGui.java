package kg.apc.jmeter.config;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;

public class DistributedTestControlGui extends AbstractConfigGui {

    public static final String WIKIPAGE = "DistributedTestControl";
    public static Logger log = LoggingManager.getLoggerForClass();
    private ServersListPanel mainPanel;

    public DistributedTestControlGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Distributed Test Control");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement te) {
        super.configure(te);
        // fill controls
        mainPanel.loadFromTestElement((DistributedTestControl) te);
    }

    @Override
    public TestElement createTestElement() {
        DistributedTestControl lockFile = new DistributedTestControl();
        modifyTestElement(lockFile);
        return lockFile;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        log.debug("[Lockfile plugin] modifyTestElement");
        configureTestElement(te);
        // fill props
        mainPanel.saveToTestElement((DistributedTestControl) te);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        mainPanel=new ServersListPanel();

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    private void initFields() {
        mainPanel.clear();
    }
}
