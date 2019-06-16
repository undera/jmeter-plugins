package kg.apc.jmeter.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class DummySampler
        extends AbstractSampler implements Interruptible {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String IS_SUCCESSFUL = "SUCCESFULL";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_MESSAGE = "RESPONSE_MESSAGE";
    public static final String RESPONSE_DATA = "RESPONSE_DATA";
    public static final String REQUEST_DATA = "REQUEST_DATA";
    public static final String RESPONSE_TIME = "RESPONSE_TIME";
    public static final String LATENCY = "LATENCY";
    public static final String CONNECT = "CONNECT";
    public static final String IS_WAITING = "WAITING";
    public static final String URL = "URL";
    public static final String RESULT_CLASS = "RESULT_CLASS";

    @Override
    public SampleResult sample(Entry e) {
        SampleResult res;
        try {
            res = createSample();
        } catch (ReflectiveOperationException ex) {
            res = new SampleResult();
            log.warn("Failed to create sample of desired type", ex);
        }

        res.setSampleLabel(getName());

        // source data
        res.setSamplerData(getRequestData());

        // response code
        res.setResponseCode(getResponseCode());
        res.setResponseMessage(getResponseMessage());
        res.setSuccessful(isSuccessfull());

        // responde data
        res.setDataType(SampleResult.TEXT);
        try {
            res.setResponseData(getResponseData().getBytes(res.getDataEncodingWithDefault()));
        } catch (UnsupportedEncodingException exc) {
            log.warn("Failed to get response data", exc);
        }

        String url = getURL();
        if (!url.isEmpty()) {
            try {
                res.setURL(new URL(url));
            } catch (MalformedURLException ex) {
                log.debug("URL is wrong: " + url, ex);
            }
        }

        res.setLatency(getLatency());
        res.setConnectTime(getConnectTime());

        return res;
    }

    private SampleResult createSample() throws ReflectiveOperationException {
        Class<SampleResult> cls;
        //noinspection unchecked
        cls = (Class<SampleResult>) Class.forName(getResultClass());

        SampleResult res;
        res = cls.newInstance();
        if (isSimulateWaiting()) {
            res.sampleStart();
            try {
                Thread.sleep(getResponseTime());
            } catch (InterruptedException ignored) {
            }
            res.sampleEnd();
        } else {
            res.setStampAndTime(System.currentTimeMillis(), (long) getResponseTime());
        }

        return res;
    }

    public void setSuccessful(boolean selected) {
        setProperty(IS_SUCCESSFUL, selected);
    }

    public void setSimulateWaiting(boolean selected) {
        setProperty(IS_WAITING, selected);
    }

    public void setResponseCode(String text) {
        setProperty(RESPONSE_CODE, text);
    }

    public void setResponseMessage(String text) {
        setProperty(RESPONSE_MESSAGE, text);
    }

    public void setResponseData(String text) {
        setProperty(RESPONSE_DATA, text);
    }

    public void setRequestData(String text) {
        setProperty(REQUEST_DATA, text);
    }

    public void setURL(String text) {
        setProperty(URL, text);
    }

    public void setResultClass(String text) {
        setProperty(RESULT_CLASS, text);
    }

    public boolean isSuccessfull() {
        return getPropertyAsBoolean(IS_SUCCESSFUL);
    }

    public boolean isSimulateWaiting() {
        return getPropertyAsBoolean(IS_WAITING);
    }

    public String getResponseCode() {
        return getPropertyAsString(RESPONSE_CODE);
    }

    public String getResponseMessage() {
        return getPropertyAsString(RESPONSE_MESSAGE);
    }

    public String getResponseData() {
        return getPropertyAsString(RESPONSE_DATA);
    }

    public String getRequestData() {
        return getPropertyAsString(REQUEST_DATA);
    }

    public String getResultClass() {
        return getPropertyAsString(RESULT_CLASS, SampleResult.class.getCanonicalName());
    }

    public String getURL() {
        return getPropertyAsString(URL);
    }

    public int getResponseTime() {
        int time = 0;
        try {
            time = Integer.valueOf(getPropertyAsString(RESPONSE_TIME));
        } catch (NumberFormatException ignored) {
        }
        return time;
    }

    public int getLatency() {
        int time = 0;
        try {
            time = Integer.valueOf(getPropertyAsString(LATENCY));
        } catch (NumberFormatException ignored) {
        }
        return time;
    }

    public void setResponseTime(String time) {
        setProperty(RESPONSE_TIME, time);
    }

    public void setLatency(String time) {
        setProperty(LATENCY, time);
    }

    public void setConnectTime(String time) {
        setProperty(CONNECT, time);
    }

    public boolean interrupt() {
        Thread.currentThread().interrupt();
        return true;
    }

    public int getConnectTime() {
        int time = 0;
        try {
            time = Integer.valueOf(getPropertyAsString(CONNECT));
        } catch (NumberFormatException ignored) {
        }
        return time;
    }
}
