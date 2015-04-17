package kg.apc.charting;

import java.io.Serializable;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import kg.apc.charting.colors.*;

public class ColorsDispatcherFactory implements Serializable {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static ColorsDispatcher getColorsDispatcher() {
        String customDispatcher = JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher");
        String customOptions = JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options");
        if (customDispatcher != null) {
            if (customDispatcher.equalsIgnoreCase("huerotate")) {
                log.debug("customDispatcher hue rotate");
                return new HueRotatePalette(customOptions);
            } else if (customDispatcher.equalsIgnoreCase("custompalette")) {
                log.debug("customDispatcher custom palette");
                return new CustomPalette(customOptions);
            }
        }
        log.debug("Original boring cycle colors");
        return new CycleColors(); // original "cycle" colors dispatcher
    }

}
