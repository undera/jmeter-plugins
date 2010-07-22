package kg.apc.jmeter.util;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeListener;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Test;

public class TestJMeterUtils
{
   private static JMeterTreeListener jMeterTreeListener;
   private static JMeterTreeModel jMeterTreeModel;

   public static void createJmeterEnv()
   {
      File propsFile = null;
      try
      {
         propsFile = File.createTempFile("jmeter-plugins", "testProps");
      }
      catch (IOException ex)
      {
      }

      JMeterUtils.loadJMeterProperties(propsFile.getAbsolutePath());
      JMeterUtils.setLocale(new Locale("ignoreResources"));

      jMeterTreeModel = new JMeterTreeModel();
      jMeterTreeListener = new JMeterTreeListener();
      jMeterTreeListener.setModel(jMeterTreeModel);
      GuiPackage.getInstance(jMeterTreeListener, jMeterTreeModel);
   }

   @Test
   public void testEnv()
   {
      TestJMeterUtils.createJmeterEnv();
      GuiPackage.getInstance().updateCurrentNode();
   }
}
