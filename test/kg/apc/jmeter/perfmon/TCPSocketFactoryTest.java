package kg.apc.jmeter.perfmon;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author APC
 */
public class TCPSocketFactoryTest
{
   public TCPSocketFactoryTest()
   {
   }

   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   @Before
   public void setUp()
   {
   }

   @After
   public void tearDown()
   {
   }

   @Test
   public void testCreateSocket_String_int() throws Exception
   {
      System.out.println("createSocket");
      String string = "google.com"; // maybe test should not be dependant on internet connection
      int i = 80;
      TCPSocketFactory instance = new TCPSocketFactory();
      Socket result = instance.createSocket(string, i);
      assertNotNull(result);
   }

   @Test
   public void testCreateSocket_4args_1() throws Exception
   {
      System.out.println("createSocket");
      TCPSocketFactory instance = new TCPSocketFactory();
      InetAddress ia = null;
      try
      {
         instance.createSocket("", 1, ia, 1);
         fail("Exception expected");
      }
      catch (UnsupportedOperationException e)
      {
      }
   }

   @Test
   public void testCreateSocket_InetAddress_int() throws Exception
   {
      System.out.println("createSocket");
      InetAddress ia = null;
      TCPSocketFactory instance = new TCPSocketFactory();
      try
      {
         instance.createSocket(ia, 0);
         fail("Exception expected");
      }
      catch (UnsupportedOperationException e)
      {
      }
   }

   @Test
   public void testCreateSocket_4args_2() throws Exception
   {
      System.out.println("createSocket");
      InetAddress ia = null;
      TCPSocketFactory instance = new TCPSocketFactory();
      try
      {
         instance.createSocket("", 1, ia, 1);
         fail("Exception expected");
      }
      catch (UnsupportedOperationException e)
      {
      }
   }
}
