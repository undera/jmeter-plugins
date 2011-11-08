package kg.apc.jmeter.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.jmeter.config.Arguments;

/**
 *
 * @author Stephane Hoblingre
 */
public class VariablesFromCSV extends Arguments{
    public static final String VARIABLE_PREFIX = "variable_prefix";
    public static final String FILENAME = "filename";
    public static final String SEPARATOR = "separator";

    private File file;


    //It seems org.apache.jmeter.engine.Precompiler requires only this method
    @Override
    public Map<String, String> getArgumentsAsMap() {
       //logic to put here
       return new HashMap<String, String>();
    }

    public String getVariablePrefix() {
        return getPropertyAsString(VARIABLE_PREFIX);
    }

    public void setVariablePrefix(String prefix) {
        setProperty(VARIABLE_PREFIX, prefix);
    }

    public String getFileName() {
        return getPropertyAsString(FILENAME);
    }

    public void setFileName(String filename) {
        setProperty(FILENAME, filename);
        file = null;
    }

    public String getSeparator() {
        return getPropertyAsString(SEPARATOR);
    }

    public void setSeparator(String separator) {
        setProperty(SEPARATOR, separator);
    }
}
