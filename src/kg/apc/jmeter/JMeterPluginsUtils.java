package kg.apc.jmeter;

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

    public static String getStackTrace(Exception ex) {
        StackTraceElement[] stack = ex.getStackTrace();
        String res = "";
        for(int n=0; n<stack.length; n++)
        {
            res+=stack[n].toString()+"\n";
        }
        return res;
    }

    static
    {
        String pluginsPrefix = JMeterUtils.getProperty("jmeterPlugin.pluginsPrefix");
        if (pluginsPrefix != null)
        {
            JMeterPluginsUtils.pluginsPrefix = pluginsPrefix;
        }
    }
}
