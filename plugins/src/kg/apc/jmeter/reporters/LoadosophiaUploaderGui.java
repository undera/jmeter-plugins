package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.BrowseAction;
import kg.apc.jmeter.gui.GuiBuilderHelper;
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
    private JButton browseButton;

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
        addToPanel(mainPanel, editConstraints, 1, 0, projectKey = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Directory to store data for upload: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, storeDir = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 2, 1, browseButton = new JButton("Browse..."));

        GuiBuilderHelper.strechButtonToComponent(storeDir, browseButton);
        browseButton.addActionListener(new BrowseAction(storeDir, true));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Filename Prefix: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, filePrefix = new JTextField(20));

        editConstraints.fill = GridBagConstraints.BOTH;

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Upload Token: ", JLabel.RIGHT));

        uploadToken = new JTextArea();
        uploadToken.setLineWrap(true);
        addToPanel(mainPanel, editConstraints, 1, 3, GuiBuilderHelper.getTextAreaScrollPaneContainer(uploadToken, 6));

        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Info Area: ", JLabel.RIGHT));
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setOpaque(false);
        
        addToPanel(mainPanel, editConstraints, 1, 4, GuiBuilderHelper.getTextAreaScrollPaneContainer(infoArea, 6));

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
            f.deleteOnExit();
        } catch (IOException ex) {
            return "";
        }
        return f.getParent();
    }
}
