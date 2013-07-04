package kg.apc.charting;

import java.io.Serializable;

/**
 * 
 * @author apc
 */
public abstract class AbstractGraphPanelChartElement implements Serializable {

    /**
     *
     * @return
     */
    public abstract double getValue();

    public abstract void add(double val);

    public boolean isPointRepresentative(int limit) {
        return true;
    }
}
