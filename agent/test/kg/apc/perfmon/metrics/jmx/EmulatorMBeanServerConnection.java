package kg.apc.perfmon.metrics.jmx;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.management.*;

/**
 *
 * @author undera
 */
public class EmulatorMBeanServerConnection implements MBeanServerConnection {

    public EmulatorMBeanServerConnection() {
    }

    public ObjectInstance createMBean(String string, ObjectName on) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ObjectInstance createMBean(String string, ObjectName on, ObjectName on1) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ObjectInstance createMBean(String string, ObjectName on, Object[] os, String[] strings) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ObjectInstance createMBean(String string, ObjectName on, ObjectName on1, Object[] os, String[] strings) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unregisterMBean(ObjectName on) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ObjectInstance getObjectInstance(ObjectName on) throws InstanceNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set queryMBeans(ObjectName on, QueryExp qe) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set queryNames(ObjectName on, QueryExp qe) throws IOException {
        return new HashSet();
    }

    public boolean isRegistered(ObjectName on) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer getMBeanCount() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getAttribute(ObjectName on, String string) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AttributeList getAttributes(ObjectName on, String[] strings) throws InstanceNotFoundException, ReflectionException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAttribute(ObjectName on, Attribute atrbt) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AttributeList setAttributes(ObjectName on, AttributeList al) throws InstanceNotFoundException, ReflectionException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object invoke(ObjectName on, String string, Object[] os, String[] strings) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDefaultDomain() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getDomains() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addNotificationListener(ObjectName on, NotificationListener nl, NotificationFilter nf, Object o) throws InstanceNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addNotificationListener(ObjectName on, ObjectName on1, NotificationFilter nf, Object o) throws InstanceNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeNotificationListener(ObjectName on, ObjectName on1) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeNotificationListener(ObjectName on, ObjectName on1, NotificationFilter nf, Object o) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeNotificationListener(ObjectName on, NotificationListener nl) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeNotificationListener(ObjectName on, NotificationListener nl, NotificationFilter nf, Object o) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MBeanInfo getMBeanInfo(ObjectName on) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isInstanceOf(ObjectName on, String string) throws InstanceNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
