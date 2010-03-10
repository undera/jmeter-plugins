package kg.apc.jmeter.samplers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

public class DummySamplerGui
      extends AbstractSamplerGui
{
   private JCheckBox isSuccessful;
   private JTextField responseCode;
   private JTextField responseMessage;
   private JTextField responseTime;
   private JTextArea responseData;

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

   /**
    * Implements JMeterGUIComponent.clearGui
    */
   @Override
   public void clearGui()
   {
      super.clearGui();

      isSuccessful.setSelected(true);
      responseCode.setText("200");
      responseMessage.setText("OK");
      responseData.setText("");
      responseTime.setText("1");
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
      labelConstraints.anchor=GridBagConstraints.FIRST_LINE_END;

      GridBagConstraints editConstraints = new GridBagConstraints();
      editConstraints.anchor=GridBagConstraints.FIRST_LINE_START;
      editConstraints.weightx=1.0;
      editConstraints.fill=GridBagConstraints.HORIZONTAL;

      labelConstraints.gridx=0;
      labelConstraints.gridy=0;
      mainPanel.add(new JLabel("Successfull sample: ", JLabel.RIGHT), labelConstraints);
      isSuccessful = new JCheckBox();
      editConstraints.gridx=1;
      editConstraints.gridy=0;
      mainPanel.add(isSuccessful, editConstraints);

      labelConstraints.gridx=0;
      labelConstraints.gridy=1;
      mainPanel.add(new JLabel("Response Code (eg 200): ", JLabel.RIGHT), labelConstraints);
      responseCode = new JTextField();
      editConstraints.gridx=1;
      editConstraints.gridy=1;
      mainPanel.add(responseCode, editConstraints);

      labelConstraints.gridx=0;
      labelConstraints.gridy=2;
      mainPanel.add(new JLabel("Response Message (eg OK): ", JLabel.RIGHT), labelConstraints);
      responseMessage = new JTextField();
      editConstraints.gridx=1;
      editConstraints.gridy=2;
      mainPanel.add(responseMessage, editConstraints);

      labelConstraints.gridx=0;
      labelConstraints.gridy=3;
      mainPanel.add(new JLabel("Response Time (milliseconds): ", JLabel.RIGHT), labelConstraints);
      responseTime = new JTextField();
      editConstraints.gridx=1;
      editConstraints.gridy=3;
      mainPanel.add(responseTime, editConstraints);

      labelConstraints.gridx=0;
      labelConstraints.gridy=4;
      mainPanel.add(new JLabel("Response Data: ", JLabel.RIGHT), labelConstraints);
      responseData = new JTextArea();
      responseData.setRows(10);
      responseData.setBorder(new BevelBorder(BevelBorder.LOWERED));
      editConstraints.gridx=1;
      editConstraints.gridy=4;
      editConstraints.fill=GridBagConstraints.BOTH;
      mainPanel.add(responseData, editConstraints);

      JPanel container=new JPanel(new BorderLayout());
      container.add(mainPanel, BorderLayout.NORTH);
      add(container, BorderLayout.CENTER);
   }
}
