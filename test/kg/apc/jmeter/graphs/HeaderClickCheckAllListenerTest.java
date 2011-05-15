package kg.apc.jmeter.graphs;

import kg.apc.jmeter.graphs.HeaderClickCheckAllListener;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author apc
 */
public class HeaderClickCheckAllListenerTest
{
   /**
    *
    */
   public HeaderClickCheckAllListenerTest()
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
   public void setUp()
   {
   }

   /**
    *
    */
   @After
   public void tearDown()
   {
   }

   /**
    * Test of mouseClicked method, of class HeaderClickCheckAllListener.
    */
   @Test
   public void testMouseClicked()
   {
      System.out.println("mouseClicked");
      JTable table=new JTable();
      MouseEvent evt = new MouseEvent(table.getTableHeader(), 1, 1, 1, 10, 10, 1, true);
      HeaderClickCheckAllListener instance = new HeaderClickCheckAllListener();
      instance.mouseClicked(evt);
   }

   /**
    * Test of mouseClicked method, of class HeaderClickCheckAllListener.
    */
   @Test
   public void testMouseClicked_work()
   {
      System.out.println("mouseClicked");
      JTable table=new JTable();
      table.addColumn(new TableColumn());
      MouseEvent evt = new MouseEvent(table.getTableHeader(), 1, 1, 1, 10, 10, 1, true);
      HeaderClickCheckAllListener instance = new HeaderClickCheckAllListener();
      instance.mouseClicked(evt);
   }
}
