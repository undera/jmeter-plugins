package kg.apc.charting;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class ColorsDispatcher implements Serializable {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private ArrayList<Color> assignedColors = new ArrayList<Color>();
    private final static int LEVEL_MAX = 256;
    private int level;
    private int bits;
    private int increment;

    /**
     *
     */
    public ColorsDispatcher() {
        reset();
    }

    public final void reset() {
        assignedColors.clear();
        increment = LEVEL_MAX;
        bits = 0;
        level = LEVEL_MAX - 1;

        assignedColors.add(new Color(255, 255, 0));
        assignedColors.add(new Color(127, 127, 0));
    }

    /**
     *
     * @return
     */
    public Color getNextColor() {
        Color color = null;

        doCycles();

        int r = 0, g = 0, b = 0;
        if ((bits & 1) == 1) {
            r = level;
        }
        if ((bits & 2) == 2) {
            b = level;
        }
        if ((bits & 4) == 4) {
            g = level;
        }
        Color c = new Color(r, g, b);
        if (assignedColors.contains(c)) {
            if (log.isDebugEnabled()) {
                log.debug("Existing " + r + " " + g + " " + b);
            }
            color = getNextColor();
        } else if ((r + g + b) / 3 < 32) {
            if (log.isDebugEnabled()) {
                log.debug("Too dark " + r + " " + g + " " + b);
            }
            color = getNextColor();
        } else if ((r + g + b) / 3 > 256 - 64) {
            if (log.isDebugEnabled()) {
                log.debug("Too light " + r + " " + g + " " + b);
            }
            color = getNextColor();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("New " + r + " " + g + " " + b);
            }
            color = new Color(r, g, b);
        }

        assignedColors.add(color);
        return color;
    }

    private void doCycles() {
        bits++;
        if (bits >= 8) {
            level -= increment;
            if (level < 0) {
                increment /= 2;
                if (increment <= 0) {
                    log.warn("Colors exceeded. Rewind colors.");
                    reset();
                }
                level = LEVEL_MAX - 1;
            }

            bits = 0;
        }
    }
}
