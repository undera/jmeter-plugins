// TODO add labels

package kg.apc.jmeter.samplers;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

public class DummySamplerGui
      extends AbstractSamplerGui
{
   private JCheckBox isSuccessful;
   private JTextField responseCode;
   private JTextField responseMessage;
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

      VerticalPanel mainPanel = new VerticalPanel();

      isSuccessful=new JCheckBox(DummySampler.IS_SUCCESSFUL);
      mainPanel.add(isSuccessful);

      responseCode=new JTextField();
      mainPanel.add(responseCode);

      responseMessage=new JTextField();
      mainPanel.add(responseMessage);

      responseData=new JTextArea();
      mainPanel.add(responseData);

      add(mainPanel, BorderLayout.CENTER);
   }
}
