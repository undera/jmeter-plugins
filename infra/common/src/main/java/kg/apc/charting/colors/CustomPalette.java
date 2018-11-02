package kg.apc.charting.colors;

import kg.apc.charting.ColorsDispatcher;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Color Palette
 * <p>
 * Define custom palette by setting the following in jmeter.properties or user.properties:
 * <ul>
 * <li><code>jmeterPlugin.customColorsDispatcher = custompalette</code></li>
 * <li><code>jmeterPlugin.customColorsDispatcher.options = 112233,aabbff</code></li>
 * </ul>
 * <p>
 * Where options are a comma separated list of color hex values
 */
public class CustomPalette implements ColorsDispatcher {
    List<Color> customPalette = new ArrayList<Color>(16);
    private static final Logger log = LoggingManager.getLoggerForClass();

    int i = 0;

    /**
     * Implements ColorDispatcher using user supplied custom palette list
     *
     * @param options comma delimited list of colors in hex, e.g. 112233,aa00ff
     */
    public CustomPalette(String options) {
        buildCustomPalette(options);
        reset();
    }

    private void buildCustomPalette(String palette) {
        try {
            String[] colors = palette.split(",");
            if (palette.trim().length() == 0 || colors.length == 0) {
                log.warn("Empty palette, using static palette");
                useStaticPalette();
                return;
            }
            for (String color : colors) {
                try {
                    Color c = new Color(Integer.parseInt(color, 16));
                    log.debug("Adding to custom palette color: " + c);
                    customPalette.add(c);
                } catch (Exception e) {
                    log.warn("Exception " + e.getClass().getName() + " adding color: " + color + " ...skipping");
                }
            }
        } catch (Exception e) {
            log.error("Error building custom palette, using static palette: "
                    + e.getClass().getName() + ": " + e.getMessage());
            useStaticPalette();
        }
    }

    private void useStaticPalette() {
        customPalette.add(new Color(0xb02727));
        customPalette.add(new Color(0xb05e27));
        customPalette.add(new Color(0xb09627));
        customPalette.add(new Color(0x93b027));
        customPalette.add(new Color(0x5bb027));
        customPalette.add(new Color(0x27b02a));
        customPalette.add(new Color(0x27b062));
        customPalette.add(new Color(0x27b099));
        customPalette.add(new Color(0x2790b0));
        customPalette.add(new Color(0x2758b0));
        customPalette.add(new Color(0x2d27b0));
        customPalette.add(new Color(0x6527b0));
        customPalette.add(new Color(0x9c27b0));
        customPalette.add(new Color(0x000));
        customPalette.add(new Color(0x333333));
        customPalette.add(new Color(0x666666));
    }

    @Override
    public void reset() {
        i = 0;
    }

    @Override
    public Color getNextColor() {
        if (i + 1 > customPalette.size()) {
            reset();
            return getNextColor();
        } else {
            Color c = customPalette.get(i);
            log.debug("Custom color c next: " + c);
            i++;
            return c;
        }
    }
}
