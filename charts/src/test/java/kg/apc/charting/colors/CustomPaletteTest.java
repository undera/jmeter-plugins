package kg.apc.charting.colors;

import kg.apc.charting.ColorsDispatcher;
import kg.apc.charting.ColorsDispatcherFactory;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { JMeterUtils.class })
public class CustomPaletteTest {

    /**
     *
     */
    public CustomPaletteTest() {
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
     * Test factory retrieves CustomPalette with null options
     */
    @Test
    public void testFactoryNullPaletteNull() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        PowerMockito.verifyStatic();
        assertEquals("CustomPalette", instance.getClass().getSimpleName());
    }

    /**
     * Test factory retrieves CustomPalette with empty options
     */
    @Test
    public void testFactoryEmptyPaletteEmpty() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        PowerMockito.verifyStatic();
        assertEquals("CustomPalette", instance.getClass().getSimpleName());
    }

    /**
     * Test factory retrieves CustomPalette with partial options
     */
    @Test
    public void testFactoryEmptyPaletteSingle() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher"))
                .thenReturn("custompalette");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options"))
                .thenReturn("9C27B0");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        PowerMockito.verifyStatic();
        assertEquals("CustomPalette", instance.getClass().getSimpleName());
    }

    /**
     * Test factory retrieves CustomPalette with partial options
     */
    @Test
    public void testFactoryEmptyPaletteMany() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher"))
                .thenReturn("custompalette");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options"))
                .thenReturn("9C27B0,9C27B0,9C27B0,9C27B0,9C27B0,9C27B0,9C27B1,9C27B2,9C27B3,9C27B4,9C27B5,9C27B6,9C27B7,9C27B8,9C27B9,9C27BA,9C27BB,9C27BC,9C27BD,9C27BD,9C27BE,9C27BF");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        PowerMockito.verifyStatic();
        assertEquals("CustomPalette", instance.getClass().getSimpleName());
    }

    /**
     * Test factory retrieves CustomPalette with partial options
     */
    @Test
    public void testFactoryEmptyPaletteInvalidColors() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher"))
                .thenReturn("custompalette");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options"))
                .thenReturn("9C27B0,9C27B0,9C27B0,9Ca27B0,9C27B0,9C27B0,9C2f7B1,9C27B2,9C2f7B3,9C27B4,9C27B5,9C27B6,9C27B7,9C27B8,9C27B9,9C27BA,9C27BB,9C27BC,9C27BD,9C27BD,9C27BE,9C27BF");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        PowerMockito.verifyStatic();
        assertEquals("CustomPalette", instance.getClass().getSimpleName());
    }

    /**
     * Test of getNextColor method, of class ColorsDispatcher.
     */
    @Test
    public void testGetNextColorNull() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        PowerMockito.verifyStatic();
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
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
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
            PowerMockito.mockStatic(JMeterUtils.class);
            PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(instance);
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }

}
