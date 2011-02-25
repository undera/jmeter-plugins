package kg.apc.jmeter;

/**
 *
 * @author undera
 */
public class JMeterPluginsUtils {

    // just prefix all the labels to be distinguished
    public static String prefixLabel(String string) {
        return "JPGC - "+string;
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

}
