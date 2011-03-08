package kg.apc.jmeter.reporters;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

/**
 *
 * @author undera
 */
public class AutoStopGui extends AbstractListenerGui {

    public static final String WIKIPAGE = "AutoStop";
    private JTextField responseTime;
    private JTextField responseTimeSecs;
    private JTextField errorRate;
    private JTextField errorRateSecs;

    public AutoStopGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("AutoStop Listener");
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    public TestElement createTestElement() {
        TestElement te = new AutoStop();
        modifyTestElement(te);
        te.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return te;
    }

    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
        if (te instanceof AutoStop) {
            AutoStop fw = (AutoStop) te;
            fw.setResponseTime(responseTime.getText());
            fw.setResponseTimeSecs(responseTimeSecs.getText());
            fw.setErrorRate(responseTime.getText());
            fw.setErrorRateSecs(responseTimeSecs.getText());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        AutoStop fw = (AutoStop) element;
        responseTime.setText(fw.getResponseTime());
        responseTimeSecs.setText(fw.getResponseTimeSecs());
        responseTime.setText(fw.getErrorRate());
        responseTimeSecs.setText(fw.getErrorRateSecs());
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void initFields() {
        responseTime.setText("1000");
        responseTimeSecs.setText("10");
        errorRate.setText("50");
        errorRateSecs.setText("10");
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new TitledBorder("Shutdown test if"));

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Average response time is greater than ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, responseTime = new JTextField());
        addToPanel(mainPanel, labelConstraints, 2, 1, new JLabel("ms for ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 3, 1, responseTimeSecs = new JTextField());
        addToPanel(mainPanel, labelConstraints, 4, 1, new JLabel("seconds", JLabel.RIGHT));

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("OR", JLabel.RIGHT));

        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Error rate is greater than ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, errorRate = new JTextField());
        addToPanel(mainPanel, labelConstraints, 2, 3, new JLabel("%  for ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 3, 3, errorRateSecs = new JTextField());
        addToPanel(mainPanel, labelConstraints, 4, 3, new JLabel("seconds", JLabel.RIGHT));

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
