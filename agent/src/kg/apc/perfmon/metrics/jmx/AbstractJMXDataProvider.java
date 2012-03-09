package kg.apc.perfmon.metrics.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import kg.apc.perfmon.metrics.MetricParams;

/**
 *
 * @author undera
 */
abstract public class AbstractJMXDataProvider {

    public static final int TYPE_GC = 0;
    public static final int TYPE_CLASSES = 1;
    protected final Set beans;

    public AbstractJMXDataProvider(MBeanServerConnection mBeanServerConn) throws Exception {
        beans = getMXBeans(mBeanServerConn);
    }

    public static AbstractJMXDataProvider getProvider(MBeanServerConnection mBeanServerConn, String[] params) throws Exception {
        for (int i = 0; i < params.length; i++) {
            if (params[i].startsWith("type=")) {
                String sType = MetricParams.getParamValue(params[i]);
                if (sType.startsWith("gc-")) {
                    return new GCDataProvider(mBeanServerConn);
                } else if (sType.startsWith("class-")) {
                    return new ClassesDataProvider(mBeanServerConn);
                }
            }
        }
        throw new IllegalArgumentException("Can't define JMX type");
    }

    private Set getMXBeans(MBeanServerConnection mBeanServerConn) throws MalformedObjectNameException, NullPointerException, IOException {
        ObjectName gcAllObjectName = new ObjectName(getMXBeanType() + ",*");
        Set gcMXBeanObjectNames = mBeanServerConn.queryNames(gcAllObjectName, null);
        Iterator it = gcMXBeanObjectNames.iterator();
        Set res = new HashSet();
        while (it.hasNext()) {
            ObjectName on = (ObjectName) it.next();
            Object mxBean = ManagementFactory.newPlatformMXBeanProxy(mBeanServerConn, on.getCanonicalName(), getMXBeanClass());
            res.add(mxBean);
        }
        return res;
    }

    abstract protected String getMXBeanType();

    abstract protected Class getMXBeanClass();

    abstract protected long getValueFromBean(Object bean);

    public void getValue(StringBuffer res) {
        Iterator it = beans.iterator();
        long value = 0;
        while (it.hasNext()) {
            value += getValueFromBean(it.next());
        }
        res.append(Long.toString(value));
    }
}
