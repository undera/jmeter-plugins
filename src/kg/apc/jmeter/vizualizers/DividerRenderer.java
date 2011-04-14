package kg.apc.jmeter.vizualizers;

import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author undera
 */
class DividerRenderer extends NumberRenderer{
    private final double factor;

    public DividerRenderer(double i) {
        factor=i;
    }

    @Override
    public void setValue(Object value) {
        Double val=((Long) value) /factor;
        setText(Double.toString(val));
    }
}
