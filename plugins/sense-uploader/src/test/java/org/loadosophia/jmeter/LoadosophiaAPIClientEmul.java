package org.loadosophia.jmeter;

import net.sf.json.JSON;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.util.LinkedList;


public class LoadosophiaAPIClientEmul extends LoadosophiaAPIClient {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private LinkedList<JSON> responses = new LinkedList<>();

    public LoadosophiaAPIClientEmul(StatusNotifierCallback aThis) {
        super(aThis, "http://localhost/", "TEST", COLOR_NONE, "TEST", "TEST");
    }

    public void addEmul(JSON response) {
        responses.add(response);
    }

    @Override
    protected JSON query(HttpRequestBase request, int expectedCode) throws IOException {
        log.info("Simulating request: " + request);
        if (responses.size()>0) {
            JSON resp = responses.remove();
            log.info("Response: " + resp);
            return resp;
        } else {
            throw new IOException("No responses to emulate");
        }
    }
}
