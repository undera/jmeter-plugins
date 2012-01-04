package kg.apc.jmeter.vizualizers;

import javax.swing.JTextField;
import static org.junit.Assert.assertNotNull;
import org.junit.*;

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
        
        // Andrey: sorry, but using JDialog causes CI headless tests to fail.
        // we need the way to workaround it.
        
        //JPerfmonParamsDialog instance = new JPerfmonParamsDialog(null, "CPU", true, new JTextField());
        //assertNotNull(instance);
    }

}