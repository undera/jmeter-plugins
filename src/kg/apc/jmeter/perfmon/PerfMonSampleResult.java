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

   // storing as latency
   public void setValue(long value) {
      setLatency(value);
   }
}
