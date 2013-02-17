package kg.apc.jmeter.control;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.gui.AbstractControllerGui;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author apc
 */
public class ParameterizedControllerGui
     extends AbstractControllerGui
{
    public static final String WIKIPAGE = "ParameterizedController";
   private ArgumentsPanel argsPanel;

   /**
    *
    */
   public ParameterizedControllerGui()
   {
      init();
   }

   public TestElement createTestElement()
   {
      ParameterizedController tc = new ParameterizedController();
      modifyTestElement(tc);
      tc.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
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
      return JMeterPluginsUtils.prefixLabel("Parameterized Controller");
   }

   private void init()
   {
      setLayout(new BorderLayout());
      setBorder(makeBorder());
      add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

      add(createVariablePanel(), BorderLayout.CENTER);
   }

   private JPanel createVariablePanel()
   {
      argsPanel = new ArgumentsPanel(JMeterUtils.getResString("user_defined_variables")); 
      return argsPanel;
   }

   @Override
   public void clearGui()
   {
      super.clearGui();
      argsPanel.clear();
   }

   @Override
   public void configure(TestElement te)
   {
      super.configure(te);
      ParameterizedController controller = (ParameterizedController) te;
      final JMeterProperty udv = controller.getUserDefinedVariablesAsProperty();
      if (udv != null)
      {
         argsPanel.configure((Arguments) udv.getObjectValue());
      }
   }
}
