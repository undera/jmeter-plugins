package kg.apc.jmeter.reporters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class LoadosophiaAggregator {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private Map<Long, List<SampleResult>> buffer = new HashMap<Long, List<SampleResult>>();
    private final long SEND_SECONDS = 5;

    public void addSample(SampleResult res) {
        if (log.isDebugEnabled()) {
            log.debug("Got sample to process: " + res);
        }

        Long time = res.getEndTime();

        if (!buffer.containsKey(time)) {
            Iterator<Long> it = buffer.keySet().iterator();
            if (it.hasNext()) {
                time = it.next();
            } else {
                buffer.put(time, new LinkedList<SampleResult>());
            }
        }
        buffer.get(time).add(res);
    }

    public boolean haveDataToSend() {
        return buffer.size() > SEND_SECONDS + 1;
    }

    public JSONArray getDataToSend() {
        JSONArray data = new JSONArray();
        Iterator<Long> it = buffer.keySet().iterator();
        int cnt = 0;
        while (cnt < SEND_SECONDS && it.hasNext()) {
            List<SampleResult> raw = buffer.get(it.next());
            data.add(getAggregateSecond(raw));
        }
        /*
         for sec in data_buffer:
         item = sec.overall
         json_item = {
         "ts": str(sec.time),
         "threads": item.active_threads,
         "rps": item.RPS,
         "planned_rps": item.planned_requests,
         "avg_rt": item.avg_response_time,
         "quantiles": item.quantiles,
         "rc": item.http_codes,
         "net": item.net_codes
         }
         */
        return data;
    }

    private JSONObject getAggregateSecond(List<SampleResult> raw) {
        JSONObject res = new JSONObject();
        res.put("ts", 10);
        return res;
    }
}
