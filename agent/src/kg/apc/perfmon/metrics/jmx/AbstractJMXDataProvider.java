package kg.apc.perfmon.metrics.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 *
 * @author undera
 */
abstract public class AbstractJMXDataProvider {

    protected final Set beans;
    private final boolean isDiff;
    private long prevValue = 0;
    protected boolean bytesValue = false;

    public AbstractJMXDataProvider(MBeanServerConnection mBeanServerConn, boolean diff) throws Exception {
        isDiff = diff;
        beans = getMXBeans(mBeanServerConn);
    }

    public static AbstractJMXDataProvider getProvider(MBeanServerConnection mBeanServerConn, String type) throws Exception {
        if (type.startsWith("gc-")) {
            return new GCDataProvider(mBeanServerConn, true);
        } else if (type.startsWith("class-")) {
            return new ClassesDataProvider(mBeanServerConn, false);
        } else if (type.startsWith("compile-")) {
            return new CompilerDataProvider(mBeanServerConn, true);
        } else if (type.startsWith("memorypool-")) {
            if (type.endsWith("-committed")) {
                return new MemoryPoolDataProvider(mBeanServerConn, false, MemoryPoolDataProvider.TYPE_COMMITTED);
            } else {
                return new MemoryPoolDataProvider(mBeanServerConn, false, MemoryPoolDataProvider.TYPE_USED);
            }
        } else if (type.startsWith("memory-")) {
            if (type.endsWith("-committed")) {
                return new MemoryDataProvider(mBeanServerConn, false, MemoryDataProvider.TYPE_COMMITTED);
            } else {
                return new MemoryDataProvider(mBeanServerConn, false, MemoryDataProvider.TYPE_USED);
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

    public boolean isBytesValue() {
       return bytesValue;
    }

    public void getValue(StringBuffer res) {
       getValue(res, 1);
    }

    public void getValue(StringBuffer res, int divider) {
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

        value = value/divider;

        res.append(Long.toString(value));
    }
}
