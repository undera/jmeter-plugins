package kg.apc.jmeter.reporters;

import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.perfmon.PerfMonCollector;
import kg.apc.jmeter.vizualizers.CorrectedResultCollector;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.backend.BackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.LoadosophiaUploadResults;
import org.loadosophia.jmeter.StatusNotifierCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

public class LoadosophiaClient implements BackendListenerClient {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    protected String address = JMeterUtils.getPropDefault("sense.address", "https://sense.blazemeter.com/");
    protected String fileName;
    protected boolean isOnlineInitiated = false;
    protected String token;
    protected String project;
    protected String color;
    protected String title;
    protected LoadosophiaAPIClient apiClient;

    // this field set from LoadosophiaUploader after BackendListener created instance of this class
    protected CorrectedResultCollector resultCollector;
    protected StatusNotifierCallback informer;



    // BackendListener called this method when test was started
    @Override
    public void setupTest(BackendListenerContext context) throws Exception {
        init(context);
    }

    private void init(BackendListenerContext context) {
        token = context.getParameter(LoadosophiaUploader.UPLOAD_TOKEN);
        project = context.getParameter(LoadosophiaUploader.PROJECT);
        color = context.getParameter(LoadosophiaUploader.COLOR);
        title = context.getParameter(LoadosophiaUploader.TITLE);
        fileName = context.getParameter(LoadosophiaUploader.FILE_NAME);
        isOnlineInitiated = context.containsParameter(LoadosophiaUploader.USE_ONLINE);
    }

    public void initiateOnline() {
        apiClient = new LoadosophiaAPIClient(informer, address, token, project,color, title);
        if (isOnlineInitiated) {
            try {
                log.info("Starting BM.Sense online test");
                String url = apiClient.startOnline();
                informer.notifyAbout("<p>Started active test: <a href='" + url + "'>" + url + "</a></p>");
            } catch (IOException ex) {
                informer.notifyAbout("Failed to start active test");
                log.warn("Failed to initiate active test", ex);
                this.isOnlineInitiated = false;
            }
        }
    }

    @Override
    public void handleSampleResults(List<SampleResult> list, BackendListenerContext backendListenerContext) {
        if (list != null && isOnlineInitiated) {
            try {
                JSONArray array = getDataToSend(list);
                log.warn("send samples " + list.size());
                log.warn(array.toString());
                apiClient.sendOnlineData(array);
            } catch (IOException ex) {
                log.warn("Failed to send active test data", ex);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.warn("Backend listener client thread was interrupted");
            }
        }
    }

    // BackendListener called this method when test was ended
    @Override
    public void teardownTest(BackendListenerContext context) throws Exception {
        stop(context);
    }

    protected void stop(BackendListenerContext context) {
        String redirectLink = "";
            try {
                flush();

                if (fileName == null) {
                    throw new IOException("File for upload was not set, search for errors above in log");
                }

                LinkedList<String> monFiles;
                if (hasStandardSet()) {
                    monFiles = PerfMonCollector.getFiles();
                } else {
                    monFiles = new LinkedList<>();
                }
                log.warn("TRY TO SEND monFiles:  " + monFiles.size());
                log.warn("TRY TO DUMP FILE:   " + fileToString(fileName));
                LoadosophiaUploadResults uploadResult = this.apiClient.sendFiles(new File(fileName), monFiles);
                redirectLink = uploadResult.getRedirectLink();
                informer.notifyAbout("<p>Uploaded successfully, go to results: <a href='" + redirectLink + "'>" + redirectLink + "</a></p>");
            } catch (Throwable ex) {
                informer.notifyAbout("Failed to upload results to BM.Sense, see log for detais: " + ex.getMessage());
                log.error("Failed to upload results to BM.Sense", ex);
            }

        if (Boolean.parseBoolean(context.getParameter(LoadosophiaUploader.USE_ONLINE))) {
            log.info("Ending BM.Sense online test");
            try {
                apiClient.endOnline(redirectLink);
            } catch (IOException ex) {
                log.warn("Failed to finalize active test", ex);
            }
        }

        if (hasStandardSet()) {
            PerfMonCollector.clearFiles();
        }
    }

