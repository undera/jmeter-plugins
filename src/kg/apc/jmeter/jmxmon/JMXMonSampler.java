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
import javax.management.remote.JMXConnector;

/**
 *
 * @author Lars Holmberg (based on work by Marten Bohlin)
 */
public class JMXMonSampler {

    private String metricName;
    private String objectName;
    private String attribute;
    private final JMXConnector connection;
    private boolean sampleDeltaValue = true;
    private double oldValue = Double.NaN;

    public JMXMonSampler(JMXConnector conn, String name, boolean sampleDeltaValue, String objectName, String attribute) {
        this.metricName = name;
        this.connection = conn;
        this.objectName = objectName;
        this.attribute = attribute;
        this.sampleDeltaValue = sampleDeltaValue;
    }

    public void generateSamples(JMXMonSampleGenerator collector) {
        try {
            final MBeanServerConnection remote = connection.getMBeanServerConnection();

            // Construct the fully qualified name of the bean.
            ObjectName beanName = new ObjectName(objectName);

             final double val = Double.parseDouble(remote.getAttribute(beanName, attribute).toString());
             if (sampleDeltaValue) {
                 if (!Double.isNaN(oldValue)) {
                     collector.generateSample(val - oldValue, metricName);
                 }
                 oldValue = val;
             } else {
                 collector.generateSample(val, metricName);
             }
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(JMXMonSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(JMXMonSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ReflectionException ex) {
            java.util.logging.Logger.getLogger(JMXMonSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedObjectNameException ex) {
            java.util.logging.Logger.getLogger(JMXMonSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            java.util.logging.Logger.getLogger(JMXMonSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MBeanException ex) {
            java.util.logging.Logger.getLogger(JMXMonSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AttributeNotFoundException ex) {
            java.util.logging.Logger.getLogger(JMXMonSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstanceNotFoundException ex) {
            java.util.logging.Logger.getLogger(JMXMonSampler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
