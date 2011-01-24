package kg.apc.jmeter.vizualizers;

import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane Hoblingre
 */
public class TotalTransactionsPerSecondGui
        extends AbstractGraphPanelVisualizer
{
    private static String obsoleteMsg = "This chart is obsolete. Please replace it with \"Transactions per Seconds\", and select \"Aggregated display\" in the settings panel.";

    /**
     *
     */
    public TotalTransactionsPerSecondGui()
    {
        super();
        graphPanel.getGraphObject().setErrorMessage(obsoleteMsg);
    }

    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Total Transactions Per Second (obsolete)";
    }

    public void add(SampleResult res)
    {
        graphPanel.getGraphObject().setErrorMessage(obsoleteMsg);
        updateGui(null);
    }

    @Override
    protected JSettingsPanel getSettingsPanel()
    {
        return new JSettingsPanel(this, false, true, false, false, false);
    }
}
