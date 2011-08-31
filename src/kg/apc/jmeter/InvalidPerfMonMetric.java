package kg.apc.jmeter;

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
