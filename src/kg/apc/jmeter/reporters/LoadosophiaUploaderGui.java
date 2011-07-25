package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

/**
 *
 * @author undera
 */
public class LoadosophiaUploaderGui extends AbstractListenerGui {

    public static final String DEFAULT_UPLOADER_URI = "https://loadosophia.org/uploader/";
    public static final String WIKIPAGE = "LoadosophiaUploader";
    private JTextField filePrefix;
    private JTextArea uploadToken;
    private JTextField projectKey;

    public LoadosophiaUploaderGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Loadosophia.org Uploader");
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    public TestElement createTestElement() {
        TestElement te = new LoadosophiaUploader();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
        if (te instanceof LoadosophiaUploader) {
            LoadosophiaUploader fw = (LoadosophiaUploader) te;
            fw.setFilePrefix(filePrefix.getText());
            fw.setProject(projectKey.getText());
            fw.setUploadToken(uploadToken.getText());
            fw.setUploaderURI(JMeterUtils.getPropDefault("loadosophia.uploaderURI", DEFAULT_UPLOADER_URI));
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        LoadosophiaUploader fw = (LoadosophiaUploader) element;
        filePrefix.setText(fw.getFilePrefix());
        projectKey.setText(fw.getProject());
        uploadToken.setText(fw.getUploadToken());
    }

    private void init() {
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

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Filename Prefix: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, filePrefix = new JTextField());

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Upload to Project: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, projectKey = new JTextField());

        editConstraints.fill = GridBagConstraints.BOTH;
        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Upload Token: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, uploadToken = new JTextArea());
        uploadToken.setRows(10);
        uploadToken.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void initFields() {
        Object root = GuiPackage.getInstance().getTreeModel().getRoot();
        if (root instanceof TestElement) {
            filePrefix.setText(((TestElement) root).getName().replaceAll("\\s", "_"));
        } else {
            filePrefix.setText("TestPlan");
        }

        projectKey.setText("DEFAULT");
        uploadToken.setText("Replace this text with upload token received at Loadosophia.org\nRemember that anyone who has this token can upload files to your account. Please, treat your token as confidential data.\nSee plugin help for details.");
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }
}
