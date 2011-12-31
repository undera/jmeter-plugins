package kg.apc.jmeter.vizualizers;

import javax.swing.JTextField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stephane Hoblingre
 */
public class JPerfmonParamsDialogTest {

    public JPerfmonParamsDialogTest() {
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
        //no tests as of now
        JPerfmonParamsDialog instance = new JPerfmonParamsDialog(null, "CPU", true, new JTextField());
        assertNotNull(instance);
    }

}