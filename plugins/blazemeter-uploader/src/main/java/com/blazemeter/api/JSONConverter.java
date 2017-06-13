package com.blazemeter.api;

import net.sf.json.JSONObject;
import org.apache.jmeter.samplers.SampleResult;
import org.json.simple.JSONArray;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JSONConverter {

    public static final String  SUMMARY_LABEL = "ALL";

    public static JSONArray convertToJSON(List<SampleResult> list) {
        JSONArray jsonArray = new JSONArray();

        jsonArray.add(caclucateMetrics(list, SUMMARY_LABEL));

        Map<String, List<SampleResult>> sortedSamples = sortSamplesByLabel(list);

        for (String label : sortedSamples.keySet()) {
            jsonArray.add(caclucateMetrics(sortedSamples.get(label), label));
        }

        return jsonArray;
    }

    private static Map<String, List<SampleResult>> sortSamplesByLabel(List<SampleResult> list) {
        final Map<String, List<SampleResult>> result = new HashMap<>();

        for (SampleResult sample : list) {
            String label = sample.getSampleLabel();
            if (!result.containsKey(label)) {
                result.put(label, new LinkedList<SampleResult>());
            }
            result.get(label).add(sample);
        }

        return result;
    }

    private static JSONObject caclucateMetrics(List<SampleResult> list, String label) {
        final JSONObject res = new JSONObject();

        res.put("n", list.size());                              // total count of samples
        res.put("name", label);                                 // label
        res.put("interval", 1);                                 // not used
        res.put("intervals", calculateIntervals(list));         // list of intervals
        res.put("samplesNotCounted", 0);                        // not used
        res.put("assertionsNotCounted", 0);                     // not used
        res.put("failedEmbeddedResources", "[]");               // not used
        res.put("failedEmbeddedResourcesSpilloverCount", 0);    // not used
        res.put("otherErrorsCount", 0);                         // not used
        res.put("errors", generateErrorList(list));             // list of errors
        res.put("assertions", generateAssertionsList(list));    // list of assertions
        res.put("percentileHistogram", "[]");                   // not used
        res.put("percentileHistogramLatency", "[]");            // not used
        res.put("percentileHistogramBytes", "[]");              // not used
        res.put("empty", "[]");                                 // not used
        res.put("summary", generateSummary(list));              // summary info

        return res;
    }

    /**

     "first": self.owner.first_ts,
     "last": self.owner.last_ts,
     "duration": self.owner.last_ts - self.owner.first_ts,
     "failed": cumul[KPISet.FAILURES],
     "hits": cumul[KPISet.SAMPLE_COUNT],

     "avg": int(1000 * cumul[KPISet.AVG_RESP_TIME]),
     "min": int(1000 * cumul[KPISet.PERCENTILES]["0.0"]) if "0.0" in cumul[KPISet.PERCENTILES] else 0,
     "max": int(1000 * cumul[KPISet.PERCENTILES]["100.0"]) if "100.0" in cumul[KPISet.PERCENTILES] else 0,
     "std": int(1000 * cumul[KPISet.STDEV_RESP_TIME]),
     "tp90": int(1000 * cumul[KPISet.PERCENTILES]["90.0"]) if "90.0" in cumul[KPISet.PERCENTILES] else 0,
     "tp95": int(1000 * cumul[KPISet.PERCENTILES]["95.0"]) if "95.0" in cumul[KPISet.PERCENTILES] else 0,
     "tp99": int(1000 * cumul[KPISet.PERCENTILES]["99.0"]) if "99.0" in cumul[KPISet.PERCENTILES] else 0,

     "latencyAvg": int(1000 * cumul[KPISet.AVG_LATENCY]),
     "latencyMax": 0,
     "latencyMin": 0,
     "latencySTD": 0,

     "bytes": cumul[KPISet.BYTE_COUNT],
     "bytesMax": 0,
     "bytesMin": 0,
     "bytesAvg": int(cumul[KPISet.BYTE_COUNT] / float(cumul[KPISet.SAMPLE_COUNT])),
     "bytesSTD": 0,

     "otherErrorsSpillcount": 0,
     */
    private static Object generateSummary(List<SampleResult> list) {
        return null;
    }

    private static Object generateAssertionsList(List<SampleResult> list) {
        return null;
    }

    private static Object generateErrorList(List<SampleResult> list) {
        return null;
    }

    private static Object calculateIntervals(List<SampleResult> list) {
        return null;
    }
}
