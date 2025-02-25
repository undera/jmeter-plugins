package kg.apc.jmeter.config;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class VariablesFromCSV extends Arguments {

    public static final String VARIABLE_PREFIX = "variablesPrefix";
    public static final String FILENAME = "filename";
    public static final String SEPARATOR = "delimiter";
    public static final String SKIP_LINES = "skipLines";
    public static final int SKIP_LINES_DEFAULT = 0;
    public static final String STORE_SYS_PROP = "storeSysProp";
    private static final Logger log = LoggerFactory.getLogger(VariablesFromCSV.class);

    @Override
    public Map<String, String> getArgumentsAsMap() {
        String fileName = getFileName();
        String interpretedPath = interpretPath(fileName);

        Map<String, String> variables = new VariableFromCsvFileReader(interpretedPath)
                .getDataAsMap(getVariablePrefix(), getSeparator(), getSkipLines());

        if (isStoreAsSystemProperty()) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                String variable = entry.getKey();
                if (System.getProperty(variable) == null) {
                    System.setProperty(variable, entry.getValue());
                }
            }
        }

        return variables;
    }

    /**
     * Interprets the given path by evaluating any embedded expressions.
     *
     * @param rawPath the raw path string that may contain expressions
     * @return the interpreted path
     */
    public String interpretPath(String rawPath) {
        if (rawPath == null || rawPath.isEmpty()) {
            return rawPath;
        }

        try {
            CompoundVariable compoundVariable = new CompoundVariable(rawPath);
            String interpretedPath = compoundVariable.execute().trim();

            if (!interpretedPath.isEmpty() && !new java.io.File(interpretedPath).exists()) {
                throw new IllegalArgumentException("Interpreted path does not exist: " + interpretedPath);
            }

            return interpretedPath;
        } catch (Exception ex) {
            log.error("Error interpreting path: " + rawPath, ex);
            throw new RuntimeException("Failed to interpret path: " + rawPath, ex);
        }
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
