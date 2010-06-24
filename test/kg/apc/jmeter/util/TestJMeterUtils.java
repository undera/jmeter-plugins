package kg.apc.jmeter.util;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.jmeter.util.JMeterUtils;

public class TestJMeterUtils
{
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
   }
}
