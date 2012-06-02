package kg.apc.perfmon.metrics.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import javax.management.MBeanServerConnection;

/**
 *
 * @author undera
 */
class MemoryDataProvider extends AbstractJMXDataProvider {

    public static final int TYPE_USED = 0;
    public static final int TYPE_COMMITTED = 1;
    private int type = TYPE_USED;

    public MemoryDataProvider(MBeanServerConnection mBeanServerConn, boolean diff, int aType) throws Exception {
        super(mBeanServerConn, diff);
        type = aType;
        bytesValue = true;
    }

    protected String getMXBeanType() {
        return ManagementFactory.MEMORY_MXBEAN_NAME;
    }

    protected Class getMXBeanClass() {
        return MemoryMXBean.class;
    }

    protected long getValueFromBean(Object bean) {
        if (type == TYPE_COMMITTED) {
            return ((MemoryMXBean) bean).getHeapMemoryUsage().getCommitted();
        } else {
            return ((MemoryMXBean) bean).getHeapMemoryUsage().getUsed();
        }
    }
}
