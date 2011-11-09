package kg.apc.jmeter.config;

import java.util.Iterator;
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
    public static final String STORE_SYS_PROP = "storeSysProp";

    //It seems org.apache.jmeter.engine.Precompiler requires only this method
    @Override
    public Map<String, String> getArgumentsAsMap() {
       Map<String, String> variables = new VariableFromCsvFileReader(getFileName()).getDataAsMap(getVariablePrefix(), getSeparator());
       //store in System Properties also
       if(isStoreAsSystemProperty()) {
          Iterator<String> iter = variables.keySet().iterator();
            while(iter.hasNext()) {
               String variable = iter.next();
               if(System.getProperty(variable) == null) {
                  System.setProperty(variable, variables.get(variable));
               }
            }
       }

       return variables;
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

    public boolean isStoreAsSystemProperty() {
        return getPropertyAsBoolean(STORE_SYS_PROP);
    }

    public void setStoreAsSystemProperty(boolean storeAsSysProp) {
        setProperty(STORE_SYS_PROP, storeAsSysProp);
    }
}
