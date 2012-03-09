package kg.apc.perfmon.metrics.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import javax.management.MBeanServerConnection;

/**
 *
 * @author undera
 */
class MemoryPoolDataProvider extends AbstractJMXDataProvider {

    public MemoryPoolDataProvider(MBeanServerConnection mBeanServerConn, boolean diff) throws Exception {
        super(mBeanServerConn, diff);
    }

    protected String getMXBeanType() {
        return ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE;
    }

    protected Class getMXBeanClass() {
        return MemoryPoolMXBean.class;
    }

    protected long getValueFromBean(Object bean) {
        return ((MemoryPoolMXBean) bean).getUsage().getUsed();
    }
}
