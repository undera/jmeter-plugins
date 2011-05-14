package kg.apc.jmeter.perfmon;

import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author APC
 */
public class PerfMonSampleResult
      extends SampleResult {

   public PerfMonSampleResult() {
      sampleStart();
      sampleEnd();
   }
}
