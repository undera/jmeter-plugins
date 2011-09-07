package kg.apc.charting;

/**
 * 
 * @author apc
 */
public abstract class AbstractGraphPanelChartElement {

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
