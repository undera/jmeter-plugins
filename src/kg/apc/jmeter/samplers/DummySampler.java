package kg.apc.jmeter.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class DummySampler
      extends AbstractSampler
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   public static String IS_SUCCESSFUL = "Successful";
   public static String RESPONSE_CODE = "Response Code (e.g. 200)";
   public static String RESPONSE_MESSAGE = "Response Message (e.g. OK)";
   public static String RESPONSE_DATA = "Response Data";
   private boolean successfull = true;
   private String responseCode = "";
   private String responseMessage = "";
   private String responseData = "";

   public SampleResult sample(Entry e)
   {
      log.info(responseData);

      SampleResult res = new SampleResult();
      res.setSampleLabel(getName());

      // response time
      res.sampleStart();

      // source data
      res.setSamplerData("");

      // response code
      res.setResponseCode(responseCode);
      res.setResponseMessage(responseMessage);
      res.setSuccessful(successfull);

      // responde data
      res.setDataType(SampleResult.TEXT);
      res.setResponseData(responseData.getBytes());

      res.sampleEnd();

      return res;
   }

   public void setSuccessful(boolean selected)
   {
      setProperty(IS_SUCCESSFUL, selected);
      successfull = selected;
   }

   public void setResponseCode(String text)
   {
      setProperty(RESPONSE_CODE, text);
      responseCode = text;
   }

   public void setResponseMessage(String text)
   {
      setProperty(RESPONSE_MESSAGE, text);
      responseMessage = text;
   }

   public void setResponseData(String text)
   {
      setProperty(RESPONSE_DATA, text);
      responseData = text;
   }
}
