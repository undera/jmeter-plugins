package org.jmeterplugins.tools;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jmeterplugins.save.MergeResultsService;

import kg.apc.jmeter.graphs.AbstractGraphPanelVisualizer;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import kg.apc.jmeter.vizualizers.JSettingsPanel;

/**
 *
 */
public class FilterResults extends AbstractGraphPanelVisualizer {
    private static final long serialVersionUID = 6432873068917332588L;

    private static final Logger log = LoggingManager.getLoggerForClass();
    private Collection<String> emptyCollection = new ArrayList<String>();

    private List<SampleResult> samples = new ArrayList<SampleResult>();


    public FilterResults() {
        super();
        init();
    }

    /**
     * Main visualizer setup.
     */
    private void init() {
        this.setLayout(new BorderLayout());

        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        Border margin = new EmptyBorder(10, 10, 5, 10);

        mainPanel.setBorder(margin);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(makeTitlePanel());
    }

    // do not insert this vizualiser in any JMeter menu
    @Override
    public Collection<String> getMenuCategories() {
        return emptyCollection;
    }
    
    

    public void add(SampleResult res) {
        if (!isSampleIncluded(res)) {
            return;
        }

        res.setSaveConfig(collector.getSaveConfig());
        samples.add(res);
    }

    public CorrectedResultCollector getCollector() {
    	return (CorrectedResultCollector) createTestElement();
    }
    
    public int doJob(CorrectedResultCollector collector, String outputFile) {
        log.info("Setup filtering...");
        setUpFiltering((CorrectedResultCollector) collector);
        log.info("Loading file...");
        collector.loadExistingFile();

        if (!samples.isEmpty()) {
            log.info("Merging results to " + outputFile);
            collector.setProperty("filename", outputFile);
            MergeResultsService mrs = new MergeResultsService();
            mrs.mergeSamples((CorrectedResultCollector) collector, samples);
            samples.clear();
        }
        return 0;
    }

    @Override
    public String getWikiPage() {
        return "FilterResults";
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName(); //$NON-NLS-1$
    }

    @Override
    protected JSettingsPanel createSettingsPanel() {
        return new JSettingsPanel(this, 0);
    }

    @Override
    public String getStaticLabel() {
        return "Nobody never should not see this. No, no, no.";
    }


}
