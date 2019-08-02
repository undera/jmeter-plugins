package kg.apc.charting.colors;

import kg.apc.charting.ColorsDispatcher;
import kg.apc.charting.ColorsDispatcherFactory;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JMeterUtils.class})
public class HueRotateTest {
    @After
    public void validate() {
        validateMockitoUsage();
    }

    /**
     * Test factory retrieves huerotate with null options
     */
    @Test
    public void testFactoryNullPaletteNull() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("huerotate");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        // FIXME: PowerMockito.verifyStatic(JMeterUtils.class);
        assertEquals("HueRotatePalette", instance.getClass().getSimpleName());
    }

    /**
     * Test factory retrieves huerotate with empty options
     */
    @Test
    public void testFactoryEmptyPaletteEmpty() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("huerotate");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        // FIXME: PowerMockito.verifyStatic(JMeterUtils.class);
        assertEquals("HueRotatePalette", instance.getClass().getSimpleName());
    }

    /**
     * Test factory retrieves huerotate with partial options
     */
    @Test
    public void testFactoryEmptyPalettePartial1() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("huerotate");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("9C27B0");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        // FIXME: PowerMockito.verifyStatic(JMeterUtils.class);
        assertEquals("HueRotatePalette", instance.getClass().getSimpleName());
    }

    /**
     * Test factory retrieves huerotate with partial options
     */
    @Test
    public void testFactoryEmptyPalettePartial2() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("huerotate");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("9C27B0,8");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        // FIXME: PowerMockito.verifyStatic(JMeterUtils.class);
        assertEquals("HueRotatePalette", instance.getClass().getSimpleName());
    }

    /**
     * Test of getNextColor method, of class ColorsDispatcher.
     */
    @Test
    public void testGetNextColor() {
        PowerMockito.mockStatic(JMeterUtils.class);
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("huerotate");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("9C27B0,8,4");
        ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
        // FIXME: PowerMockito.verifyStatic(JMeterUtils.class);
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
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("huerotate");
        PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("9C27B0,8,4");
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
            PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("huerotate");
            PowerMockito.when(JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("9C27B0,8,4");
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(instance);
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }

}
