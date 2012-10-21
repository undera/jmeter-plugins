package kg.apc.jmeter.dbmon;

/**
 *
 * @author Stephane Hoblingre
 */
public interface DbMonSampleGenerator {
    public void generateSample(double d, String string);
    public void generateErrorSample(String errorMsg);
}
