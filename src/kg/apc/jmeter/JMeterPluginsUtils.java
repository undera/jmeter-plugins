package kg.apc.jmeter;

import java.nio.ByteBuffer;
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
public abstract class JMeterPluginsUtils
{
    private static String PLUGINS_PREFIX = "jp@gc - ";
    private static boolean prefixPlugins = true;

    // just prefix all the labels to be distinguished
    public static String prefixLabel(String label)
    {
        return prefixPlugins ? PLUGINS_PREFIX + label : label;
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
        String prefixPluginsCfg = JMeterUtils.getProperty("jmeterPlugin.prefixPlugins");
        if (prefixPluginsCfg != null)
        {
            JMeterPluginsUtils.prefixPlugins = "true".equalsIgnoreCase(prefixPluginsCfg.trim());
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

   public static String byteBufferToString(ByteBuffer buf)
    {
       ByteBuffer str=buf.duplicate();
       //System.err.println(str);
       str.rewind();
       //System.err.println(str);
       byte[] dst=new byte[str.limit()];
       str.get(dst) ;
       return new String(dst);
   }

    public static String replaceRNT(String str) {
        str=str.replaceAll("\\\\\\\\", "VERY BAD WAY");
        //System.err.println(str);
        str=str.replaceAll("\\\\t", "\t");
        //str=str.replaceAll("(^|[^\\\\])\\\\t", "$1\t");
        //System.err.println(str);
        str=str.replaceAll("\\\\n", "\n");
        //System.err.println(str);
        str=str.replaceAll("\\\\r", "\r");
        str=str.replaceAll("VERY BAD WAY", "\\\\");
        return str;
    }
}
