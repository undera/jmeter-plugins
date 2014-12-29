package kg.apc.jmeter.config;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class LockFileGui extends AbstractConfigGui {

    public static final String WIKIPAGE = "LockFile";
    public static Logger log = LoggingManager.getLoggerForClass();
    private JTextField tfFileName;
    private JTextField tfFileMask;

    public LockFileGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Lock File Config");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement te) {
        log.debug("[Lockfile plugin] configure");
        super.configure(te);
        LockFile lf = (LockFile) te;
        tfFileName.setText(lf.getFilename());
        tfFileMask.setText(lf.getFilemask());
    }

    @Override
    public TestElement createTestElement() {
        log.debug("[Lockfile plugin] createTestElement");
        LockFile lockFile = new LockFile();
        modifyTestElement(lockFile);
        lockFile.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return lockFile;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        log.debug("[Lockfile plugin] modifyTestElement");
        configureTestElement(te);
        LockFile lf = (LockFile) te;
        lf.setFilename(tfFileName.getText());
        lf.setFilemask(tfFileMask.getText());
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void init() {
        log.debug("[Lockfile plugin] init");
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Lock file name: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, tfFileName = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Also check filemask: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, tfFileMask = new JTextField(20));

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
        log.debug("[Lockfile plugin] initFields");
        tfFileName.setText("");
        tfFileMask.setText("");
    }
}
