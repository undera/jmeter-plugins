package kg.apc.jmeter.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class DummySampler
      extends AbstractSampler
{
   public SampleResult sample(Entry e)
   {
      SampleResult res = new SampleResult();
      res.setSampleLabel(getName());

      // source data
      res.setSamplerData("");

      // response code
      res.setResponseCode(getResponseCode());
      res.setResponseMessage(getResponseMessage());
      res.setSuccessful(isSuccessfull());

      // responde data
      res.setDataType(SampleResult.TEXT);
      res.setResponseData(getResponseData().getBytes());

      return res;
   }

   private String getResponseData()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private String getResponseCode()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private String getResponseMessage()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   private boolean isSuccessfull()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }
}
