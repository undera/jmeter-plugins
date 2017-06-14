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
        res.put("samplesNotCounted", 0);                        // not used
        res.put("assertionsNotCounted", 0);                     // not used
        res.put("failedEmbeddedResources", "[]");               // not used
        res.put("failedEmbeddedResourcesSpilloverCount", 0);    // not used
        res.put("otherErrorsCount", 0);                         // not used
        res.put("percentileHistogram", "[]");                   // not used
        res.put("percentileHistogramLatency", "[]");            // not used
        res.put("percentileHistogramBytes", "[]");              // not used
        res.put("empty", "[]");                                 // not used
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
        setQuantiles(summaryValues, rtimes, average);


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


    public static void setQuantiles(JSONObject summaryValues, Long[] rtimes, long average) {
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
            summaryValues.put(getMetricLabel(quan), timing);
        }
        summaryValues.put("std", Math.sqrt(sqr_diffs / rtimes.length));
    }

    private static String getMetricLabel(double perc) {
        if (perc == 0.0) {
            return "min";
        } else if (perc == 100.0) {
            return "max";
        } else if (perc == 90.0) {
            return "tp90";
        } else if (perc == 95.0) {
            return "tp95";
        } else if (perc == 99.0) {
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
            JSONObject interval = new JSONObject();

            List<SampleResult> results = intervals.get(second);

            interval.put("ts", second);
            interval.put("n", results.size());
            interval.put("rc", generateResponseCodec(results));
            int fails = getFails(list);
            interval.put("ec", fails);
            interval.put("failed", fails);
            interval.put("na", getThreadsCount(list));

/**
 * "t": {
 "min": int(1000 * item[KPISet.PERCENTILES]["0.0"]) if "0.0" in item[KPISet.PERCENTILES] else 0,
 "max": int(1000 * item[KPISet.PERCENTILES]["100.0"]) if "100.0" in item[KPISet.PERCENTILES] else 0,
 "sum": 1000 * item[KPISet.AVG_RESP_TIME] * item[KPISet.SAMPLE_COUNT],
 "n": item[KPISet.SAMPLE_COUNT],
 "std": 1000 * item[KPISet.STDEV_RESP_TIME],
 "avg": 1000 * item[KPISet.AVG_RESP_TIME]
 },
 "lt": {
 "min": 0,
 "max": 0,
 "sum": 1000 * item[KPISet.AVG_LATENCY] * item[KPISet.SAMPLE_COUNT],
 "n": 1000 * item[KPISet.SAMPLE_COUNT],
 "std": 0,
 "avg": 1000 * item[KPISet.AVG_LATENCY]
 },
 "by": {
 "min": 0,
 "max": 0,
 "sum": item[KPISet.BYTE_COUNT],
 "n": item[KPISet.SAMPLE_COUNT],
 "std": 0,
 "avg": item[KPISet.BYTE_COUNT] / float(item[KPISet.SAMPLE_COUNT])
 },
 */
            result.add(interval);
        }

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
