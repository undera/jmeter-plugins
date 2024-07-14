package kg.apc.charting;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.AbstractGraphRow;

import java.util.Iterator;
import java.util.Map.Entry;

public class GraphRowStatistics {
    private long maxX = Long.MIN_VALUE;
    private long minX = Long.MAX_VALUE;
    private long firstTime = Long.MIN_VALUE;

    public long getMaxX() {
        return maxX;
    }

    public long getMinX() {
        return minX;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void add(long xVal, double yVal) {
        if (firstTime == Long.MIN_VALUE) {
            firstTime = xVal;
        }

        if (xVal > maxX) {
            maxX = xVal;
        }
        if (xVal < minX) {
            minX = xVal;
        }
    }

    public double[] getMinMaxY(AbstractGraphRow graphRow, int maxPoints) {
        int factor;
        double[] minMax = new double[2];
        minMax[0] = Double.MAX_VALUE;
        minMax[1] = 0;
        Entry<Long, AbstractGraphPanelChartElement> element;

        if (maxPoints > 0) {
            factor = (int) Math.floor(graphRow.size() / (double) maxPoints) + 1;
        } else {
            factor = 1;
        }

        Iterator<Entry<Long, AbstractGraphPanelChartElement>> it = graphRow.iterator();

        double calcY;

        while (it.hasNext()) {
            calcY = 0;

            if (factor == 1) {
                element = it.next();
                AbstractGraphPanelChartElement elt = element.getValue();
                calcY = elt.getValue();
            } else {
                double nbPointProcessed = 0;
                for (int i = 0; i < factor; i++) {
                    if (it.hasNext()) {
                        element = it.next();
                        calcY = calcY + element.getValue().getValue();
                        nbPointProcessed++;
                    }
                }
                calcY = calcY / nbPointProcessed;
            }

            if (minMax[0] > calcY) {
                minMax[0] = calcY;
            }
            if (minMax[1] < calcY) {
                minMax[1] = calcY;
            }
        }

        //if bars min always 0
        if (graphRow.isDrawBar()) {
            minMax[0] = 0;
        }
        return minMax;
    }
}