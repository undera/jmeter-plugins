package kg.apc.charting;

import kg.apc.charting.ColorsDispatcher;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.io.FileOutputStream;
import kg.apc.emulators.TestJMeterUtils;
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
public class ColorsDispatcherTest {

    /**
     *
     */
    public ColorsDispatcherTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass()
            throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass()
            throws Exception {
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
     * Test of getNextColor method, of class ColorsDispatcher.
     */
    @Test
    public void testGetNextColor() {
        System.out.println("getNextColor");
        ColorsDispatcher instance = new ColorsDispatcher();
        for (int n = 0; n < 2000; n++) {
            Color c = instance.getNextColor();
            System.out.println(c);
            assertNotNull(c);
        }
    }

    /**
     * Test of reset method, of class ColorsDispatcher.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        ColorsDispatcher instance = new ColorsDispatcher();
        Color first = instance.getNextColor();
        assertNotNull(first);
        instance.getNextColor();
        instance.getNextColor();
        instance.reset();
        assertEquals(first, instance.getNextColor());
    }

    @Test
    public void testDemoTable() throws FileNotFoundException, IOException {
        System.out.println("test");
        FileOutputStream os = new FileOutputStream(TestJMeterUtils.getTempDir() + "/test.html");
        os.write("<html><body><p style='width: 100%'>".getBytes());
        ArrayList<Color> assignedColors = new ArrayList<Color>();
        for (int factor_i = 256; factor_i > 0; factor_i /= 2) {
            System.out.println("factor_i " + factor_i);
            for (int factor = 256 - 1; factor >= 0; factor -= factor_i) {
                System.out.println("factor " + factor);
                for (int i = 1; i < 8; i++) {
                    int r = 0, g = 0, b = 0;
                    if ((i & 1) == 1) {
                        r = factor;
                    }
                    if ((i & 2) == 2) {
                        g = factor;
                    }
                    if ((i & 4) == 4) {
                        b = factor;
                    }
                    Color c = new Color(r, g, b);
                    if (assignedColors.contains(c)) {
                        System.out.println("Existing " + r + " " + g + " " + b);
                    } else if ((r + g + b) / 3 < 32) {
                        System.out.println("Too dark " + r + " " + g + " " + b);
                    } else if ((r + g + b) / 3 > 256-64) {
                        System.out.println("Too light " + r + " " + g + " " + b);
                    } else {
                        System.err.println("New " + r + " " + g + " " + b);
                        String s = "<font style='background-color: rgb(" + r + ", " + g + ", " + b + ")' "
                                + "title='" + r + " " + g + " " + b + " / " + (r + g + b) / 3 + "'>&nbsp; &nbsp;</font>";
                        os.write(s.getBytes());
                        assignedColors.add(c);
                    }
                }
            }
        }
        System.out.println("Got colors: " + assignedColors.size());
    }
}
