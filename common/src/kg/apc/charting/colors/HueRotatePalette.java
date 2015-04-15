package kg.apc.charting.colors;

import kg.apc.charting.ColorsDispatcher;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Rotating Hue Color Palette
 *
 * Creates rotating hue palette by setting the following in jmeter.properties or user.properties:
 * <li><code>jmeterPlugin.customColorsDispatcher = huerotate</code></li>
 * <li><code>jmeterPlugin.customColorsDispatcher.options = 9C27B0,8,4</code></li>
 *
 * Where options define 3 fields
 * <li>options[0] - Starting color</li>
 * <li>options[1] - Number of proportional rotations</li>
 * <li>options[2] - black->gray gradient steps to generate</li>
 */
public class HueRotatePalette implements ColorsDispatcher {
    List<Color> customPalette = new ArrayList<Color>(16);
    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final int startingGradient = 0x000;
    private static final int maxGradient = 120;
    int i = 0;

    /**
     * Implements ColorDispatcher using a base color and rotating it's hue and generating
     * black -> gray gradient with user defined steps
     * @param options comma delimited list of colors in hex, e.g. 112233,aa00ff
     */
    public HueRotatePalette(String options) {
        buildCustomPalette(options);
        reset();
    }

    private void buildCustomPalette(String options) {
        try {
            String[] opts = options.split(",");
            Color baseColor = new Color ( Integer.parseInt(opts[0].trim(), 16) );
            int rotations = Integer.parseInt(opts[1]);
            int gradient_steps = Integer.parseInt(opts[2]);
            float[] hsbVals = new float[3];
            Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hsbVals);
            log.debug("Base color: " + baseColor + " rotations " + rotations + " hue: " + hsbVals[0]);
            for (int i = 0; i <= rotations; i++) {
                float hue = hsbVals[0]*(1f/rotations)*i;
                Color c = new Color(Color.HSBtoRGB( hue, hsbVals[1], hsbVals[2]));
                log.debug("Adding custom color (hue rotation): " + c);
                customPalette.add(c);
            }
            log.debug("Black->Gray gradient steps: " + gradient_steps);
            for (int i = startingGradient; i < maxGradient; i= i+(maxGradient/gradient_steps+2) ) {
                Color c = new Color(i, i, i);
                log.debug("Adding gradient step: " + c);
                customPalette.add(c);
            }
        } catch (Exception e) {
            log.warn("Error building custom palette, using static palette: "
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
        if (i+1 > customPalette.size()) {
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
