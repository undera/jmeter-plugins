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

package org.jmeterplugins.visualizers.gui;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;

import kg.apc.jmeter.vizualizers.CorrectedResultCollector;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Felix Henry
 * @author Vincent Daburon
 */
public abstract class AbstractFilterableVisualizer extends AbstractVisualizer {
    private static final long serialVersionUID = 240L;

    /** Logging. */
    private static final Logger log = LoggingManager.getLoggerForClass();

    protected FilterPanel jPanelFilter;

    private List<String> includes = new ArrayList<String>(0);
    private List<String> excludes = new ArrayList<String>(0);
    private String incRegex;
    private String excRegex;
    private boolean includeRegexChkboxState;
    private boolean excludeRegexChkboxState;
    protected long startTimeRef = 0;
    protected long startTimeInf;
    protected long startTimeSup;
    private long startOffset;
    private long endOffset;

    public AbstractFilterableVisualizer() {
        super();
        jPanelFilter = new FilterPanel();
    }

    /**
     * Invoked when the target of the listener has changed its state. This
     * implementation assumes that the target is the FilePanel, and will update
     * the result collector for the new filename.
     *
     * @param e
     *            the event that has occurred
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        log.debug("getting new collector");
        collector = (CorrectedResultCollector) createTestElement();
        if (collector instanceof CorrectedResultCollector) {
            setUpFiltering((CorrectedResultCollector) collector);
        }
        collector.loadExistingFile();
    }

    @Override
    public TestElement createTestElement() {
        if (collector == null
                || !(collector instanceof CorrectedResultCollector)) {
            collector = new CorrectedResultCollector();
        }
        return super.createTestElement();
    }

    @Override
    public void modifyTestElement(TestElement c) {
        super.modifyTestElement(c);
        c.setProperty(new StringProperty(
                CorrectedResultCollector.INCLUDE_SAMPLE_LABELS, jPanelFilter
                        .getIncludeSampleLabels()));
        c.setProperty(new StringProperty(
                CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS, jPanelFilter
                        .getExcludeSampleLabels()));

        c.setProperty(new StringProperty(CorrectedResultCollector.START_OFFSET,
                jPanelFilter.getStartOffset()));
        c.setProperty(new StringProperty(CorrectedResultCollector.END_OFFSET,
                jPanelFilter.getEndOffset()));

        c.setProperty(new BooleanProperty(
                CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE,
                jPanelFilter.isSelectedRegExpInc()));
        c.setProperty(new BooleanProperty(
                CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE,
                jPanelFilter.isSelectedRegExpExc()));
    }

    @Override
    public void configure(TestElement el) {
        super.configure(el);

        jPanelFilter
                .setIncludeSampleLabels(el
                        .getPropertyAsString(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS));
        jPanelFilter
                .setExcludeSampleLabels(el
                        .getPropertyAsString(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS));

        if (!CorrectedResultCollector.EMPTY_FIELD.equals(el
                .getPropertyAsString(CorrectedResultCollector.START_OFFSET))) {
            jPanelFilter.setStartOffset((el
                    .getPropertyAsLong(CorrectedResultCollector.START_OFFSET)));
        }
        if (!CorrectedResultCollector.EMPTY_FIELD.equals(el
                .getPropertyAsString(CorrectedResultCollector.END_OFFSET))) {
            jPanelFilter.setEndOffset((el
                    .getPropertyAsLong(CorrectedResultCollector.END_OFFSET)));
        }

        jPanelFilter
                .setSelectedRegExpInc(el
                        .getPropertyAsBoolean(CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE));
        jPanelFilter
                .setSelectedRegExpExc(el
                        .getPropertyAsBoolean(CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE));

        if (el instanceof CorrectedResultCollector) {
            setUpFiltering((CorrectedResultCollector) el);
        }
    }

    protected boolean isSampleIncluded(SampleResult res) {
        if (null == res) {
            return true;
        }

        if (startTimeRef == 0) {
            startTimeRef = res.getStartTime();
            startTimeInf = startTimeRef - startTimeRef % 1000;
            startTimeSup = startTimeRef + (1000 - startTimeRef % 1000) % 1000;
        }

        if (includeRegexChkboxState && !incRegex.isEmpty()
                && !res.getSampleLabel().matches(incRegex)) {
            return false;
        }

        if (excludeRegexChkboxState && !excRegex.isEmpty()
                && res.getSampleLabel().matches(excRegex)) {
            return false;
        }

        if (!includeRegexChkboxState && !includes.isEmpty()
                && !includes.contains(res.getSampleLabel())) {
            return false;
        }

        if (!excludeRegexChkboxState && !excludes.isEmpty()
                && excludes.contains(res.getSampleLabel())) {
            return false;
        }

        if (startOffset > res.getStartTime() - startTimeInf) {
            return false;
        }

        if (endOffset < res.getStartTime() - startTimeSup) {
            return false;
        }
        return true;
    }

    protected boolean isSampleIncluded(String sampleLabel) {
        if (includeRegexChkboxState && !incRegex.isEmpty()
                && !sampleLabel.matches(incRegex)) {
            return false;
        }

        if (excludeRegexChkboxState && !excRegex.isEmpty()
                && sampleLabel.matches(excRegex)) {
            return false;
        }

        if (!includeRegexChkboxState && !includes.isEmpty()
                && !includes.contains(sampleLabel)) {
            return false;
        }

        if (!excludeRegexChkboxState && !excludes.isEmpty()
                && excludes.contains(sampleLabel)) {
            return false;
        }
        return true;
    }

    public void setUpFiltering(CorrectedResultCollector rc) {
        startOffset = rc.getTimeDelimiter(
                CorrectedResultCollector.START_OFFSET, Long.MIN_VALUE);
        endOffset = rc.getTimeDelimiter(CorrectedResultCollector.END_OFFSET,
                Long.MAX_VALUE);
        includeRegexChkboxState = rc
                .getRegexChkboxState(CorrectedResultCollector.INCLUDE_REGEX_CHECKBOX_STATE);
        excludeRegexChkboxState = rc
                .getRegexChkboxState(CorrectedResultCollector.EXCLUDE_REGEX_CHECKBOX_STATE);
        if (includeRegexChkboxState)
            incRegex = rc
                    .getRegex(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS);
        else
            includes = rc
                    .getList(CorrectedResultCollector.INCLUDE_SAMPLE_LABELS);
        if (excludeRegexChkboxState)
            excRegex = rc
                    .getRegex(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS);
        else
            excludes = rc
                    .getList(CorrectedResultCollector.EXCLUDE_SAMPLE_LABELS);
    }

    @Override
    protected Container makeTitlePanel() {
        Container panel = super.makeTitlePanel();
        panel.add(jPanelFilter);
        return panel;
    }

    @Override
    public void clearGui() {
        super.clearGui();
        jPanelFilter.clearGui();
    }
}