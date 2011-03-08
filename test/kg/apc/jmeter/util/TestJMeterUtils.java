package kg.apc.jmeter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeListener;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Test;

/**
 *
 * @author apc
 */
public abstract class TestJMeterUtils
{
   private static JMeterTreeListener jMeterTreeListener;
   private static JMeterTreeModel jMeterTreeModel;

   /**
    *
    */
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
      JMeterContextService.getContext().setVariables(new JMeterVariables());
        StandardJMeterEngine engine = new EmulatorJmeterEngine();
      JMeterContextService.getContext().setEngine(engine);
   }

   /**
    *
    */
   @Test
   public void testEnv()
   {
      TestJMeterUtils.createJmeterEnv();
      GuiPackage.getInstance().updateCurrentNode();
   }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
