package kg.apc.jmeter.config;

import java.util.Map;
import org.apache.jmeter.config.Arguments;

/**
 *
 * @author Stephane Hoblingre
 */
public class VariablesFromCSV extends Arguments{
    public static final String VARIABLE_PREFIX = "variablesPrefix";
    public static final String FILENAME = "filename";
    public static final String SEPARATOR = "delimiter";

    //It seems org.apache.jmeter.engine.Precompiler requires only this method
    @Override
    public Map<String, String> getArgumentsAsMap() {
       return new VariableFromCsvFileReader(getFileName()).getDataAsMap(getVariablePrefix(), getSeparator());
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
    }

    public String getSeparator() {
        return getPropertyAsString(SEPARATOR);
    }

    public void setSeparator(String separator) {
        setProperty(SEPARATOR, separator);
    }
}
