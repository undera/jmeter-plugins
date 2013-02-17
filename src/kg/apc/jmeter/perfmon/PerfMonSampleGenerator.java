package kg.apc.jmeter.perfmon;

/**
 *
 * @author undera
 */
public interface PerfMonSampleGenerator {

    public void generate2Samples(long[] netIO, String string, String string0, double d);

    public void generate2Samples(long[] disksIO, String string, String string0);

    public void generateSample(double d, String string);

    public void generateErrorSample(String label, String errorMsg);
}
