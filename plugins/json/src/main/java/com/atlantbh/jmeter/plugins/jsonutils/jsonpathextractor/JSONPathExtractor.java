/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.jsonutils.jsonpathextractor;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * This is main class for JSONPath extractor which works on previous sample
 * result and extracts value from JSON output using JSONPath
 */
public class JSONPathExtractor extends AbstractTestElement implements PostProcessor {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 1L;

    public static final String JSONPATH = "JSONPATH";
    public static final String VAR = "VAR";
    public static final String DEFAULT = "DEFAULT";
    public static final String SUBJECT = "SUBJECT";
    public static final String SRC_VARNAME = "VARIABLE";

    public static final String SUBJECT_BODY = "BODY";
    public static final String SUBJECT_VARIABLE = "VAR";

    public static final DecimalFormat decimalFormatter = new DecimalFormat("#.#");
    static {
        decimalFormatter.setMaximumFractionDigits(340); // java.text.DecimalFormat.DOUBLE_FRACTION_DIGITS == 340
        decimalFormatter.setMinimumFractionDigits(1);
    }

    public JSONPathExtractor() {
        super();
    }

    public String getJsonPath() {
        return getPropertyAsString(JSONPATH);
    }

    public void setJsonPath(String jsonPath) {
        setProperty(JSONPATH, jsonPath);
    }

    public String getVar() {
        return getPropertyAsString(VAR);
    }

    public void setVar(String var) {
        setProperty(VAR, var);
    }

    public void setDefaultValue(String defaultValue) {
        setProperty(DEFAULT, defaultValue);
    }

    public String getDefaultValue() {
        return getPropertyAsString(DEFAULT);
    }

    public void setSrcVariableName(String defaultValue) {
        setProperty(SRC_VARNAME, defaultValue);
    }

    public String getSrcVariableName() {
        return getPropertyAsString(SRC_VARNAME);
    }

    public void setSubject(String defaultValue) {
        setProperty(SUBJECT, defaultValue);
    }

    public String getSubject() {
        return getPropertyAsString(SUBJECT);
    }

    @Override
    public void process() {
        JMeterContext context = getThreadContext();
        JMeterVariables vars = context.getVariables();
        SampleResult previousResult = context.getPreviousResult();
        String responseData;
        if (getSubject().equals(SUBJECT_VARIABLE)) {
            responseData = vars.get(getSrcVariableName());
        } else {
            responseData = previousResult.getResponseDataAsString();
        }


        try {
            Object jsonPathResult = JsonPath.read(responseData, getJsonPath());
            if (jsonPathResult instanceof JSONArray) {
                Object[] arr = ((JSONArray) jsonPathResult).toArray();

                if (arr.length == 0) {
                    throw new PathNotFoundException("Extracted array is empty");
                }

                vars.put(this.getVar(), objectToString(jsonPathResult));
                vars.put(this.getVar() + "_matchNr", objectToString(arr.length));

                int k = 1;
                while (vars.get(this.getVar() + "_" + k) != null) {
                    vars.remove(this.getVar() + "_" + k);
                    k++;
                }

                for (int n = 0; n < arr.length; n++) {
                    vars.put(this.getVar() + "_" + (n + 1), objectToString(arr[n]));
                }
            } else {
                vars.put(this.getVar(), objectToString(jsonPathResult));
            }
        } catch (Exception e) {
            log.debug("Extract failed", e);
            vars.put(this.getVar(), getDefaultValue());
            vars.put(this.getVar() + "_matchNr", "0");
            int k = 1;
            while (vars.get(this.getVar() + "_" + k) != null) {
                vars.remove(this.getVar() + "_" + k);
                k++;
            }
        }
    }

    public static String objectToString(Object subj) {
        String str;
        if (subj == null) {
            str = "null";
        } else if (subj instanceof Map) {
            //noinspection unchecked
            str = new JSONObject((Map<String, ?>) subj).toJSONString();
        } else if (subj instanceof Double || subj instanceof Float) {
            str = decimalFormatter.format(subj);
        } else {
            str = subj.toString();
        }
        return str;
    }
}
