package kg.apc.charting.colors;

import java.io.*;
import java.util.ArrayList;
import java.awt.Color;

import kg.apc.charting.ColorsDispatcher;
import kg.apc.charting.ColorsDispatcherFactory;
import org.junit.*;

import static org.junit.Assert.*;

public class CycleColorsTest {

    /**
     *
     */
    public CycleColorsTest() {
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
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        for (int n = 0; n < 2000; n++) {
            Color c = instance.getNextColor();
            System.out.println(c);
            Assert.assertNotNull(c);
        }
    }

    /**
     * Test of reset method, of class ColorsDispatcher.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        Color first = instance.getNextColor();
        Assert.assertNotNull(first);
        instance.getNextColor();
        instance.getNextColor();
        instance.reset();
        assertEquals(first, instance.getNextColor());
    }

    /**
     * Test that the object is serializable
     */
    @Test
    public void testSerialization() {
        try {
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(instance);
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    public void testDemoTable() throws FileNotFoundException, IOException {
        System.out.println("test");
        FileOutputStream os = new FileOutputStream(File.createTempFile("test", ".html"));
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
                    } else if ((r + g + b) / 3 > 256 - 64) {
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
