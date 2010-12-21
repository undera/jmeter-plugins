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
        log.info("Adding: "+this.getClass().getName());

        GuiPackage gui = GuiPackage.getInstance();
        HashTree testTree = gui.getTreeModel().getTestPlan();

        SearchByClass<CompositeRowsProvider> searchRowProviders = new SearchByClass<CompositeRowsProvider>(CompositeRowsProvider.class);
        testTree.traverse(searchRowProviders);

        Iterator it = searchRowProviders.getSearchResults().iterator();
        while (it.hasNext()) {
            Object provider = it.next();
            log.info(provider.toString());
        }
    }
}
