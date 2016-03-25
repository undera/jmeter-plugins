package kg.apc.jmeter.reporters;

import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.StatusNotifierCallback;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;


class FakeAPIClient extends LoadosophiaAPIClient {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private LinkedList<String[]> response;

    private FakeAPIClient(StatusNotifierCallback aThis, LinkedList<String[]> aresponse) {
        super(aThis, "TEST", "TEST", COLOR_NONE, "TEST", "TEST");
        response = aresponse;
    }

    @Override
    protected String[] getUploadStatus(int queueID) throws IOException {
        String[] resp = response.pop();
        log.info("Simulating response: " + Arrays.toString(resp));
        return resp;
    }

    @Override
    protected String[] multipartPost(LinkedList<Part> parts, String URL, int expectedSC) throws IOException {
        return getUploadStatus(0);
    }
}
