package com.blazemeter.jmeter;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlazemeterUploaderGui extends AbstractListenerGui implements HyperlinkListener {

    public static final String WIKIPAGE = "BlazemeterUploader";
    public static final String UPLOAD_TOKEN_PLACEHOLDER = "Replace this text with upload token received at a.blazemeter.com\nCan be used deprecated API keys or new improved keys.\nRemember that anyone who has this token can upload files to your account.\nPlease, treat your token as confidential data.\nSee plugin help for details.";

    private JCheckBox anonymousTest;
    private ActionListener actionListener;
    private JCheckBox shareTest;
    private JTextField testWorkspace;
    private JTextField projectKey;
    private JTextField testTitle;
    private JTextArea uploadToken;
    private JTextPane infoArea;
    private String infoText = "";

    public BlazemeterUploaderGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return "bzm - BlazeMeter Uploader";
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public TestElement createTestElement() {
        TestElement te = new BlazemeterUploader();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        if (te instanceof BlazemeterUploader) {
            BlazemeterUploader uploader = (BlazemeterUploader) te;
            uploader.setAnonymousTest(anonymousTest.isSelected());
            uploader.setShareTest(shareTest.isSelected());
            uploader.setTitle(testTitle.getText());
            uploader.setWorkspace(testWorkspace.getText());
            uploader.setProject(projectKey.getText());
            String token = uploadToken.getText();
            uploader.setUploadToken(UPLOAD_TOKEN_PLACEHOLDER.equals(token) ? "" : token);
            uploader.setGui(this);
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        BlazemeterUploader uploader = (BlazemeterUploader) element;
        anonymousTest.setSelected(uploader.isAnonymousTest());
        shareTest.setSelected(uploader.isShareTest());
        testWorkspace.setText(uploader.getWorkspace());
        projectKey.setText(uploader.getProject());
        testTitle.setText(uploader.getTitle());
        uploadToken.setText(uploader.getUploadToken());
        actionListener.actionPerformed(new ActionEvent(anonymousTest, 0, "select"));
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
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Anonymous test: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, anonymousTest = new JCheckBox());

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Share test: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, shareTest = new JCheckBox());

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Project Workspace: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, testWorkspace = new JTextField(20));

        editConstraints.fill = GridBagConstraints.BOTH;

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Upload to Project: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, projectKey = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Test Title: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, row, testTitle = new JTextField(20));

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Upload Token: ", JLabel.RIGHT));

        uploadToken = new JTextArea();
        uploadToken.setLineWrap(true);
        addToPanel(mainPanel, editConstraints, 1, row, GuiBuilderHelper.getTextAreaScrollPaneContainer(uploadToken, 6));

        row++;
        addToPanel(mainPanel, labelConstraints, 0, row, new JLabel("Info Area: ", JLabel.RIGHT));
        infoArea = new JTextPane();
        infoArea.setEditable(false);
        infoArea.setOpaque(false);
        infoArea.setContentType("text/html");
        infoArea.addHyperlinkListener(this);

        JScrollPane ret = new JScrollPane();
        ret.setViewportView(infoArea);
        addToPanel(mainPanel, editConstraints, 1, row, ret);

        ret.setMinimumSize(new Dimension(0, 200));
        ret.setPreferredSize(new Dimension(0, 200));
        ret.setSize(new Dimension(0, 200));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);

        actionListener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableComponent(!anonymousTest.isSelected());
            }

            private void disableComponent(boolean enable) {
                shareTest.setEnabled(enable);
                testWorkspace.setEnabled(enable);
                projectKey.setEnabled(enable);
                testTitle.setEnabled(enable);
                uploadToken.setEnabled(enable);
            }
        };
        anonymousTest.addActionListener(actionListener);
    }



    private void initFields() {
        anonymousTest.setSelected(false);
        shareTest.setSelected(false);
        testTitle.setText("");
        projectKey.setText("Default project");
        testWorkspace.setText("Default workspace");
        uploadToken.setText(UPLOAD_TOKEN_PLACEHOLDER);
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

    public void inform(String string) {
        infoText += string + "<br/>\n";
        infoArea.setText(infoText);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JMeterPluginsUtils.openInBrowser(e.getURL().toString());
        }
    }
}
