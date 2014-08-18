package kg.apc.jmeter.config;

import org.apache.jmeter.config.Arguments;

import java.util.Map;

public class VariablesFromCSV extends Arguments {

    public static final String VARIABLE_PREFIX = "variablesPrefix";
    public static final String FILENAME = "filename";
    public static final String SEPARATOR = "delimiter";
    public static final String SKIP_LINES = "skipLines";
    public static final int SKIP_LINES_DEFAULT = 0;
    public static final String STORE_SYS_PROP = "storeSysProp";

    // It seems org.apache.jmeter.engine.Precompiler requires only this
    // https://groups.google.com/forum/#!topic/jmeter-plugins/gWn7MTgvTfE method
    @Override
    public Map<String, String> getArgumentsAsMap() {
        Map<String, String> variables = new VariableFromCsvFileReader(getFileName()).getDataAsMap(getVariablePrefix(), getSeparator(), getSkipLines());
        //store in System Properties also
        if (isStoreAsSystemProperty()) {
            for (Map.Entry<String, String> element : variables.entrySet()) {
                String variable = element.getKey();
                if (System.getProperty(variable) == null) {
                    System.setProperty(variable, element.getValue());
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

    public int getSkipLines() {
        return getPropertyAsInt(SKIP_LINES, SKIP_LINES_DEFAULT);
    }

    public void setSkipLines(int skipLines) {
        setProperty(SKIP_LINES, skipLines);
    }

    public boolean isStoreAsSystemProperty() {
        return getPropertyAsBoolean(STORE_SYS_PROP);
    }

    public void setStoreAsSystemProperty(boolean storeAsSysProp) {
        setProperty(STORE_SYS_PROP, storeAsSysProp);
    }
}
