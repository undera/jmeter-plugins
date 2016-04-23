package kg.apc.jmeter.graphs;

import kg.apc.charting.AbstractGraphPanelChartElement;
import kg.apc.charting.AbstractGraphRow;
import org.apache.jmeter.samplers.SampleResult;

import java.awt.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Class used to handle thread counts in local and distributed tests
 */
public abstract class AbstractVsThreadVisualizer extends AbstractGraphPanelVisualizer {
    private ConcurrentSkipListMap<String, AbstractGraphRow> state = new ConcurrentSkipListMap<String, AbstractGraphRow>();
    private final static int PRECISION_MS = 500;

    public AbstractVsThreadVisualizer() {
        graphPanel.getGraphObject().setForcedMinX(0);
        graphPanel.getGraphObject().setxAxisLabel("Number of active threads");
    }

    @Override
    protected void setExtraChartSettings() {
        graphPanel.getGraphObject().getChartSettings().enableDrawCurrentX();
    }

    private void addCount(String tgName, int nbThread, long time) {
        AbstractGraphRow row = state.get(tgName);
        if (row == null) {
            row = getNewRow(state, AbstractGraphRow.ROW_AVERAGES, tgName, AbstractGraphRow.MARKER_SIZE_NONE, false, false, false, false, Color.BLACK, false);
            state.put(tgName, row);
        }
        row.add(time - time % PRECISION_MS, nbThread);
    }

    protected int getCurrentThreadCount(SampleResult res) {
        int ret;
        if (state.size() > 1) {
            ret = 0;
            for (AbstractGraphRow abstractGraphRow : state.values()) {
                AbstractGraphPanelChartElement element = abstractGraphRow.getLowerElement(res.getStartTime());
                if (element != null) ret += element.getValue();
            }
        } else {
            ret = res.getGroupThreads();
        }
        return ret;
    }


    @Override
    public void add(SampleResult res) {
        String threadName = res.getThreadName();
        threadName = threadName.lastIndexOf(" ") >= 0 ? threadName.substring(0, threadName.lastIndexOf(" ")) : threadName;
        // System.out.println(threadName);
        addCount(threadName, res.getGroupThreads(), res.getStartTime());
    }

    @Override
    public void clearData() {
        super.clearData();
        state.clear();
    }
}
