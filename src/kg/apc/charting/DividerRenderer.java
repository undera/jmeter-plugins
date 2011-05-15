package kg.apc.charting;

import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author undera
 */
public class DividerRenderer extends NumberRenderer{
    private final double factor;

    public DividerRenderer(double i) {
        factor=i;
    }

    public double getFactor() {
        return factor;
    }

    @Override
    public void setValue(Object value) {
        Double val=((Long) value) /factor;
        setText(Double.toString(val));
    }
}
