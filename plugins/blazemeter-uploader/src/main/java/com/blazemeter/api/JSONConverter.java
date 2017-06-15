package com.blazemeter.api;

import net.sf.json.JSONObject;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.samplers.SampleResult;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JSONConverter {

    public static final String SUMMARY_LABEL = "ALL";

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
        res.put("samplesNotCounted", 0);                        // not used
        res.put("assertionsNotCounted", 0);                     // not used
        res.put("failedEmbeddedResources", "[]");               // not used
        res.put("failedEmbeddedResourcesSpilloverCount", 0);    // not used
        res.put("otherErrorsCount", 0);                         // not used
        res.put("percentileHistogram", "[]");                   // not used
        res.put("percentileHistogramLatency", "[]");            // not used
        res.put("percentileHistogramBytes", "[]");              // not used
        res.put("empty", "[]");                                 // not used

        // TODO: use samples from test started
        res.put("summary", generateSummary(list));              // summary info
        res.put("intervals", calculateIntervals(list));         // list of intervals

        addErrors(res, list);

        return res;
    }

    private static void addErrors(JSONObject res, List<SampleResult> list) {
        JSONArray errors = new JSONArray();
        JSONArray assertions = new JSONArray();

        for (SampleResult sample : list) {
            if (!sample.isSuccessful()) {
                AssertionResult[] assertionResults = sample.getAssertionResults();
                if (assertionResults != null && assertionResults.length != 0) {
                    for (AssertionResult result : assertionResults) {
                        JSONObject obj = new JSONObject();
                        obj.put("failureMessage", result.getFailureMessage());
                        obj.put("name", "All Assertions");
                        obj.put("failures", sample.getErrorCount());
                        assertions.add(obj);
                    }
                } else {
                    JSONObject obj = new JSONObject();
                    obj.put("m", sample.getResponseMessage());
                    obj.put("rc", sample.getResponseCode());
                    obj.put("failures", sample.getErrorCount());
                    errors.add(obj);
                }
            }
        }

        res.put("errors", errors);             // list of errors
        res.put("assertions", assertions);    // list of assertions
    }


    private static JSONObject generateSummary(List<SampleResult> list) {
        final JSONObject summaryValues = new JSONObject();

        final Map<String, Object> mainMetrics = getMainMetrics(list);

        long first = (long) mainMetrics.get("first") / 1000;
        long last = (long) mainMetrics.get("last") / 1000;
        summaryValues.put("first", first / 100);
        summaryValues.put("last", last);
        summaryValues.put("duration", last - first);
        summaryValues.put("failed", mainMetrics.get("failed"));
        summaryValues.put("hits", list.size());

        long average = (long) mainMetrics.get("sumTime") / list.size();
        summaryValues.put("avg", average);
        summaryValues.accumulateAll(getQuantiles((Long[]) mainMetrics.get("rtimes"), average));


        summaryValues.put("latencyAvg", (long) mainMetrics.get("sumLatency")  / list.size());
        summaryValues.put("latencyMax", 0L);
        summaryValues.put("latencyMin", 0L);
        summaryValues.put("latencySTD", 0L);

        summaryValues.put("bytes", mainMetrics.get("bytesCount"));
        summaryValues.put("bytesMax", 0L);
        summaryValues.put("bytesMin", 0L);
        summaryValues.put("bytesAvg",  (long) mainMetrics.get("bytesCount") / list.size());
        summaryValues.put("bytesSTD", 0L);

        summaryValues.put("otherErrorsSpillcount", 0L);

        return summaryValues;
    }

    private static Map<String, Object> getMainMetrics(List<SampleResult> list) {
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

        final Map<String, Object> results = new HashMap<>();

        results.put("first", first);
        results.put("last", last);
        results.put("failed", failed);
        results.put("bytesCount", bytesCount);
        results.put("sumTime", sumTime);
        results.put("sumLatency", sumLatency);
        results.put("rtimes", rtimes);

        return results;
    }


    public static Map<String, Long> getQuantiles(Long[] rtimes, long average) {
        Map<String, Long> result = new HashMap<>();
        Arrays.sort(rtimes);

        double[] quantiles = {0.0, 0.90, 0.95, 0.99, 1.00};

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
            result.put(getMetricLabel(quan), timing);
        }
        result.put("std", (long) Math.sqrt(sqr_diffs / rtimes.length));
        return result;
    }

    private static String getMetricLabel(double perc) {
        if (perc == 0.0) {
            return "min";
        } else if (perc == 1.00) {
            return "max";
        } else if (perc == 0.90) {
            return "tp90";
        } else if (perc == 0.95) {
            return "tp95";
        } else if (perc == 0.99) {
            return "tp99";
        } else {
            return "";
        }
    }

    private static JSONArray calculateIntervals(List<SampleResult> list) {
        Map<Long, List<SampleResult>> intervals = new HashMap<>();

        for (SampleResult sample : list) {
            long time = sample.getEndTime() / 1000;
            if (!intervals.containsKey(time)) {
                intervals.put(time, new LinkedList<SampleResult>());
            }
            intervals.get(time).add(sample);
        }

        JSONArray result = new JSONArray();
        for (Long second : intervals.keySet()) {
            JSONObject intervalResult = new JSONObject();

            List<SampleResult> intervalValues = intervals.get(second);

            intervalResult.put("ts", second);
            intervalResult.put("n", intervalValues.size());
            intervalResult.put("rc", generateResponseCodec(intervalValues));
            int fails = getFails(intervalValues);
            intervalResult.put("ec", fails);
            intervalResult.put("failed", fails);
            intervalResult.put("na", getThreadsCount(intervalValues));

            final Map<String, Object> mainMetrics = getMainMetrics(intervalValues);
            int intervalSize = intervalValues.size();
            intervalResult.put("t", generateTimestamp(mainMetrics, intervalSize));
            intervalResult.put("lt", generateLatencyTime(mainMetrics, intervalSize));
            intervalResult.put("by", generateBytes(mainMetrics, intervalSize));

            result.add(intervalResult);
        }

        return result;
    }

    private static JSONObject generateTimestamp(Map<String, Object> mainMetrics, int sampleCount) {
        JSONObject result = new JSONObject();

        result.put("sum", mainMetrics.get("sumTime"));
        result.put("n", sampleCount);

        long average = (long) mainMetrics.get("sumTime") / sampleCount;
        Map<String, Long> perc = getQuantiles((Long[]) mainMetrics.get("rtimes"), average);
        result.put("avg", average);
        result.put("std", perc.get("std"));
        result.put("min", perc.get("min"));
        result.put("max", perc.get("max"));

        return result;
    }

    private static JSONObject generateLatencyTime(Map<String, Object> mainMetrics, int sampleCount) {
        JSONObject result = new JSONObject();

        result.put("min", 0);
        result.put("max", 0);
        result.put("std", 0);
        result.put("sum", mainMetrics.get("sumLatency"));
        result.put("avg", (long) mainMetrics.get("sumLatency") / sampleCount);
        result.put("n", sampleCount);

        return result;
    }

    private static JSONObject generateBytes(Map<String, Object> mainMetrics, int sampleCount) {
        JSONObject result = new JSONObject();

        result.put("min", 0);
        result.put("max", 0);
        result.put("std", 0);
        result.put("sum", mainMetrics.get("bytesCount"));
        result.put("avg", (long) mainMetrics.get("bytesCount") / sampleCount);
        result.put("n", sampleCount);

        return result;
    }

    private static long getThreadsCount(List<SampleResult> list) {
        Map<String, Integer> threads = new HashMap<>();
        for (SampleResult res : list) {
            if (!threads.containsKey(res.getThreadName())) {
                threads.put(res.getThreadName(), 0);
            }
            threads.put(res.getThreadName(), res.getAllThreads());
        }
        long tsum = 0;
        for (Integer tcount : threads.values()) {
            tsum += tcount;
        }
        return tsum;
    }

    private static int getFails(List<SampleResult> list) {
        int res = 0;
        for (SampleResult result : list) {
            if (!result.isSuccessful()) {
                res++;
            }
        }
        return res;
    }

    private static JSONArray generateResponseCodec(List<SampleResult> list) {
        JSONArray rc = new JSONArray();

        Map<String, List<SampleResult>> samplesSortedByResponseCode = new HashMap<>();
        for (SampleResult result : list) {
            String responseCode = result.getResponseCode();
            if (!samplesSortedByResponseCode.containsKey(responseCode)) {
                samplesSortedByResponseCode.put(responseCode, new LinkedList<SampleResult>());
            }
            samplesSortedByResponseCode.get(responseCode).add(result);
        }

        for (String responseCode : samplesSortedByResponseCode.keySet()) {
            int failes = 0;
            List<SampleResult> results = samplesSortedByResponseCode.get(responseCode);
            for (SampleResult result : results) {
                if (!result.isSuccessful()) {
                    failes++;
                }
            }
            JSONObject obj = new JSONObject();
            obj.put("f", failes);
            obj.put("n", results.size());
            obj.put("rc", responseCode);
            rc.add(obj);
        }

        return rc;
    }

}
