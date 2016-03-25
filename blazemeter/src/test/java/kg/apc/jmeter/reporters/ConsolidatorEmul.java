package kg.apc.jmeter.reporters;


import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.StatusNotifierCallback;

import java.util.LinkedList;

public class ConsolidatorEmul extends LoadosophiaConsolidator implements StatusNotifierCallback {
    private final LinkedList<String[]> response;

    public ConsolidatorEmul(LinkedList<String[]> aresponse) {
        super();
        response = aresponse;
    }

    @Override
    protected LoadosophiaAPIClient getAPIClient(LoadosophiaUploader source) {
        return new FakeAPIClient(this, response);
    }

    @Override
    public void notifyAbout(String info) {

    }
}

