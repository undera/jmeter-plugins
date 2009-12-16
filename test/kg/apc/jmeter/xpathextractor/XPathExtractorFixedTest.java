package kg.apc.jmeter.xpathextractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

/*
This tests normally will fail
because xerces XML parser doesn't have handling of W3C DTD URLs
Those DTDs returns 503 error, and parser fails.
Workaround is to add to lmhosts record for www.w3.org,
Target that record to own server and place some DTDs there
Maybe there's more beautiful solution, but I don't know it yet.
"Use namespaces" flag should somehow affect this problem, but it has no visible effect...
 */
public class XPathExtractorFixedTest
      extends TestCase
{
   XPathExtractorFixed extractor;
   SampleResult result;
   JMeterVariables vars;

   public XPathExtractorFixedTest(String name)
   {
      super(name);
   }
   private JMeterContext jmctx = null;
   private final static String VAL_NAME = "value";
   private final static String VAL_NAME_NR = "value_matchNr";

   // code from http://snippets.dzone.com/posts/show/555
   public static String slurp(InputStream in) throws IOException
   {
      StringBuffer out = new StringBuffer();
      byte[] b = new byte[4096];
      for (int n; (n = in.read(b)) != -1;)
      {
         out.append(new String(b, 0, n));
      }
      return out.toString();
   }

   @Override
   public void setUp()
   {
      jmctx = JMeterContextService.getContext();
      extractor = new XPathExtractorFixed();
      extractor.setThreadContext(jmctx);// This would be done by the run command
      extractor.setRefName(VAL_NAME);
      extractor.setDefaultValue("FAILED!");
      result = new SampleResult();

      InputStream is = getClass().getResourceAsStream("/kg/apc/jmeter/xpathextractor/f-online.htm");
      String data = null;
      try
      {
         data = slurp(is);
      }
      catch (IOException ex)
      {
         Logger.getLogger(XPathExtractorFixedTest.class.getName()).log(Level.SEVERE, null, ex);
      }

      result.setResponseData(data.getBytes());
      vars = new JMeterVariables();
      jmctx.setVariables(vars);
      jmctx.setPreviousResult(result);
   }

   // this test succeeds when fake www.w3.org used, fails otherwise
   public void testAttributeExtraction_NotTolerant_NotNamespace() throws Exception
   {
      extractor.setTolerant(false); // this test not using tidy, tidy is greedy
      extractor.setNameSpace(false); // this seems to make no sense... but it should disable DTD checking I think!
      extractor.setXPathQuery("//input[@id='ctl00_BCT_CACHE']/@value");
      extractor.process();
      assertEquals("3c03c150d3da47afac1626cfd4fb4346", vars.get(VAL_NAME));
      assertEquals("1", vars.get(VAL_NAME_NR));
      assertEquals("3c03c150d3da47afac1626cfd4fb4346", vars.get(VAL_NAME + "_1"));
      assertNull(vars.get(VAL_NAME + "_2"));
   }

   // this test will fail always
   public void testAttributeExtraction_NotTolerant_UseNamespace() throws Exception
   {
      extractor.setTolerant(false); // this test not using tidy, tidy is greedy
      extractor.setNameSpace(true); // this seems to make no sense... but it should disable DTD checking I think!
      extractor.setXPathQuery("//input[@id='ctl00_BCT_CACHE']/@value");
      extractor.process();
      assertEquals("3c03c150d3da47afac1626cfd4fb4346", vars.get(VAL_NAME));
      assertEquals("1", vars.get(VAL_NAME_NR));
      assertEquals("3c03c150d3da47afac1626cfd4fb4346", vars.get(VAL_NAME + "_1"));
      assertNull(vars.get(VAL_NAME + "_2"));
   }

   // this one should succeed anyway, because of tidy usage
   public void testAttributeExtraction_Tolerant() throws Exception
   {
      extractor.setTolerant(true);
      extractor.setNameSpace(true);
      extractor.setXPathQuery("//input[@id='ctl00_BCT_CACHE']/@value");
      extractor.process();
      assertEquals("3c03c150d3da47afac1626cfd4fb4346", vars.get(VAL_NAME));
      assertEquals("1", vars.get(VAL_NAME_NR));
      assertEquals("3c03c150d3da47afac1626cfd4fb4346", vars.get(VAL_NAME + "_1"));
      assertNull(vars.get(VAL_NAME + "_2"));
   }
}
