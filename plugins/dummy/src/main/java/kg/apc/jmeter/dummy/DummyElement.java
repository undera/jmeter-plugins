package kg.apc.jmeter.dummy;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class DummyElement implements Serializable {
    private static final long serialVersionUID = 246L;
    private static final Logger log = LoggerFactory.getLogger(DummyElement.class);

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
    private AbstractTestElement model;

    public DummyElement(AbstractTestElement model) {
        this.model = model;
    }

    public SampleResult sample() {
        SampleResult res;
        try {
            res = createSample();
        } catch (ReflectiveOperationException ex) {
            res = new SampleResult();
            log.warn("Failed to create sample of desired type", ex);
        }

        res.setDataEncoding(JMeterUtils.getProperty("sampleresult.default.encoding"));

        res.setSampleLabel(model.getName());

        // source data
        res.setSamplerData(getRequestData());

        // response code
        res.setResponseCode(getResponseCode());
        res.setResponseMessage(getResponseMessage());
        res.setSuccessful(isSuccessfull());

        // responde data
        res.setDataType(SampleResult.TEXT);
        try {
            byte[] bytes = getResponseData().getBytes(res.getDataEncodingWithDefault());
            res.setResponseData(bytes);
        } catch (UnsupportedEncodingException exc) {
            log.warn("Failed to get response data", exc);
            res.setResponseMessage(exc.toString());
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

    public int getConnectTime() {
        int time = 0;
        try {
            time = Integer.valueOf(getPropertyAsString(CONNECT));
        } catch (NumberFormatException ignored) {
        }
        return time;
    }

    // adapter methods
    private void setProperty(String key, String val) {
        model.setProperty(key, val);
    }

    private void setProperty(String key, boolean val) {
        model.setProperty(key, val);
    }

    private String getPropertyAsString(String key) {
        return model.getPropertyAsString(key);
    }

    private String getPropertyAsString(String key, String vdefault) {
        return model.getPropertyAsString(key, vdefault);
    }

    private boolean getPropertyAsBoolean(String key) {
        return model.getPropertyAsBoolean(key);
    }
}
