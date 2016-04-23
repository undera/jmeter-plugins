package kg.apc.charting;

import java.awt.Color;
import java.io.Serializable;

/**
 * Interface for ColorsDispatcher to providing custom color palettes
 */
public interface ColorsDispatcher extends Serializable {
    public void reset();
    public Color getNextColor();
}
