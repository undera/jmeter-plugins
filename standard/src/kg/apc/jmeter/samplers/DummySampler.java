package kg.apc.jmeter.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author apc
 */
public class DummySampler
        extends AbstractSampler implements Interruptible {

    public static final String IS_SUCCESSFUL = "SUCCESFULL";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_MESSAGE = "RESPONSE_MESSAGE";
    public static final String RESPONSE_DATA = "RESPONSE_DATA";
    public static final String REQUEST_DATA = "REQUEST_DATA";
    public static final String RESPONSE_TIME = "RESPONSE_TIME";
    public static final String LATENCY = "LATENCY";
    public static final String IS_WAITING = "WAITING";

    @Override
    public SampleResult sample(Entry e) {
        SampleResult res;
        if (isSimulateWaiting()) {
            res = new SampleResult();
            res.sampleStart();
            try {
                Thread.sleep(getResponseTime());
            } catch (InterruptedException ex) {
            }
            res.sampleEnd();
        } else {
            res = new SampleResult(System.currentTimeMillis(), getResponseTime());
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
        res.setResponseData(getResponseData().getBytes());

        res.setLatency(getLatency());

        return res;
    }

    /**
     *
     * @param selected
     */
    public void setSuccessful(boolean selected) {
        setProperty(IS_SUCCESSFUL, selected);
    }

    public void setSimulateWaiting(boolean selected) {
        setProperty(IS_WAITING, selected);
    }

    /**
     *
     * @param text
     */
    public void setResponseCode(String text) {
        setProperty(RESPONSE_CODE, text);
    }

    /**
     *
     * @param text
     */
    public void setResponseMessage(String text) {
        setProperty(RESPONSE_MESSAGE, text);
    }

    /**
     *
     * @param text
     */
    public void setResponseData(String text) {
        setProperty(RESPONSE_DATA, text);
    }

    public void setRequestData(String text) {
        setProperty(REQUEST_DATA, text);
    }

    /**
     * @return the successfull
     */
    public boolean isSuccessfull() {
        return getPropertyAsBoolean(IS_SUCCESSFUL);
    }

    public boolean isSimulateWaiting() {
        return getPropertyAsBoolean(IS_WAITING);
    }

    /**
     * @return the responseCode
     */
    public String getResponseCode() {
        return getPropertyAsString(RESPONSE_CODE);
    }

    /**
     * @return the responseMessage
     */
    public String getResponseMessage() {
        return getPropertyAsString(RESPONSE_MESSAGE);
    }

    /**
     * @return the responseData
     */
    public String getResponseData() {
        return getPropertyAsString(RESPONSE_DATA);
    }

    public String getRequestData() {
        return getPropertyAsString(REQUEST_DATA);
    }

    /**
     *
     * @return
     */
    public int getResponseTime() {
        int time = 0;
        try {
            time = Integer.valueOf(getPropertyAsString(RESPONSE_TIME));
        } catch (NumberFormatException e) {
        }
        return time;
    }

    public int getLatency() {
        int time = 0;
        try {
            time = Integer.valueOf(getPropertyAsString(LATENCY));
        } catch (NumberFormatException e) {
        }
        return time;
    }

    public void setResponseTime(String time) {
        setProperty(RESPONSE_TIME, time);
    }

    public void setLatency(String time) {
        setProperty(LATENCY, time);
    }

    public boolean interrupt() {
        Thread.currentThread().interrupt();
        return true;
    }
}
