package com.blazemeter.api;

import net.sf.json.JSONObject;
import org.apache.jmeter.samplers.SampleResult;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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


    private static JSONObject generateSummary(List<SampleResult> list) {
        final JSONObject summaryValues = new JSONObject();

        long first = Long.MAX_VALUE;
        long last = 0;
        long failed = 0;
        long bytesCount = 0;
        long sumTime = 0;
        long sumLatency = 0;
        Long[] rtimes = new Long[list.size()];
        int counter = 0;

        for (SampleResult sample : list) {
            long endTime = sample.getEndTime();

            if (endTime < first) {
                first = endTime;
            }

            if (endTime > last) {
                last = endTime;
            }

            if (!sample.isSuccessful()) {
                failed++;
            }

            bytesCount += sample.getBytes();
            sumTime += sample.getTime();
            sumLatency += sample.getLatency();
            rtimes[counter] = sample.getTime();
            counter++;
        }

        summaryValues.put("first", first);  // TODO: cast to sec?
        summaryValues.put("last", last);     // TODO: cast to sec?
        summaryValues.put("duration", last - first); // TODO: cast to sec?
        summaryValues.put("failed", failed);
        summaryValues.put("hits", list.size());

        long average = sumTime / list.size();
        summaryValues.put("avg", average);
        Map<String, Long>  percentiles = getQuantiles(rtimes, average);
        summaryValues.put("min", percentiles.containsKey("pect_0.0") ? percentiles.get("perc_0.0") : 0);
        summaryValues.put("max", percentiles.containsKey("pect_100.0") ? percentiles.get("perc_100.0") : 0);
        summaryValues.put("tp90", percentiles.containsKey("pect_90.0") ? percentiles.get("perc_90.0") : 0);
        summaryValues.put("tp95", percentiles.containsKey("pect_95.0") ? percentiles.get("perc_95.0") : 0);
        summaryValues.put("tp99", percentiles.containsKey("pect_99.0") ? percentiles.get("perc_99.0") : 0);
        summaryValues.put("std", percentiles.containsKey("stdev") ? percentiles.get("stdev") : 0);

        summaryValues.put("latencyAvg", sumLatency / list.size());
        summaryValues.put("latencyMax", 0L);
        summaryValues.put("latencyMin", 0L);
        summaryValues.put("latencySTD", 0L);

        summaryValues.put("bytes", bytesCount);
        summaryValues.put("bytesMax", 0L);
        summaryValues.put("bytesMin", 0L);
        summaryValues.put("bytesAvg", bytesCount / list.size());
        summaryValues.put("bytesSTD", 0L);

        summaryValues.put("otherErrorsSpillcount", 0L);

        return summaryValues;
    }


    public static Map<String, Long> getQuantiles(Long[] rtimes, long average) {
        Map<String, Long> result = new JSONObject();
        Arrays.sort(rtimes);

        double[] quantiles = {0.0, 0.25, 0.50, 0.75, 0.80, 0.90, 0.95, 0.98, 0.99, 1.00};

        Stack<Long> timings = new Stack<>();
        timings.addAll(Arrays.asList(rtimes));
        double level = 1.0;
        Long timing = 0L;
        long sqr_diffs = 0;
        for (int qn = quantiles.length - 1; qn >= 0; qn--) {
            double quan = quantiles[qn];
            while (level >= quan && !timings.empty()) {
                timing = timings.pop();
                level -= 1.0 / rtimes.length;
                sqr_diffs += timing * Math.pow(timing - average, 2);
            }
            result.put("perc_" + String.valueOf(quan), timing);
        }
        result.put("stdev", (long) Math.sqrt(sqr_diffs / rtimes.length));
        return result;
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
