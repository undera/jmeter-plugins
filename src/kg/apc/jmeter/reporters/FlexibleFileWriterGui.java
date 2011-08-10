package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.BrowseAction;
import kg.apc.jmeter.gui.GuiBuilderHelper;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

/**
 *
 * @author undera
 */
public class FlexibleFileWriterGui extends AbstractListenerGui {

    public static final String WIKIPAGE = "FlexibleFileWriter";

    private JTextField filename;
    private JTextField columns;
    private JButton browseButton;

    private static String help = "|startTime|\t\tEpoch time when the request was started\n"
           + "|endTime|\t\tEpoch time when the request was ended\n"
           + "|responseTime|\t\tResponse time, time to full response loaded\n"
           + "|latency|\t\tLatency, time to first response byte received (if available)\n"
           + "|responseCode|\tResponse code (200, 404, etc.)\n"
           + "|responseMessage|\tResponse message (OK, Not Found, etc.)\n"
           + "|responseHeaders|\tResponse headers (if present in sample)\n"
           + "|responseData|\t\tResponse data\n"
           + "|requestData|\t\tRequest data from sample\n"
           + "|sentBytes|\t\tNumber of request bytes sent (if available)\n"
           + "|receivedBytes|\t\tNumber of request bytes received (if available)\n"
           + "|threadName|\t\tName of thread in Thread Group that processed the request\n"
           + "|sampleLabel|\t\tName of the sampler that made the request\n"
           + "|isSuccsessful|\t\tIf response was marked as successful\n"
           + "|isFailed|\t\tIf response was marked as failed (surrogate field)\n"
           + "|startTimeMillis|\t\tSame as startTime, but divided by 1000 (surrogate field, example: 1311121131.362)\n"
           + "|endTimeMillis|\t\tSame as endTime, but divided by 1000 (surrogate field)\n"
           + "|responseTimeMicros|\tSame as responseTime, but multiplied by 1000 (surrogate field)\n"
           + "|latencyMicros|\t\tSame as latency, but multiplied by 1000 (surrogate field)";

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
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        FlexibleFileWriter fw = (FlexibleFileWriter) element;
        filename.setText(fw.getFilename());
        columns.setText(fw.getColumns());
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
        addToPanel(mainPanel, labelConstraints, 2, 1, browseButton = new JButton("Browse..."));

        GuiBuilderHelper.strechButtonToComponent(filename, browseButton);

        browseButton.addActionListener(new BrowseAction(filename));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Record each sample as: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, columns = new JTextField(20));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setWrapStyleWord(true);
        info.setOpaque(false);
        info.setLineWrap(true);
        info.setColumns(20);

        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setViewportView(info);
        jScrollPane1.setBorder(null);

        info.setText("Available fields:\n\n" + help + "\n");

        add(info, BorderLayout.SOUTH);
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }
}
