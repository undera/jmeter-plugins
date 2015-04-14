package kg.apc.charting;

import java.awt.Color;

/**
 * Interface for ColorsDispatcher to providing custom color palettes
 */
public interface ColorsDispatcher {
    public void reset();
    public Color getNextColor();
}
