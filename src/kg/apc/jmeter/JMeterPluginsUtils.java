package kg.apc.jmeter;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author undera
 */
public class JMeterPluginsUtils
{
    private static String pluginsPrefix = "jp@gc - ";

    // just prefix all the labels to be distinguished
    public static String prefixLabel(String label)
    {
        return pluginsPrefix + label;
    }

    public static String getStackTrace(Exception ex)
    {
        StackTraceElement[] stack = ex.getStackTrace();
        StringBuilder res = new StringBuilder();
        for(int n=0; n<stack.length; n++)
        {
            res.append(stack[n].toString());
            res.append('\n');
        }
        return res.toString();
    }

    static
    {
        String pluginsPrefixCfg = JMeterUtils.getProperty("jmeterPlugin.pluginsPrefix");
        if (pluginsPrefixCfg != null)
        {
            JMeterPluginsUtils.pluginsPrefix = pluginsPrefixCfg;
        }
    }

   /**
    *
    * @param model
    * @return
    */
   public static CollectionProperty tableModelToCollectionProperty(PowerTableModel model, String propname)
   {
      CollectionProperty rows = new CollectionProperty(propname, new ArrayList<Object>());
      for (int col = 0; col < model.getColumnCount(); col++)
      {
         rows.addItem(model.getColumnData(model.getColumnName(col)));
      }
      return rows;
   }
  /**
    *
    * @param model
    * @return
    */
   public static CollectionProperty tableModelToCollectionPropertyEval(PowerTableModel model, String propname)
   {
      CollectionProperty rows = new CollectionProperty(propname, new ArrayList<Object>());
      for (int col = 0; col < model.getColumnCount(); col++)
      {
         ArrayList<Object> tmp = new ArrayList<Object>();
         Iterator iter = model.getColumnData(model.getColumnName(col)).iterator();
         while(iter.hasNext())
         {
             String value = iter.next().toString();
             tmp.add(new CompoundVariable(value).execute());
         }

         rows.addItem(tmp);
      }
      return rows;
   }

}
