package kg.apc.jmeter.perfmon;

import kg.apc.jmeter.perfmon.AbstractPerfMonMetric;

/**
 *
 * @author undera
 */
class InvalidPerfMonMetric extends AbstractPerfMonMetric {

    public InvalidPerfMonMetric() {
        super(null);
    }

    @Override
    public void getValue(StringBuilder res) {
        res.append('0');
    }
}
