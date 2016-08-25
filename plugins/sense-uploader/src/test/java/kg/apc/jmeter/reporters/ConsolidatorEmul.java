package kg.apc.jmeter.reporters;


import net.sf.json.JSON;
import org.loadosophia.jmeter.LoadosophiaAPIClient;
import org.loadosophia.jmeter.LoadosophiaAPIClientEmul;
import org.loadosophia.jmeter.StatusNotifierCallback;

import java.util.LinkedList;

public class ConsolidatorEmul extends LoadosophiaConsolidator implements StatusNotifierCallback {
    private final LinkedList<JSON> response;

    public ConsolidatorEmul(LinkedList<JSON> aresponse) {
        super();
        response = aresponse;
    }

    @Override
    protected LoadosophiaAPIClient getAPIClient(LoadosophiaUploader source) {
        LoadosophiaAPIClientEmul loadosophiaAPIClientEmul = new LoadosophiaAPIClientEmul(this);
        for (JSON resp : response) {
            loadosophiaAPIClientEmul.addEmul(resp);
        }
        return loadosophiaAPIClientEmul;
    }

    @Override
    public void notifyAbout(String info) {

    }
}

