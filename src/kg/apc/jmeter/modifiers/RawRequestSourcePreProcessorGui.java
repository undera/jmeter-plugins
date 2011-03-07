// TODO: have "check file consistency" button
// TODO: add "browse" button
package kg.apc.jmeter.modifiers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;

/**
 *
 * @author undera
 */
public class RawRequestSourcePreProcessorGui extends AbstractPreProcessorGui {
    public static final String WIKIPAGE = "RawDataSource";

    private JCheckBox rewindOnEOF;
    private JTextField variableName;
    private JTextField fileName;

    public RawRequestSourcePreProcessorGui() {
        super();
        init();
        initFields();
    }

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Raw Data Source PreProcessor");
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        rewindOnEOF.setSelected(element.getPropertyAsBoolean(RawRequestSourcePreProcessor.REWIND));
        variableName.setText(element.getPropertyAsString(RawRequestSourcePreProcessor.VARIABLE_NAME));
        fileName.setText(element.getPropertyAsString(RawRequestSourcePreProcessor.FILENAME));
    }

    public TestElement createTestElement() {
        RawRequestSourcePreProcessor preproc = new RawRequestSourcePreProcessor();
        modifyTestElement(preproc);
        preproc.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return preproc;
    }

    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
        if (te instanceof RawRequestSourcePreProcessor) {
            RawRequestSourcePreProcessor preproc = (RawRequestSourcePreProcessor) te;
            preproc.setRewindOnEOF(rewindOnEOF.isSelected());
            preproc.setVarName(variableName.getText());
            preproc.setFileName(fileName.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void init() {
     setLayout(new BorderLayout(0, 5));
      setBorder(makeBorder());

      add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(),WIKIPAGE), BorderLayout.NORTH);

      JPanel mainPanel = new JPanel(new GridBagLayout());

      GridBagConstraints labelConstraints = new GridBagConstraints();
      labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

      GridBagConstraints editConstraints = new GridBagConstraints();
      editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
      editConstraints.weightx = 1.0;
      editConstraints.fill = GridBagConstraints.HORIZONTAL;

      addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Rewind on end of file: ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 0, rewindOnEOF = new JCheckBox());
      addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Variable name: ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 1, variableName = new JTextField());
      addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Data file name: ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 2, fileName = new JTextField());

      JPanel container = new JPanel(new BorderLayout());
      container.add(mainPanel, BorderLayout.NORTH);
      add(container, BorderLayout.CENTER);
     }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component)
   {
      constraints.gridx = col;
      constraints.gridy = row;
      panel.add(component, constraints);
   }

    private void initFields() {
        rewindOnEOF.setSelected(true);
        variableName.setText("rawData");
        fileName.setText("");
    }
}
