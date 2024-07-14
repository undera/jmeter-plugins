package kg.apc.jmeter.vizualizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.jmeter.samplers.SampleSaveConfiguration;

public class ConfigurationHelper {
    private static final String COMMA = ",";

    public static List<String> getList(String property) {
        if (property.isEmpty()) {
            return new ArrayList<>(0);
        } else {
            return Arrays.asList(property.split(COMMA));
        }
    }

    public static long getTimeDelimiter(String property, long defaultValue) {
        if (!property.isEmpty() && property.length() < 19 && property.matches("^[1-9][0-9]*")) {
            return Long.valueOf(property) * 1000;
        }
        return defaultValue;
    }

    public static void setupThreadCounts(SampleSaveConfiguration config) {
        config.setThreadCounts(true);
    }
}
