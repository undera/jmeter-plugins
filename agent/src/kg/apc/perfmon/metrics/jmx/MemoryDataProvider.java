package kg.apc.perfmon.metrics.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import javax.management.MBeanServerConnection;

/**
 *
 * @author undera
 */
class MemoryDataProvider extends AbstractJMXDataProvider {

    public MemoryDataProvider(MBeanServerConnection mBeanServerConn, boolean diff) throws Exception {
        super(mBeanServerConn, diff);
    }

    protected String getMXBeanType() {
        return ManagementFactory.MEMORY_MXBEAN_NAME;
    }

    protected Class getMXBeanClass() {
        return MemoryMXBean.class;
    }

    protected long getValueFromBean(Object bean) {
        return ((MemoryMXBean) bean).getHeapMemoryUsage().getUsed();
    }
}
