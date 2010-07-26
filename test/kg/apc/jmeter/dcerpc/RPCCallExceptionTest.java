package kg.apc.jmeter.dcerpc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apc
 */
public class RPCCallExceptionTest
{

   /**
    * 
    */
   public RPCCallExceptionTest()
   {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
   public static void setUpClass()
        throws Exception
   {
   }

    /**
     *
     * @throws Exception
     */
    @AfterClass
   public static void tearDownClass()
        throws Exception
   {
   }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
   public void testSomeMethod()
   {
     RPCCallException e=new RPCCallException("TEST");
   }

}