package kg.apc.jmeter.jmxmon;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author Lars Holmberg (based on work by Marten Bohlin)
 */
public class JMXMonSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String metricName;
    private String objectName;
    private String attribute;
    private String key; // for use with complex types
    private String url; // use to open one connection for the same url
    private final MBeanServerConnection remote;
    private final JMXConnector jmxConnector;
    private boolean sampleDeltaValue = true;
    private double oldValue = Double.NaN;

    public JMXMonSampler(MBeanServerConnection remote, JMXConnector jmxConnector, String url, String name, String objectName, String attribute, String key, boolean sampleDeltaValue) {
        this.metricName = name;
        this.remote = remote;
        this.jmxConnector = jmxConnector;
        this.url = url;
        this.objectName = objectName;
        this.attribute = attribute;
        this.sampleDeltaValue = sampleDeltaValue;
        this.key = key;
    }

    public void generateSamples(JMXMonSampleGenerator collector) {
        try {

            // Construct the fully qualified name of the bean.
            ObjectName beanName = new ObjectName(objectName);

            Object o = remote.getAttribute(beanName, attribute);
            
            
            final double val;
            if (o instanceof CompositeDataSupport) {
                if (key == null || "".equals(key)) {
                    log.error("Got composite object from JMX, but no key specified ");
                    return;
                }                    
                CompositeDataSupport cds = (CompositeDataSupport)o;
                // log.info("CDS: " + cds.toString());
                val = Double.parseDouble(cds.get(key).toString());
            } else {
                if (key != null && !key.equals("")) {
                    log.error("key specified, but didnt get composite object from JMX. Will continue anyway.");
                }                    
                val = Double.parseDouble(o.toString());
            }
            if (sampleDeltaValue) {
                if (!Double.isNaN(oldValue)) {
                    collector.generateSample(val - oldValue, metricName);
                }
                oldValue = val;
            } else {
                collector.generateSample(val, metricName);
            }
        } catch (MalformedURLException ex) {          
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } catch (ReflectionException ex) {
            log.error(ex.getMessage());
        } catch (MalformedObjectNameException ex) {
            log.error(ex.getMessage());
        } catch (NullPointerException ex) {
            log.error(ex.getMessage());
        } catch (MBeanException ex) {
            log.error(ex.getMessage());
        } catch (AttributeNotFoundException ex) {
            log.error(ex.getMessage());
        } catch (InstanceNotFoundException ex) {
            log.error(ex.getMessage());
        }
    }

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isSampleDeltaValue() {
		return sampleDeltaValue;
	}

	public void setSampleDeltaValue(boolean sampleDeltaValue) {
		this.sampleDeltaValue = sampleDeltaValue;
	}

	public double getOldValue() {
		return oldValue;
	}

	public void setOldValue(double oldValue) {
		this.oldValue = oldValue;
	}

	public MBeanServerConnection getRemote() {
		return remote;
	}

	public JMXConnector getJmxConnector() {
		return jmxConnector;
	}
}
