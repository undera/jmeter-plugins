/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package kg.apc.jmeter.listener;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.File;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import kg.apc.jmeter.PluginsCMDWorker;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.visualizers.Visualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Listener that generates graphs
 * @author <a href="p.mouawad at ubik-ingenierie.com">Philippe M.</a>
 * @since 1.1.3
 */
public class GraphsGeneratorListener extends AbstractListenerElement
    implements TestStateListener, TestBean, TestElement, Visualizer {

    private static final Logger log = LoggingManager.getLoggerForClass();

    public enum ExportMode {
        PNG((byte)0),
        CSV((byte)1);
        
        private byte value;
        private ExportMode(byte value) {
            this.value = value;
        }
        
        public byte getValue() {
            return value;
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = -136031193118302572L;
    private static final String[] pluginTypes = new String[] {
        "ResponseTimesOverTime",                //$NON-NLS-1$
        "HitsPerSecond",                        //$NON-NLS-1$
        "BytesThroughputOverTime",              //$NON-NLS-1$
        "LatenciesOverTime",                    //$NON-NLS-1$
        "ResponseCodesPerSecond",               //$NON-NLS-1$
        "TransactionsPerSecond",                //$NON-NLS-1$
        "ResponseTimesDistribution",            //$NON-NLS-1$
        "ResponseTimesPercentiles",             //$NON-NLS-1$
        "ThreadsStateOverTime",                 //$NON-NLS-1$
        "TimesVsThreads",                       //$NON-NLS-1$
        "ThroughputVsThreads"                   //$NON-NLS-1$
    };
    private static final Set<String> TIME_BASED_GRAPHS = new HashSet<String>();
    static {
        TIME_BASED_GRAPHS.add("ResponseTimesOverTime");     //$NON-NLS-1$
        TIME_BASED_GRAPHS.add("HitsPerSecond");             //$NON-NLS-1$
        TIME_BASED_GRAPHS.add("BytesThroughputOverTime");   //$NON-NLS-1$
        TIME_BASED_GRAPHS.add("LatenciesOverTime");         //$NON-NLS-1$
        TIME_BASED_GRAPHS.add("ResponseCodesPerSecond");    //$NON-NLS-1$
        TIME_BASED_GRAPHS.add("TransactionsPerSecond");     //$NON-NLS-1$
    }
    private String outputBaseFolder;
    private String resultsFileName;
    private ExportMode exportMode;
    private String filePrefix;
    private int graphWidth;
    private int graphHeight;
    private boolean aggregateRows;
    private String paintMarkers;
    private boolean paintZeroing;
    private boolean paintGradient;
    private boolean preventOutliers;
    private boolean relativeTimes;
    private boolean autoScaleRows;
    private String limitRows;
    private String forceY;
    private String granulation;
    private String lineWeight;
    private String lowCountLimit;
    private String successFilter;
    private String includeLabels;
    private String excludeLabels;

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestStateListener#testEnded()
     */
    @Override
    public void testEnded() {
        testEnded("");  //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestStateListener#testEnded(java.lang.String)
     */
    @Override
    public void testEnded(String host) {
        for (int i = 0; i < pluginTypes.length; i++) {
            PluginsCMDWorker worker = new PluginsCMDWorker();
            worker.setInputFile(resultsFileName);
            worker.setGraphWidth(graphWidth);
            worker.setGraphHeight(graphHeight);
            if(!StringUtils.isEmpty(forceY)) {
                worker.setForceY(Integer.parseInt(forceY));
            }
            if(!StringUtils.isEmpty(limitRows)) {
                worker.setRowsLimit(Integer.parseInt(limitRows));
            }
            worker.setAggregate(aggregateRows?1:0);
            worker.setPreventOutliers(preventOutliers?1:0);
            worker.setAggregate(aggregateRows?1:0);
            if(!StringUtils.isEmpty(paintMarkers)) {
                worker.setMarkers("True". //$NON-NLS-1$
                        equalsIgnoreCase(paintMarkers) ? 1 : 0);                
            }
            worker.setZeroing(paintZeroing?1:0);
            if(isTimeBasedGraph(pluginTypes[i])) {
                worker.setRelativeTimes(relativeTimes?1:0);                
            }
            worker.setGradient(paintGradient?1:0);
            worker.setAutoScaleRows(autoScaleRows?1:0);
            if(!StringUtils.isEmpty(successFilter)) {
                worker.setSuccessFilter(
                        "True". //$NON-NLS-1$
                        equalsIgnoreCase(successFilter) ? 1 : 0);
            }
            if(!StringUtils.isEmpty(granulation)) {
                worker.setGranulation(Integer.parseInt(granulation));
            }
            if(!StringUtils.isEmpty(lineWeight)) {
                worker.setLineWeight(Float.parseFloat(lineWeight));
            }
            if(!StringUtils.isEmpty(lowCountLimit)) {
                worker.setHideLowCounts(Integer.parseInt(lowCountLimit));
            }
            if(!StringUtils.isEmpty(includeLabels)) {
                worker.setIncludeLabels(includeLabels);
            }
            if(!StringUtils.isEmpty(excludeLabels)) {
                worker.setExcludeLabels(excludeLabels);
            }
            String fileName = null;
            if(!StringUtils.isEmpty(outputBaseFolder)) {
                fileName = outputBaseFolder+File.separatorChar+filePrefix+pluginTypes[i];
            } else {
                // Handle backward compatibility
                fileName = filePrefix+pluginTypes[i];                
            }
            if(exportMode==ExportMode.PNG) {
                fileName +=  ".png"; //$NON-NLS-1$
                worker.setOutputPNGFile(fileName); //$NON-NLS-1$
                worker.addExportMode(PluginsCMDWorker.EXPORT_PNG); 
            } else {
                fileName +=  ".csv"; //$NON-NLS-1$
                worker.setOutputCSVFile(fileName); //$NON-NLS-1$           
                worker.addExportMode(PluginsCMDWorker.EXPORT_CSV); 
            }
            worker.setPluginType(pluginTypes[i]);
            int status = worker.doJob();
            if(status == 0) {
                log.info("Successful generation of file "+fileName+" by plugin:"+pluginTypes[i]);
            } else {
                log.error("Error generating file "+fileName+" by plugin:"+pluginTypes[i]);                
            }
        }
    }

    /**
     * 
     * @param graphName
     * @return boolean
     */
    private static boolean isTimeBasedGraph(String graphName) {
        return TIME_BASED_GRAPHS.contains(graphName);
    }
    
    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestStateListener#testStarted()
     */
    @Override
    public void testStarted() {
        testStarted("");
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.testelement.TestStateListener#testStarted(java.lang.String)
     */
    @Override
    public void testStarted(String host) {
        // NOOP
    }
    


    @Override
    public void add(SampleResult result) {
        // NOOP
    }
    
    /**
     * Override the setProperty method in order to convert
     * the original String calcMode property.
     * This used the locale-dependent display value, so caused
     * problems when the language was changed.
     * Note that the calcMode StringProperty is replaced with an IntegerProperty
     * so the conversion only needs to happen once.
     */
    @Override
    public void setProperty(JMeterProperty property) {
        if (property instanceof StringProperty) {
            final String pn = property.getName();
            if (pn.equals("exportMode")) { //$NON-NLS-1$
                final Object objectValue = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for(Enum<ExportMode> e : ExportMode.values()) {
                        final String propName = e.toString();
                        if (objectValue.equals(rb.getObject(propName))) {
                            final int tmpMode = e.ordinal();
                            if (log .isDebugEnabled()) {
                                log.debug("Converted " + pn + "=" + objectValue + " to mode=" + tmpMode  + " using Locale: " + rb.getLocale());
                            }
                            super.setProperty(pn, tmpMode);
                            return;
                        }
                    }
                    log.warn("Could not convert " + pn + "=" + objectValue + " using Locale: " + rb.getLocale());
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            }
        }
        super.setProperty(property);
    }

    @Override
    public boolean isStats() {
        return false;
    }

    /**
     * @return the resultsFileName
     */
    public String getResultsFileName() {
        return resultsFileName;
    }

    /**
     * @param resultsFileName the resultsFileName to set
     */
    public void setResultsFileName(String resultsFileName) {
        this.resultsFileName = resultsFileName;
    }

    /**
     * @return the FilePrefix
     */
    public String getFilePrefix() {
        return filePrefix;
    }

    /**
     * @param FilePrefix the FilePrefix to set
     */
    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    /**
     * @return the graphWidth
     */
    public int getGraphWidth() {
        return graphWidth;
    }

    /**
     * @param graphWidth the graphWidth to set
     */
    public void setGraphWidth(int graphWidth) {
        this.graphWidth = graphWidth;
    }

    /**
     * @return the graphHeight
     */
    public int getGraphHeight() {
        return graphHeight;
    }

    /**
     * @param graphHeight the graphHeight to set
     */
    public void setGraphHeight(int graphHeight) {
        this.graphHeight = graphHeight;
    }

    /**
     * @return the aggregateRows
     */
    public boolean isAggregateRows() {
        return aggregateRows;
    }

    /**
     * @param aggregateRows the aggregateRows to set
     */
    public void setAggregateRows(boolean aggregateRows) {
        this.aggregateRows = aggregateRows;
    }

    /**
     * @return the paintZeroing
     */
    public boolean isPaintZeroing() {
        return paintZeroing;
    }

    /**
     * @param paintZeroing the paintZeroing to set
     */
    public void setPaintZeroing(boolean paintZeroing) {
        this.paintZeroing = paintZeroing;
    }

    /**
     * @return the paintGradient
     */
    public boolean isPaintGradient() {
        return paintGradient;
    }

    /**
     * @param paintGradient the paintGradient to set
     */
    public void setPaintGradient(boolean paintGradient) {
        this.paintGradient = paintGradient;
    }

    /**
     * @return the preventOutliers
     */
    public boolean isPreventOutliers() {
        return preventOutliers;
    }

    /**
     * @param preventOutliers the preventOutliers to set
     */
    public void setPreventOutliers(boolean preventOutliers) {
        this.preventOutliers = preventOutliers;
    }

    /**
     * @return the relativeTimes
     */
    public boolean isRelativeTimes() {
        return relativeTimes;
    }

    /**
     * @param relativeTimes the relativeTimes to set
     */
    public void setRelativeTimes(boolean relativeTimes) {
        this.relativeTimes = relativeTimes;
    }

    /**
     * @return the autoScaleRows
     */
    public boolean isAutoScaleRows() {
        return autoScaleRows;
    }

    /**
     * @param autoScaleRows the autoScaleRows to set
     */
    public void setAutoScaleRows(boolean autoScaleRows) {
        this.autoScaleRows = autoScaleRows;
    }

    /**
     * @return the limitRows
     */
    public String getLimitRows() {
        return limitRows;
    }

    /**
     * @param limitRows the limitRows to set
     */
    public void setLimitRows(String limitRows) {
        this.limitRows = limitRows;
    }

    /**
     * @return the forceY
     */
    public String getForceY() {
        return forceY;
    }

    /**
     * @param forceY the forceY to set
     */
    public void setForceY(String forceY) {
        this.forceY = forceY;
    }

    /**
     * @return the granulation
     */
    public String getGranulation() {
        return granulation;
    }

    /**
     * @param granulation the granulation to set
     */
    public void setGranulation(String granulation) {
        this.granulation = granulation;
    }

    /**
     * @return the lineWeight
     */
    public String getLineWeight() {
        return lineWeight;
    }

    /**
     * @param lineWeight the lineWeight to set
     */
    public void setLineWeight(String lineWeight) {
        this.lineWeight = lineWeight;
    }

    /**
     * @return the lowCountLimit
     */
    public String getLowCountLimit() {
        return lowCountLimit;
    }

    /**
     * @param lowCountLimit the lowCountLimit to set
     */
    public void setLowCountLimit(String lowCountLimit) {
        this.lowCountLimit = lowCountLimit;
    }

    /**
     * @return the successFilter
     */
    public String getSuccessFilter() {
        return successFilter;
    }

    /**
     * @param successFilter the successFilter to set
     */
    public void setSuccessFilter(String successFilter) {
        this.successFilter = successFilter;
    }

    /**
     * @return the includeLabels
     */
    public String getIncludeLabels() {
        return includeLabels;
    }

    /**
     * @param includeLabels the includeLabels to set
     */
    public void setIncludeLabels(String includeLabels) {
        this.includeLabels = includeLabels;
    }

    /**
     * @return the excludeLabels
     */
    public String getExcludeLabels() {
        return excludeLabels;
    }

    /**
     * @param excludeLabels the excludeLabels to set
     */
    public void setExcludeLabels(String excludeLabels) {
        this.excludeLabels = excludeLabels;
    }

    /**
     * @return the exportMode
     */
    public int getExportMode() {
        return exportMode.ordinal();
    }

    /**
     * @param exportMode the exportMode to set
     */
    public void setExportMode(int exportMode) {
        this.exportMode = ExportMode.values()[exportMode];
    }

    /**
     * @return the outputBaseFolder
     */
    public String getOutputBaseFolder() {
        return outputBaseFolder;
    }

    /**
     * @param outputBaseFolder the outputBaseFolder to set
     */
    public void setOutputBaseFolder(String outputBaseFolder) {
        this.outputBaseFolder = outputBaseFolder;
    }

    /**
     * @return the paintMarkers
     */
    public String getPaintMarkers() {
        return paintMarkers;
    }

    /**
     * @param paintMarkers the paintMarkers to set
     */
    public void setPaintMarkers(String paintMarkers) {
        this.paintMarkers = paintMarkers;
    }
}
