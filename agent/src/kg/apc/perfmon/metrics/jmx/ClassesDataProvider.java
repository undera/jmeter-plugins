package kg.apc.perfmon.metrics.jmx;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServerConnection;

/**
 *
 * @author undera
 */
class ClassesDataProvider extends AbstractJMXDataProvider {

    public ClassesDataProvider(MBeanServerConnection mBeanServerConn) throws Exception {
        super(mBeanServerConn);
    }

    protected String getMXBeanType() {
        return ManagementFactory.CLASS_LOADING_MXBEAN_NAME;
    }

    protected Class getMXBeanClass() {
        return ClassLoadingMXBean.class;
    }

    protected long getValueFromBean(Object bean) {
        return ((ClassLoadingMXBean) bean).getLoadedClassCount();
    }
}
