package kg.apc.jmeter.jmxmon;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.util.Hashtable;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;

import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class JMXMonSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String metricName;
    private String objectName;
    private String attribute;
    private String key; // for use with complex types
    private JMeterProperty url; // use to open one connection for the same url
    
    private final JMXMonConnectionPool pool;
    private final Hashtable connectionAttributes;
    private MBeanServerConnection remote;
    
    private boolean sampleDeltaValue = true;
    private double oldValue = Double.NaN;
    private boolean canRetry = true;
    private boolean hasFailed = false;
	private JMXMonCollector collector;
	
    
    public JMXMonSampler(MBeanServerConnection remote, JMXConnector jmxConnector, JMeterProperty url, String name, String objectName, String attribute, String key, boolean sampleDeltaValue) {
    	this.pool = null;
    	this.connectionAttributes = null;
    	this.remote = remote;
        this.metricName = name;
        this.url = url;
        this.objectName = objectName;
        this.attribute = attribute;
        this.sampleDeltaValue = sampleDeltaValue;
        this.key = key;
        this.canRetry = false;
    }
    
    /**
     * Constructor
     * @param jmxMonCollector 
     * @param pool the connection pool
     * @param attributes connection attributes
     * @param url jmx url
     * @param name sampler name
     * @param objectName jmx object name
     * @param attribute jmx object attribute name
     * @param key jmx object attribute key name if needed
     * @param sampleDeltaValue if true the sample value is the delta between previous value
     * @param canRetry if true the sampler will try to connect to the jmx server until connection/reconnection
     */
    public JMXMonSampler(JMXMonCollector jmxMonCollector, JMXMonConnectionPool pool, Hashtable attributes, JMeterProperty url, String name, String objectName, String attribute, String key, boolean sampleDeltaValue, boolean canRetry) {
    	this.collector = jmxMonCollector;
        this.pool = pool;
        this.connectionAttributes = attributes;
    	this.metricName = name;
        this.url = url;
        this.objectName = objectName;
        this.attribute = attribute;
        this.sampleDeltaValue = sampleDeltaValue;
        this.key = key;
        this.canRetry = canRetry;
    }

	public void generateSamples(JMXMonSampleGenerator collector) {
        try {

        	if (hasFailed && !canRetry){
        		return;
        	}
        	
            // Construct the fully qualified name of the bean.
            ObjectName beanName = new ObjectName(objectName);

            MBeanServerConnection activeRemote = null;
            if (remote == null) {
            	activeRemote = pool.getConnection(url.getStringValue(), connectionAttributes);
            	
            	if (activeRemote == null){
            		hasFailed = true;
            	}
            }
            else
            {
            	activeRemote = remote;
            }
            
            final double val;
            if (activeRemote != null) {
            	Object o = activeRemote.getAttribute(beanName, attribute);
                
                
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
            } else {
            	val = 0;
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
        } catch (ConnectException ex) {          
            log.warn("Connection lost", ex);
            pool.notifyConnectionDirty(url.getStringValue());
            
            if (sampleDeltaValue) {
                if (!Double.isNaN(oldValue)) {
                    collector.generateSample(0 - oldValue, metricName);
                }
                oldValue = 0;
            } else {
                collector.generateSample(0, metricName);
            }
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

	public JMeterProperty getUrl() {
		return url;
	}

	public void setUrl(JMeterProperty url) {
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

}
