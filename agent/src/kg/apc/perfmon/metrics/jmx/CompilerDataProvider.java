package kg.apc.perfmon.metrics.jmx;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServerConnection;

/**
 *
 * @author undera
 */
class CompilerDataProvider extends AbstractJMXDataProvider {

    public CompilerDataProvider(MBeanServerConnection mBeanServerConn) throws Exception {
        super(mBeanServerConn);
    }

    protected String getMXBeanType() {
        return ManagementFactory.COMPILATION_MXBEAN_NAME;
    }

    protected Class getMXBeanClass() {
        return CompilationMXBean.class;
    }

    protected long getValueFromBean(Object bean) {
        return ((CompilationMXBean) bean).getTotalCompilationTime();
    }
    
}
