package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.GraphPanelChart;

/**
 *
 * @author Stephane Hoblingre
 */
public interface SettingsInterface {
    public int getGranulation();
    public void setGranulation(int granulation);
    public GraphPanelChart getGraphPanelChart();
    public void switchModel(boolean aggregate);
    public String getWikiPage();
}
