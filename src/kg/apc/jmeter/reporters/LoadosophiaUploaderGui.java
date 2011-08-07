package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;

/**
 *
 * @author undera
 */
public class LoadosophiaUploaderGui
        extends AbstractVisualizer {

    public static final String DEFAULT_UPLOADER_URI = "https://loadosophia.org/uploader/";
    public static final String WIKIPAGE = "LoadosophiaUploader";
    private JTextField filePrefix;
    private JTextArea uploadToken;
    private JTextField projectKey;
    private JTextArea infoArea;
    private JTextField storeDir;

    public LoadosophiaUploaderGui() {
        super();
        init();
        initFields();
    }

    @Override
    protected Component getFilePanel() {
        return new JPanel();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Loadosophia.org Uploader");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public TestElement createTestElement() {
        TestElement te = new LoadosophiaUploader();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        super.modifyTestElement(te);
        if (te instanceof LoadosophiaUploader) {
            LoadosophiaUploader fw = (LoadosophiaUploader) te;
            fw.setFilePrefix(filePrefix.getText());
            fw.setProject(projectKey.getText());
            fw.setUploadToken(uploadToken.getText());
            fw.setUploaderURI(JMeterUtils.getPropDefault("loadosophia.uploaderURI", DEFAULT_UPLOADER_URI));
            fw.setStoreDir(storeDir.getText());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        LoadosophiaUploader fw = (LoadosophiaUploader) element;
        filePrefix.setText(fw.getFilePrefix());
        projectKey.setText(fw.getProject());
        uploadToken.setText(fw.getUploadToken());
        storeDir.setText(fw.getStoreDir());
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

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Upload to Project: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, projectKey = new JTextField());

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Directory to store data for upload: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, storeDir = new JTextField());

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Filename Prefix: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, filePrefix = new JTextField());

        editConstraints.fill = GridBagConstraints.BOTH;
        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Upload Token: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, uploadToken = new JTextArea());
        uploadToken.setRows(5);
        uploadToken.setBorder(new BevelBorder(BevelBorder.LOWERED));

        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Info Area: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, infoArea = new JTextArea());
        infoArea.setRows(5);
        infoArea.setBorder(new BevelBorder(BevelBorder.LOWERED));
        infoArea.setEditable(false);

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
        uploadToken.setText("Replace this text with upload token received at Loadosophia.org\nRemember that anyone who has this token can upload files to your account.\nPlease, treat your token as confidential data.\nSee plugin help for details.");
        storeDir.setText(getTempDir());
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    @Override
    public void add(SampleResult sr) {
    }

    @Override
    public void clearData() {
        infoArea.setText("");
    }

    public void inform(String string) {
        infoArea.append(string + "\n");
    }

    @Override
    public boolean isStats() {
        return false;
    }

    private String getTempDir() {
        File f = null;
        try {
            f = File.createTempFile("jmeterplugins", ".tmp");
        } catch (IOException ex) {
            return "";
        }
        return f.getParent();
    }
}
