package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.BrowseAction;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

public class FlexibleFileWriterGui extends AbstractListenerGui implements ClipboardOwner {

    public static final String WIKIPAGE = "FlexibleFileWriter";
    private JTextField filename;
    private JTextField columns;
    private JCheckBox overwrite;
    private JTextArea header;
    private JTextArea footer;
    private String[] fields = {
            "endTime", "Epoch time when the request was ended",
            "endTimeMillis", "Same as endTime, but divided by 1000 (surrogate field, eg. 1311122631.104)",
            "isFailed", "If response was marked as failed (surrogate field)",
            "isSuccsessful", "If response was marked as successful",
            "latency", "Latency, time to first response byte received (if available)",
            "latencyMicros", "Same as latency, but multiplied by 1000 (surrogate field)",
            "connectTime", "Time to establish connection",
            "receivedBytes", "Number of request bytes received (if available)",
            "requestData", "Request data from sample",
            "responseCode", "Response code (eg. 200, 404, etc.)",
            "responseData", "Response data",
            "responseHeaders", "Response headers (if present in sample)",
            "responseMessage", "Response message (eg. OK, Not Found, etc.)",
            "responseFileName", "Response file name",
            "responseHeaderSize", "Size of response headers",
            "responseSize", "Size of response body",
            "responseTime", "Response time, time to full response loaded",
            "responseTimeMicros", "Same as responseTime, but multiplied by 1000 (surrogate field)",
            "sampleLabel", "Name of the sampler that made the request",
            "sentBytes", "Number of request bytes sent (if available)",
            "startTime", "Epoch time when the request was started",
            "startTimeMillis", "Same as startTime, but divided by 1000 (surrogate field, eg. 1311121131.062)",
            "threadName", "Name of thread in Thread Group that processed the request",
            "threadsCount", "total number of active threads in all groups",
            "grpThreads", "number of active threads in this thread group",
            "sampleCount", "number of samples (1, unless multiple samples are aggregated)",
            "errorCount", "number of errors (0 or 1, unless multiple samples are aggregated)",
            "URL", "The sample URL",
            "variable#<N>", "Sample variable with index N (eg. variable#2), see help for details"
    };

    public FlexibleFileWriterGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Flexible File Writer");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public TestElement createTestElement() {
        TestElement te = new FlexibleFileWriter();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    @Override
    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
        if (te instanceof FlexibleFileWriter) {
            FlexibleFileWriter fw = (FlexibleFileWriter) te;
            fw.setFilename(filename.getText());
            fw.setColumns(columns.getText());
            fw.setOverwrite(overwrite.isSelected());
            fw.setFileHeader(header.getText());
            fw.setFileFooter(footer.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void initFields() {
        filename.setText("testResults.txt");
        columns.setText("endTimeMillis|\\t|"
                + "responseTime|\\t|latency|\\t|"
                + "sentBytes|\\t|receivedBytes|\\t|"
                + "isSuccessful|\\t|responseCode|\\r\\n");
        overwrite.setSelected(false);
        header.setText("endTimeMillis\tresponseTime\tlatency\tsentBytes\t"
                + "receivedBytes\tisSuccessful\tresponseCode\n");
        footer.setText("");
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        FlexibleFileWriter fw = (FlexibleFileWriter) element;
        filename.setText(fw.getFilename());
        columns.setText(fw.getColumns());
        overwrite.setSelected(fw.isOverwrite());
        header.setText(fw.getFileHeader());
        footer.setText(fw.getFileFooter());
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

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Filename: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, filename = new JTextField(20));
        JButton browseButton = new JButton("Browse...");
        addToPanel(mainPanel, labelConstraints, 2, 1, browseButton);
        GuiBuilderHelper.strechItemToComponent(filename, browseButton);
        browseButton.addActionListener(new BrowseAction(filename));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Overwrite existing file: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, overwrite = new JCheckBox());

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Write File Header: ", JLabel.RIGHT));
        header = new JTextArea();
        header.setLineWrap(true);
        addToPanel(mainPanel, editConstraints, 1, 3, GuiBuilderHelper.getTextAreaScrollPaneContainer(header, 3));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Record each sample as: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, columns = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        addToPanel(mainPanel, labelConstraints, 0, 5, new JLabel("Write File Footer: ", JLabel.RIGHT));
        footer = new JTextArea();
        footer.setLineWrap(true);
        addToPanel(mainPanel, editConstraints, 1, 5, GuiBuilderHelper.getTextAreaScrollPaneContainer(footer, 3));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);

        add(createHelperPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHelperPanel() {
        JPanel ret = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.insets = new Insets(0, 0, 10, 0);
        labelConstraints.gridx = 0;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.gridwidth = 2;

        ret.add(new JLabel("Available sample fields (click any button to copy the field to clipboard):"), labelConstraints);

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.insets = new Insets(4, 0, 0, 0);
        buttonConstraints.gridx = 0;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints detailConstraints = new GridBagConstraints();
        detailConstraints.insets = new Insets(4, 10, 0, 0);
        detailConstraints.weightx = 1.0;
        detailConstraints.fill = GridBagConstraints.HORIZONTAL;
        detailConstraints.gridx = 1;
        detailConstraints.anchor = GridBagConstraints.WEST;

        int line = 1;

        CopyAction copyAction = new CopyAction();

        for (int i = 0; i < fields.length / 2; i++) {
            JButton fieldButton = new JButton(fields[2 * i]);
            fieldButton.addActionListener(copyAction);

            JTextField fieldDescription = new JTextField(fields[2 * i + 1]);
            fieldDescription.setEditable(false);
            fieldDescription.setBorder(null);
            fieldDescription.setOpaque(false);

            GuiBuilderHelper.strechItemToComponent(fieldDescription, fieldButton);

            buttonConstraints.gridy = line;
            detailConstraints.gridy = line;

            ret.add(fieldButton, buttonConstraints);
            ret.add(fieldDescription, detailConstraints);

            line++;
        }
        return ret;
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // do nothing
    }

    private class CopyAction
            implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            Clipboard clipboard = getToolkit().getSystemClipboard();
            Transferable transferable = new Transferable() {

                @Override
                public Object getTransferData(DataFlavor flavor) {
                    if (isDataFlavorSupported(flavor)) {
                        return "|" + ((JButton) e.getSource()).getText() + "|";
                    }
                    return null;
                }

                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{
                            DataFlavor.stringFlavor
                    };
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return DataFlavor.stringFlavor.equals(flavor);
                }
            };
            clipboard.setContents(transferable, FlexibleFileWriterGui.this);
        }
    }
}
