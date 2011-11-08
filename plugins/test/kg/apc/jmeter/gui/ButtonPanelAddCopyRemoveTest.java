/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.gui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class ButtonPanelAddCopyRemoveTest {

    public ButtonPanelAddCopyRemoveTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        Object[] defaultValues=new String[]{"", "", ""};
        ButtonPanelAddCopyRemove instance = new ButtonPanelAddCopyRemove(null, null, defaultValues);
    }

   /**
    * Test of checkDeleteButtonStatus method, of class ButtonPanelAddCopyRemove.
    */
   @Test
   public void testCheckDeleteButtonStatus() {
      System.out.println("checkDeleteButtonStatus");
      Object[] defaultValues=new String[]{"", "", ""};
      ButtonPanelAddCopyRemove instance = new ButtonPanelAddCopyRemove(null, null, defaultValues);;
      instance.checkDeleteButtonStatus();
   }

}