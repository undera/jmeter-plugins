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

   public SampleResult sample(Entry e)
   {
      log.info(getResponseData());

      SampleResult res = new SampleResult();
      res.setSampleLabel(getName());

      // response time
      res.sampleStart();

      // source data
      res.setSamplerData("");

      // response code
      res.setResponseCode(getResponseCode());
      res.setResponseMessage(getResponseMessage());
      res.setSuccessful(isSuccessfull());

      // responde data
      res.setDataType(SampleResult.TEXT);
      res.setResponseData(getResponseData().getBytes());

      res.sampleEnd();

      return res;
   }

   public void setSuccessful(boolean selected)
   {
      setProperty(IS_SUCCESSFUL, selected);
   }

   public void setResponseCode(String text)
   {
      setProperty(RESPONSE_CODE, text);
   }

   public void setResponseMessage(String text)
   {
      setProperty(RESPONSE_MESSAGE, text);
   }

   public void setResponseData(String text)
   {
      setProperty(RESPONSE_DATA, text);
   }

   /**
    * @return the successfull
    */
   public boolean isSuccessfull()
   {
      return getPropertyAsBoolean(IS_SUCCESSFUL);
   }

   /**
    * @return the responseCode
    */
   public String getResponseCode()
   {
      return getPropertyAsString(RESPONSE_CODE);
   }

   /**
    * @return the responseMessage
    */
   public String getResponseMessage()
   {
      return getPropertyAsString(RESPONSE_MESSAGE);
   }

   /**
    * @return the responseData
    */
   public String getResponseData()
   {
      return getPropertyAsString(RESPONSE_DATA);
   }
}
