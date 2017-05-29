package kg.apc.jmeter.graphs;

import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.JPanel;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.vizualizers.JSettingsPanel;
import kg.apc.jmeter.vizualizers.MonitoringResultsCollector;
import kg.apc.jmeter.vizualizers.MonitoringSampleResult;
import org.apache.jmeter.testelement.TestElement;
import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractMonitoringVisualizerTest {

    public class AbstractMonitoringVisualizerImpl
            extends AbstractMonitoringVisualizer {
        
        @Override
        protected String[] getColumnIdentifiers() { return new String[]{ "Label", "URL", "Flag", "Value" }; }
        @Override
        protected Class[] getColumnClasses() { return new Class[]{ String.class, String.class, Boolean.class, Integer.class }; }
        @Override
        protected Object[] getDefaultValues() { return new Object[]{ "", "", Boolean.FALSE, new Integer(0) }; }
        @Override
        protected int[] getColumnWidths() { return new int[]{ 100, 200, 20, 50 }; }

        @Override
        protected MonitoringResultsCollector createMonitoringResultsCollector() { return new MonitoringResultsCollector();}
        
        @Override
        public String getWikiPage() { return "Test"; }

        @Override
        public String getStaticLabel() { return "AbstractMonitoringVisualizerImpl"; }

        public ConcurrentSkipListMap<String, AbstractGraphRow> getModelMulti() {
            return model;
        }

        public ConcurrentSkipListMap<String, AbstractGraphRow> getModelAggr() {
            return modelAggregate;
        }
    }

    @Test
    public void testCreateSettingsPanel() {
        System.out.println("createSettingsPanel");
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        JSettingsPanel result = instance.createSettingsPanel();
        assertNotNull(result);
    }

    @Test
    public void testGetWikiPage() {
        System.out.println("getWikiPage");
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        String result = instance.getWikiPage();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetLabelResource() {
        System.out.println("getLabelResource");
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        String result = instance.getLabelResource();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testGetStaticLabel() {
        System.out.println("getStaticLabel");
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        String result = instance.getStaticLabel();
        assertTrue(result.length() > 0);
    }

    @Test
    public void testCreateTestElement() {
        System.out.println("createTestElement");
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        TestElement result = instance.createTestElement();
        assertTrue(result instanceof MonitoringResultsCollector);
    }

    @Test
    public void testModifyTestElement() {
        System.out.println("modifyTestElement");
        TestElement c = new MonitoringResultsCollector();
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        instance.modifyTestElement(c);
        assertNotNull(c.getProperty(MonitoringResultsCollector.DATA_PROPERTY));
    }

    @Test
    public void testConfigure() {
        System.out.println("configure");
        TestElement el = new MonitoringResultsCollector();
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        instance.configure(el);
        assertNotNull(instance.tableModel);
    }

    @Test
    public void testAdd() {
        System.out.println("add");
        MonitoringSampleResult res = new MonitoringSampleResult();
        res.setSuccessful(true);
        res.setValue(1.0);
        AbstractMonitoringVisualizerImpl instance = new AbstractMonitoringVisualizerImpl();
        instance.add(res);
        assertEquals(1, instance.getModelMulti().size());
        assertEquals(1, instance.getModelMulti().firstEntry().getValue().size());
        assertEquals(0, instance.getModelAggr().size());
    }

    @Test
    public void testGetGraphPanelContainer() {
        System.out.println("getGraphPanelContainer");
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        JPanel result = instance.getGraphPanelContainer();
        assertNotNull(result);
    }

    @Test
    public void testClearErrorMessage() {
        System.out.println("clearErrorMessage");
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        instance.clearErrorMessage();
        assertEquals(instance.errorTextArea.getText().length(), 0);
    }

    @Test
    public void testClearData() {
        System.out.println("clearData");
        AbstractMonitoringVisualizer instance = new AbstractMonitoringVisualizerImpl();
        instance.clearData();
        assertEquals(instance.relativeStartTime, 0);
    }
}
