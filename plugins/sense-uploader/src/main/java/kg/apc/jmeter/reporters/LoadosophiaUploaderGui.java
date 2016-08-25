package kg.apc.jmeter.reporters;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.BrowseAction;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.LoadosophiaAPIClient;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class LoadosophiaUploaderGui extends AbstractVisualizer implements HyperlinkListener { // FIXME: Why Visualizer? We've grownups now!
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String WIKIPAGE = "LoadosophiaUploader";
    private JTextField testTitle;
    private JTextArea uploadToken;
    private JTextField projectKey;
    private JTextPane infoArea;
    private JTextField storeDir;
    private JComboBox<String> colorFlag;
    private JCheckBox useOnline;
    private String infoText = "";

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
        return "bzm - BlazeMeter Sense Uploader";
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
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Test Title: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, testTitle = new JTextField(20));

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Color Flag: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, colorFlag = new JComboBox<>(LoadosophiaAPIClient.colors));

        editConstraints.fill = GridBagConstraints.BOTH;

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Upload Token: ", JLabel.RIGHT));

        uploadToken = new JTextArea();
        uploadToken.setLineWrap(true);
        addToPanel(mainPanel, editConstraints, 1, row, GuiBuilderHelper.getTextAreaScrollPaneContainer(uploadToken, 6));

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Temp directory: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, storeDir = new JTextField(20));
        JButton browseButton;
        addToPanel(mainPanel, labelConstraints, 2, row, browseButton = new JButton("Browse..."));

        GuiBuilderHelper.strechItemToComponent(storeDir, browseButton);
        browseButton.addActionListener(new BrowseAction(storeDir, true));

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Info Area: ", JLabel.RIGHT));
        infoArea = new JTextPane();
        infoArea.setEditable(false);
        infoArea.setOpaque(false);
        infoArea.setContentType("text/html");
        infoArea.addHyperlinkListener(this);

        GuiBuilderHelper.strechItemToComponent(storeDir, infoArea);

        JScrollPane ret = new JScrollPane();
        ret.setViewportView(infoArea);
        addToPanel(mainPanel, editConstraints, 1, row, ret);

        ret.setMinimumSize(new Dimension(0, 200));
        ret.setPreferredSize(new Dimension(0, 200));
        ret.setSize(new Dimension(0, 200));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void initFields() {
        testTitle.setText("");
        projectKey.setText("DEFAULT");
        uploadToken.setText("Replace this text with upload token received at sense.blazemeter.com\nRemember that anyone who has this token can upload files to your account.\nPlease, treat your token as confidential data.\nSee plugin help for details.");
        storeDir.setText("");
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
    public void clearData() {
        infoText = "";
        infoArea.setText("");
    }

    public void inform(String string) {
        infoText += string + "<br/>\n";
        infoArea.setText(infoText);
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

    @Override
    public void add(SampleResult sample) {

    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            openInBrowser(e.getURL().toString());
        }
    }

    public static void openInBrowser(String string) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                java.awt.Desktop.getDesktop().browse(new URI(string));
            } catch (IOException | URISyntaxException ignored) {
                log.debug("Failed to open in browser", ignored);
            }
        }
    }
}