    // TODO: remove it
    public static String fileToString(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
                String line = reader.readLine();
                if (line != null) {
                    do {
                        sb.append(line);
                        line = reader.readLine();
                    } while (line != null);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public JSONArray getDataToSend(List<SampleResult> list) {
        JSONArray data = new JSONArray();

        SortedMap<Long, List<SampleResult>> sortedResults = sortResults(list);
        for (Long sec : sortedResults.keySet()) {
            data.add(getAggregateSecond(sec, sortedResults.get(sec)));
        }
        return data;
    }

    private SortedMap<Long, List<SampleResult>> sortResults(List<SampleResult> list) {
        SortedMap<Long, List<SampleResult>> sortedResults = new TreeMap<>();

        for (SampleResult result : list) {
            long time = result.getTime();
            if (!sortedResults.containsKey(time)) {
                sortedResults.put(time, new LinkedList<SampleResult>());
            }
            sortedResults.get(time).add(result);
        }

        return sortedResults;
    }


    private JSONObject getAggregateSecond(Long sec, List<SampleResult> raw) {
        /*
         "rc": item.http_codes,
         "net": item.net_codes
         */
        JSONObject result = new JSONObject();
        Date ts = new Date(sec * 1000);
        log.debug("Aggregating " + sec);
        result.put("ts", format.format(ts));

        Map<String, Integer> threads = new HashMap<>();
        int avg_rt = 0;
        Long[] rtimes = new Long[raw.size()];
        String[] rcodes = new String[raw.size()];
        int cnt = 0;
        int failedCount = 0;
        for (SampleResult res : raw) {
            if (!threads.containsKey(res.getThreadName())) {
                threads.put(res.getThreadName(), 0);
            }
            threads.put(res.getThreadName(), res.getAllThreads());

            avg_rt += res.getTime();
            rtimes[cnt] = res.getTime();
            rcodes[cnt] = res.getResponseCode();
            if (!res.isSuccessful()) {
                failedCount++;
            }
            cnt++;
        }

        long tsum = 0;
        for (Integer tcount : threads.values()) {
            tsum += tcount;
        }
        result.put("rps", cnt);
        result.put("threads", tsum);
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

    @Override
    public Arguments getDefaultParameters() {
        return null;
    }

    @Override
    public SampleResult createSampleResult(BackendListenerContext backendListenerContext, SampleResult sampleResult) {
        return sampleResult;
    }

    private void flush() {
        // FIXME: trying to handle safe upgrade, needs to be removed in the future
        // @see https://issues.apache.org/bugzilla/show_bug.cgi?id=56807
        try {
            Class<ResultCollector> c = ResultCollector.class;
            Method m = c.getDeclaredMethod("flushFile");
            m.invoke(resultCollector);
            log.info("Successfully flushed results file");
        } catch (NoSuchMethodException ex) {
            log.warn("Cannot flush results file since you are using old version of JMeter, consider upgrading to latest. Currently the results may be incomplete.");
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("Failed to flush file", e);
        }
    }

    private boolean hasStandardSet() {
        boolean hasStandardSet = true;
        try {
            Class.forName("kg.apc.jmeter.perfmon.PerfMonCollector");
        } catch (ClassNotFoundException e) {
            hasStandardSet = false;
        }
        return hasStandardSet;
    }

    public LoadosophiaAPIClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(LoadosophiaAPIClient apiClient) {
        this.apiClient = apiClient;
    }

    public CorrectedResultCollector getResultCollector() {
        return resultCollector;
    }

    public void setResultCollector(CorrectedResultCollector resultCollector) {
        this.resultCollector = resultCollector;
    }

    public StatusNotifierCallback getInformer() {
        return informer;
    }

    public void setInformer(StatusNotifierCallback informer) {
        this.informer = informer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isOnlineInitiated() {
        return isOnlineInitiated;
    }

    public void setOnlineInitiated(boolean onlineInitiated) {
        isOnlineInitiated = onlineInitiated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
