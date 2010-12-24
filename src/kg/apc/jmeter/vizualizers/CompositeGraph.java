package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.samplers.SampleResult;
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
        JMeterTreeModel testTree = gui.getTreeModel();

        Iterator it = testTree.getNodesOfType(RowsProviderResultCollector.class).iterator();
        while (it.hasNext()) {
            Object obj=it.next();
            log.info(obj.getClass().getCanonicalName());
            if (((JMeterTreeNode) obj).getTestElement() instanceof RowsProviderResultCollector)
            {
                RowsProviderResultCollector provider = (RowsProviderResultCollector)((JMeterTreeNode) obj).getTestElement();
                log.info("Size: "+provider.getRowNames().size());

                Iterator it2=provider.getRowNames().iterator();
                while(it2.hasNext())
                {
                    String rowName=(String) it2.next();
                    log.info(rowName);
                }
            }
        }
    }
}
