package kg.apc.jmeter.reporters;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.BrowseAction;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.loadosophia.jmeter.LoadosophiaAPIClient;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoadosophiaUploaderGui
        extends AbstractVisualizer {

    public static final String WIKIPAGE = "LoadosophiaUploader";
    private JTextField testTitle;
    private JTextArea uploadToken;
    private JTextField projectKey;
    private JTextArea infoArea;
    private JTextField storeDir;
    private JComboBox colorFlag;
    private JCheckBox useOnline;

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
            fw.setProject(projectKey.getText());
            fw.setUploadToken(uploadToken.getText());
            fw.setStoreDir(storeDir.getText());
            fw.setColorFlag(indexToColor(colorFlag.getSelectedIndex()));
            fw.setTitle(testTitle.getText());
            fw.setUseOnline(useOnline.isSelected());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        LoadosophiaUploader fw = (LoadosophiaUploader) element;
        projectKey.setText(fw.getProject());
        uploadToken.setText(fw.getUploadToken());
        storeDir.setText(fw.getStoreDir());
        colorFlag.setSelectedIndex(colorToIndex(fw.getColorFlag()));
        testTitle.setText(fw.getTitle());
        useOnline.setSelected(fw.isUseOnline());
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

        int row = 0;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Initiate active test: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, useOnline = new JCheckBox());

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Upload to Project: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, projectKey = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Directory to store data for upload: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, storeDir = new JTextField(20));
        JButton browseButton;
        addToPanel(mainPanel, labelConstraints, 2, row, browseButton = new JButton("Browse..."));

        GuiBuilderHelper.strechItemToComponent(storeDir, browseButton);
        browseButton.addActionListener(new BrowseAction(storeDir, true));

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Test Title: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, testTitle = new JTextField(20));

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Color Flag: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, colorFlag = new JComboBox(LoadosophiaAPIClient.colors));

        GuiBuilderHelper.strechItemToComponent(storeDir, colorFlag);

        editConstraints.fill = GridBagConstraints.BOTH;

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Upload Token: ", JLabel.RIGHT));

        uploadToken = new JTextArea();
        uploadToken.setLineWrap(true);
        addToPanel(mainPanel, editConstraints, 1, row, GuiBuilderHelper.getTextAreaScrollPaneContainer(uploadToken, 6));

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Info Area: ", JLabel.RIGHT));
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setOpaque(false);

        addToPanel(mainPanel, editConstraints, 1, row, GuiBuilderHelper.getTextAreaScrollPaneContainer(infoArea, 6));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void initFields() {
        testTitle.setText("");
        projectKey.setText("DEFAULT");
        uploadToken.setText("Replace this text with upload token received at Loadosophia.org\nRemember that anyone who has this token can upload files to your account.\nPlease, treat your token as confidential data.\nSee plugin help for details.");
        storeDir.setText(System.getProperty("java.io.tmpdir"));
        colorFlag.setSelectedIndex(0);
        useOnline.setSelected(true);
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

    private String indexToColor(int selectedIndex) {
        if (selectedIndex >= 0) {
            return LoadosophiaAPIClient.colors[selectedIndex];
        } else {
            return LoadosophiaAPIClient.COLOR_NONE;
        }
    }

    private int colorToIndex(String colorFlag) {
        return Arrays.asList(LoadosophiaAPIClient.colors).indexOf(colorFlag);
    }
}
