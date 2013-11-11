package kg.apc.jmeter.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.util.ArrayList;
import java.util.LinkedList;

public class DistributedTestControl extends ConfigTestElement {

    public static final String DATA_PROP = "SERVERS";
    public static final String PROP_HOSTS = "remote_hosts";
    public static Logger log = LoggingManager.getLoggerForClass();

    public CollectionProperty getData() {
        CollectionProperty data = (CollectionProperty) getProperty(DATA_PROP);
        LinkedList<String> arr=new LinkedList<String>();

        for (int n = 0; n < data.size(); n++) {
            arr.add(data.get(n).getStringValue());
        }

        String val = StringUtils.join(arr, ",");
        log.debug("Setting hosts 1: " + val);
        JMeterUtils.setProperty(PROP_HOSTS, val);
        return data;
    }

    public void setData(ArrayList<String> data) {
        setProperty(new CollectionProperty(DistributedTestControl.DATA_PROP, data));
        String val = StringUtils.join(data, ",");
        log.debug("Setting hosts 2: " + val);
        JMeterUtils.setProperty(PROP_HOSTS, val);
    }
}
