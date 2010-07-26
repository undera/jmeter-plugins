package kg.apc.jmeter.samplers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

/**
 *
 * @author apc
 */
public class DummySamplerGui
     extends AbstractSamplerGui
{
   private JCheckBox isSuccessful;
   private JTextField responseCode;
   private JTextField responseMessage;
   private JTextField responseTime;
   private JTextArea responseData;

   /**
    *
    */
   public DummySamplerGui()
   {
      init();
   }

   @Override
   public String getStaticLabel()
   {
      return "Dummy Sampler";
   }

   @Override
   public void configure(TestElement element)
   {
      super.configure(element);

      isSuccessful.setSelected(element.getPropertyAsBoolean(DummySampler.IS_SUCCESSFUL));
      responseCode.setText(element.getPropertyAsString(DummySampler.RESPONSE_CODE));
      responseMessage.setText(element.getPropertyAsString(DummySampler.RESPONSE_MESSAGE));
      responseData.setText(element.getPropertyAsString(DummySampler.RESPONSE_DATA));
      responseTime.setText(element.getPropertyAsString(DummySampler.RESPONSE_TIME));
   }

   public TestElement createTestElement()
   {
      DummySampler sampler = new DummySampler();
      modifyTestElement(sampler);
      return sampler;
   }

   /**
    * Modifies a given TestElement to mirror the data in the gui components.
    *
    * @param sampler
    * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
    */
   public void modifyTestElement(TestElement sampler)
   {
      super.configureTestElement(sampler);

      if (sampler instanceof DummySampler)
      {
         DummySampler dummySampler = (DummySampler) sampler;
         dummySampler.setSuccessful(isSuccessful.isSelected());
         dummySampler.setResponseCode(responseCode.getText());
         dummySampler.setResponseMessage(responseMessage.getText());
         dummySampler.setResponseData(responseData.getText());
         int time = 0;
         try
         {
            time = Integer.valueOf(responseTime.getText());
         }
         catch (NumberFormatException e)
         {
         }
         dummySampler.setResponseTime(time);
      }
   }

   @Override
   public void clearGui()
   {
      super.clearGui();

      isSuccessful.setSelected(true);
      responseCode.setText("200");
      responseMessage.setText("OK");
      responseData.setText("");
      responseTime.setText("100");
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   private void init()
   {
      setLayout(new BorderLayout(0, 5));
      setBorder(makeBorder());

      add(makeTitlePanel(), BorderLayout.NORTH);

      JPanel mainPanel = new JPanel(new GridBagLayout());

      GridBagConstraints labelConstraints = new GridBagConstraints();
      labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

      GridBagConstraints editConstraints = new GridBagConstraints();
      editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
      editConstraints.weightx = 1.0;
      editConstraints.fill = GridBagConstraints.HORIZONTAL;

      addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Successfull sample: ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 0, isSuccessful = new JCheckBox());
      addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Response Code (eg 200): ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 1, responseCode = new JTextField());
      addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Response Message (eg OK): ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 2, responseMessage = new JTextField());
      addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Response Time (milliseconds): ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 3, responseTime = new JTextField());
      addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Response Data: ", JLabel.RIGHT));

      editConstraints.fill = GridBagConstraints.BOTH;
      addToPanel(mainPanel, editConstraints, 1, 4, responseData = new JTextArea());
      responseData.setRows(10);
      responseData.setBorder(new BevelBorder(BevelBorder.LOWERED));

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
}
