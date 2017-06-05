package kg.apc.jmeter.reporters;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.LoadosophiaAPIClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 *
 */
public class LoadosophiaClient extends AbstractBackendListenerClient {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private String address = JMeterUtils.getPropDefault("sense.address", "https://sense.blazemeter.com/");
    ;
    private LoadosophiaAPIClient apiClient;

    public LoadosophiaClient() {
        log.error("LoadosophiaClient create");
        log.error(Thread.currentThread().getName());
    }


    @Override
    public void setupTest(BackendListenerContext context) throws Exception {
        super.setupTest(context);
        apiClient = new LoadosophiaAPIClient(
                null, // TODO :!!!!!!!
                address,
                context.getParameter(LoadosophiaUploader.UPLOAD_TOKEN),
                context.getParameter(LoadosophiaUploader.PROJECT),
                context.getParameter(LoadosophiaUploader.COLOR),
                context.getParameter(LoadosophiaUploader.TITLE)
        );
        log.error("API: " +address+ " "+
                context.getParameter(LoadosophiaUploader.UPLOAD_TOKEN)+" "+
                context.getParameter(LoadosophiaUploader.PROJECT)+" "+
                context.getParameter(LoadosophiaUploader.COLOR)+" "+
                context.getParameter(LoadosophiaUploader.TITLE));
    }

    @Override
    public void handleSampleResults(List<SampleResult> list, BackendListenerContext backendListenerContext) {
//        log.error("handleSampleResults null list");
//        log.error(Thread.currentThread().getName());
        if (list != null) {
            log.error("send samples " + list.size());
//            try {
                JSONArray array = getDataToSend(list);
                log.error(array.toString());
                throw new RuntimeException("");
//                apiClient.sendOnlineData(array);

//            } catch (IOException ex) {
//                log.warn("Failed to send active test data", ex);
//            }
//            try {
//                Thread.sleep(500); // TODO:! what about sleep???
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }



    public JSONArray getDataToSend(List<SampleResult> list) {
        JSONArray data = new JSONArray();
        data.add(getAggregateSecond(list));
        return data;
    }

    private JSONObject getAggregateSecond(List<SampleResult> raw) {
        /*
         "rc": item.http_codes,
         "net": item.net_codes
         */
        JSONObject result = new JSONObject();
//        this.lastAggregatedTime = sec;
//        Date ts = new Date(sec * 1000);
//        log.debug("Aggregating " + sec);
//        result.put("ts", format.format(ts));

//        Map<String, Integer> threads = new HashMap<>();
        int avg_rt = 0;
        Long[] rtimes = new Long[raw.size()];
        String[] rcodes = new String[raw.size()];
        int cnt = 0;
        int failedCount = 0;
        long tsum = 0;
        for (SampleResult res : raw) {
            avg_rt += res.getTime();
            rtimes[cnt] = res.getTime();
            rcodes[cnt] = res.getResponseCode();
            if (!res.isSuccessful()) {
                failedCount++;
            }
            cnt++;

            if (tsum < res.getAllThreads()) {
                tsum = res.getAllThreads();
            }
        }

        result.put("rps", cnt);
        result.put("threads", raw);
        result.put("avg_rt", avg_rt / cnt);
        result.put("quantiles", getQuantilesJSON(rtimes));
        result.put("net", getNetJSON(failedCount, cnt - failedCount));
        result.put("rc", getRCJSON(rcodes));
        result.put("planned_rps", 0); // JMeter has no such feature like Yandex.Tank
        return result;
    }

    public static JSONObject getQuantilesJSON(Long[] rtimes) {
        JSONObject result = new JSONObject();
        Arrays.sort(rtimes);

        double[] quantiles = {0.25, 0.50, 0.75, 0.80, 0.90, 0.95, 0.98, 0.99, 1.00};

        Stack<Long> timings = new Stack<>();
        timings.addAll(Arrays.asList(rtimes));
        double level = 1.0;
        Object timing = 0;
        for (int qn = quantiles.length - 1; qn >= 0; qn--) {
            double quan = quantiles[qn];
            while (level >= quan && !timings.empty()) {
                timing = timings.pop();
                level -= 1.0 / rtimes.length;
            }
            result.element(String.valueOf(quan * 100), timing);
        }

        return result;
    }

    private JSONObject getNetJSON(int failedCount, int succCount) {
        JSONObject result = new JSONObject();
        result.put("0", succCount);
        result.put("1", failedCount);
        return result;
    }

    private JSONObject getRCJSON(String[] rcodes) {
        JSONObject result = new JSONObject();
        for (String rcode : rcodes) {
            int oldval = 0;
            if (result.containsKey(rcode)) {
                oldval = (Integer) result.get(rcode);
            }
            result.put(rcode, oldval + 1);

        }
        return result;
    }
}
