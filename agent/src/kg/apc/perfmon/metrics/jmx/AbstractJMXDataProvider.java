//"metrics:jmx:url=localhost\:4711:type=memorypool-usage	jmx:url=localhost\:4711:type=memory-usage	jmx:url=localhost\:4711:type=class-count	jmx:url=localhost\:4711:type=gc-time	jmx:url=localhost\:4711:type=compile-time"
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

    protected final Set beans;
    private final boolean isDiff;
    private long prevValue = 0;

    public AbstractJMXDataProvider(MBeanServerConnection mBeanServerConn, boolean diff) throws Exception {
        isDiff = diff;
        beans = getMXBeans(mBeanServerConn);
    }

    public static AbstractJMXDataProvider getProvider(MBeanServerConnection mBeanServerConn, String[] params) throws Exception {
        for (int i = 0; i < params.length; i++) {
            if (params[i].startsWith("type=")) {
                String sType = MetricParams.getParamValue(params[i]);
                if (sType.startsWith("gc-")) {
                    return new GCDataProvider(mBeanServerConn, true);
                } else if (sType.startsWith("class-")) {
                    return new ClassesDataProvider(mBeanServerConn, false);
                } else if (sType.startsWith("compile-")) {
                    return new CompilerDataProvider(mBeanServerConn, true);
                } else if (sType.startsWith("memorypool-")) {
                    return new MemoryPoolDataProvider(mBeanServerConn, false);
                } else if (sType.startsWith("memory-")) {
                    return new MemoryDataProvider(mBeanServerConn, false);
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

        if (isDiff) {
            if (prevValue == 0) {
                prevValue = value;
                value = 0;
            } else {
                long oldVal = value;
                value -= prevValue;
                prevValue = oldVal;
            }
        }

        res.append(Long.toString(value));
    }
}
