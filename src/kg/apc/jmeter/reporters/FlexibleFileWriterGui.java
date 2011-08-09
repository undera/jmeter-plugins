package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.gui.BrowseAction;
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

    public FlexibleFileWriterGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Flexible File Writer");
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    public TestElement createTestElement() {
        TestElement te = new FlexibleFileWriter();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

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
        browseButton.addActionListener(new BrowseAction(filename));


        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Record each sample as: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, columns = new JTextField(20));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }
}
