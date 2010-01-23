package kg.apc.jmeter.control;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.gui.AbstractControllerGui;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.util.JMeterUtils;

public class ParameterizedControllerGui
     extends AbstractControllerGui
{
   private ArgumentsPanel argsPanel;

   public ParameterizedControllerGui()
   {
      init();
   }

   public TestElement createTestElement()
   {
      ParameterizedController tc = new ParameterizedController();
      modifyTestElement(tc);
      return tc;
   }

   public void modifyTestElement(TestElement te)
   {
      super.configureTestElement(te);
      if (te instanceof ParameterizedController)
      {
         ParameterizedController controller = (ParameterizedController) te;
         controller.setUserDefinedVariables((Arguments) argsPanel.createTestElement());
      }
   }

   public String getLabelResource()
   {
      return getClass().getName();
   }

   @Override
   public String getStaticLabel()
   {
      return "Parameterized Controller";
   }

   private void init()
   {
      setLayout(new BorderLayout());
      setBorder(makeBorder());
      add(makeTitlePanel(), BorderLayout.NORTH);

      add(createVariablePanel(), BorderLayout.CENTER);
   }

   private JPanel createVariablePanel()
   {
      argsPanel = new ArgumentsPanel(JMeterUtils.getResString("user_defined_variables")); // $NON-NLS-1$
      return argsPanel;
   }

   @Override
   public void clearGui()
   {
      argsPanel.clear();
      super.clearGui();
   }

   @Override
   public void configure(TestElement te)
   {
      ParameterizedController controller = (ParameterizedController) te;
      final JMeterProperty udv = controller.getUserDefinedVariablesAsProperty();
      if (udv != null)
      {
         argsPanel.configure((Arguments) udv.getObjectValue());
      }
   }
}
