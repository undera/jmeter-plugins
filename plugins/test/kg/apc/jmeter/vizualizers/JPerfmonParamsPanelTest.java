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
public class JPerfmonParamsPanelTest {

    public JPerfmonParamsPanelTest() {
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
        JPerfmonParamsPanel instance = new JPerfmonParamsPanel("CPU", new JTextField("core=2:pid=1234:label=myLabel:total"));
        assertNotNull(instance);
    }

}