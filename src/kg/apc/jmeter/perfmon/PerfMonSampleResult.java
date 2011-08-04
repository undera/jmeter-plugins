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

   // storing as latency, multiply by 1000 to keep floating precision
   public void setValue(double value) {
      setLatency((long)(value*1000));
   }

   public double getValue() {
       return ((double)getLatency())/1000d;
   }
}
