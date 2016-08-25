package kg.apc.jmeter.reporters;


import kg.apc.emulators.TestJMeterUtils;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;

public class LoadosophiaConsolidatorTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testFlow() throws Exception {
        LinkedList<JSON> response = new LinkedList<>();
        response.add(getFake());
        response.add(getFake());
        response.add(getFake());
        response.add(getFake());
        response.add(getFake());
        response.add(getFake());
        response.add(getFake());
        response.add(getFake());
        response.add(getFake());
        response.add(getFake());

        LoadosophiaConsolidator obj = new ConsolidatorEmul(response);

        LoadosophiaUploader source = new LoadosophiaUploader();
        source.setUseOnline(true);
        obj.add(source);
        obj.sampleOccurred(new SampleEvent(new SampleResult(System.currentTimeMillis() + 1000, 1), ""));
        obj.sampleOccurred(new SampleEvent(new SampleResult(System.currentTimeMillis() + 2000, 1), ""));
        obj.sampleOccurred(new SampleEvent(new SampleResult(System.currentTimeMillis() + 3000, 1), ""));
        obj.sampleOccurred(new SampleEvent(new SampleResult(System.currentTimeMillis() + 3000, 3), ""));
        obj.sampleOccurred(new SampleEvent(new SampleResult(System.currentTimeMillis() + 3000, 2), ""));
        obj.sampleOccurred(new SampleEvent(new SampleResult(System.currentTimeMillis() + 4000, 1), ""));
        obj.sampleOccurred(new SampleEvent(new SampleResult(System.currentTimeMillis() + 5000, 1), ""));
        obj.sampleOccurred(new SampleEvent(new SampleResult(System.currentTimeMillis() + 6000, 1), ""));
        obj.remove(source);
    }

    private JSONObject getFake() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("QueueID", 1);
        jsonObject.put("status", 4);
        jsonObject.put("TestID", 4);
        return jsonObject;
    }
}