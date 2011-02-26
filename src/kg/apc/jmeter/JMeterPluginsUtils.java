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
}
