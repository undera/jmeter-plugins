package kg.apc.perfmon.metrics.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import javax.management.MBeanServerConnection;

/**
 *
 * @author undera
 */
class MemoryPoolDataProvider extends AbstractJMXDataProvider {

    public static final int TYPE_USED = 0;
    public static final int TYPE_COMMITTED = 1;
    private int type = TYPE_USED;

    public MemoryPoolDataProvider(MBeanServerConnection mBeanServerConn, boolean diff, int aType) throws Exception {
        super(mBeanServerConn, diff);
        type = aType;
    }

    protected String getMXBeanType() {
        return ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE;
    }

    protected Class getMXBeanClass() {
        return MemoryPoolMXBean.class;
    }

    protected long getValueFromBean(Object bean) {
        if (type == TYPE_COMMITTED) {
            return ((MemoryPoolMXBean) bean).getUsage().getCommitted();
        } else {
            return ((MemoryPoolMXBean) bean).getUsage().getUsed();
        }
    }
}
