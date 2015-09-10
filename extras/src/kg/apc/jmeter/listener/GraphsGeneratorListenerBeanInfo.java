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

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * BeanInfo for {@link GraphsGeneratorListener}
 *
 * @since 1.1.3
 */
public class GraphsGeneratorListenerBeanInfo extends BeanInfoSupport {

    private static final Logger LOGGER = LoggingManager.getLoggerForClass();
    // These names must agree case-wise with the variable and property names
    private static final String OUTPUT_BASE_FOLDER = "outputBaseFolder";
    private static final String RESULTS_FILE_NAME = "resultsFileName";
    private static final String FILE_PREFIX = "filePrefix";
    private static final String EXPORT_MODE = "exportMode";

    private static final String GRAPH_WIDTH = "graphWidth";
    private static final String GRAPH_HEIGHT = "graphHeight";
    private static final String PAINT_MARKERS = "paintMarkers";
    private static final String PAINT_ZEROING = "paintZeroing";
    private static final String PAINT_GRADIENT = "paintGradient";
    private static final String PREVENT_OUTLIERS = "preventOutliers";
    private static final String RELATIVE_TIMES = "relativeTimes";
    private static final String AUTO_SCALE_ROWS = "autoScaleRows";
    private static final String LIMIT_ROWS = "limitRows";
    private static final String FORCE_Y = "forceY";
    private static final String GRANULATION = "granulation";
    private static final String LINE_WEIGHT = "lineWeight";

    private static final String AGGREGATE_ROWS = "aggregateRows";
    private static final String LOW_COUNT_LIMIT = "lowCountLimit";
    private static final String SUCCESS_FILTER = "successFilter";
    private static final String INCLUDE_LABELS = "includeLabels";
    private static final String EXCLUDE_LABELS = "excludeLabels";

    static final String[] FILTER_TAGS = new String[]{
            "",
            "True",
            "False"
    };
    static final int FILTER_NONE = 0;
    static final int FILTER_TRUE = 1;
    static final int FILTER_FALSE = 2;

    private static final String INCLUDE_SAMPLES_WITH_REGEX = "includeSamplesWithRegex";
    private static final String EXCLUDE_SAMPLES_WITH_REGEX = "excludeSamplesWithRegex";
    private static final String START_OFFSET = "startOffset";
    private static final String END_OFFSET = "endOffset";

    public GraphsGeneratorListenerBeanInfo() {
        super(GraphsGeneratorListener.class);
        try {
            createPropertyGroup("output_config",
                    new String[]{OUTPUT_BASE_FOLDER, RESULTS_FILE_NAME, EXPORT_MODE, FILE_PREFIX});

            PropertyDescriptor p = property(OUTPUT_BASE_FOLDER);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(RESULTS_FILE_NAME);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(EXPORT_MODE, GraphsGeneratorListener.ExportMode.class);
            p.setValue(DEFAULT, GraphsGeneratorListener.ExportMode.PNG.ordinal());
            p.setValue(NOT_UNDEFINED, Boolean.TRUE); // must be defined

            p = property(FILE_PREFIX);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);


            createPropertyGroup("graphs_config",
                    new String[]{GRAPH_WIDTH, GRAPH_HEIGHT, PAINT_MARKERS, PAINT_ZEROING,
                            PAINT_GRADIENT, PREVENT_OUTLIERS, RELATIVE_TIMES, AUTO_SCALE_ROWS,
                            LIMIT_ROWS, FORCE_Y, GRANULATION, LINE_WEIGHT});

            p = property(GRAPH_WIDTH);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "800");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(GRAPH_HEIGHT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "600");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(PAINT_MARKERS);
            p.setValue(NOT_UNDEFINED, Boolean.FALSE);
            p.setValue(DEFAULT, "Undefined");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.FALSE);
            p.setValue(TAGS, new String[]{"True", "False"});

            p = property(PAINT_ZEROING);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.TRUE);
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(PAINT_GRADIENT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.TRUE);
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(PREVENT_OUTLIERS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(RELATIVE_TIMES);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(AUTO_SCALE_ROWS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(LIMIT_ROWS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "150");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(FORCE_Y);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(GRANULATION);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "60000");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(LINE_WEIGHT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            createPropertyGroup("filtering_config",
                    new String[]{AGGREGATE_ROWS, LOW_COUNT_LIMIT,
                            SUCCESS_FILTER, INCLUDE_SAMPLES_WITH_REGEX,
                            INCLUDE_LABELS, EXCLUDE_SAMPLES_WITH_REGEX,
                            EXCLUDE_LABELS, START_OFFSET, END_OFFSET});

            p = property(AGGREGATE_ROWS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(LOW_COUNT_LIMIT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(SUCCESS_FILTER);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, FILTER_TAGS[FILTER_NONE]);
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);
            p.setValue(TAGS, FILTER_TAGS);
            p.setValue(NOT_OTHER, Boolean.FALSE);

            p = property(INCLUDE_SAMPLES_WITH_REGEX);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(INCLUDE_LABELS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(EXCLUDE_SAMPLES_WITH_REGEX);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(EXCLUDE_LABELS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(START_OFFSET);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

            p = property(END_OFFSET);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.FALSE);

        } catch (NoSuchMethodError e) {
            LOGGER.error("Error initializing component GraphGeneratorListener due to missing method, if your version is lower than 2.10, this" +
                    "is expected to fail, if not check project dependencies");
        }
    }
}
