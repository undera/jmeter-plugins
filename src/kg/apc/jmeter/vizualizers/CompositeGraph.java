package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class CompositeGraph extends AbstractGraphPanelVisualizer {

    private static final Logger log = LoggingManager.getLoggerForClass();

    @Override
    protected JSettingsPanel getSettingsPanel() {
        return new JSettingsPanel(this, true, true, true, true, true);
    }

    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel() {
        return "Composite Graph";
    }

    public void add(SampleResult sr) {
        GuiPackage gui = GuiPackage.getInstance();
        HashTree testTree = gui.getTreeModel().getTestPlan();

        SearchByClass<RowsProviderResultCollector> searchRowProviders = new SearchByClass<RowsProviderResultCollector>(RowsProviderResultCollector.class);
        log.info("testTree: " + testTree);
        testTree .traverse(searchRowProviders);

        log.info("Size: " + searchRowProviders.getSearchResults().size());

        Iterator it = searchRowProviders.getSearchResults().iterator();
        while (it.hasNext()) {
            RowsProviderResultCollector provider = (RowsProviderResultCollector) it.next();
            log.info(Integer.toString(provider.getRowNames().size()));
        }
    }
}
