package kg.apc.jmeter.perfmon;

public interface PerfMonSampleGenerator {

    void generate2Samples(long[] netIO, String string, String string0, double d);

    void generate2Samples(long[] disksIO, String string, String string0);

    void generateSample(double d, String string);

    void generateErrorSample(String label, String errorMsg);
}
