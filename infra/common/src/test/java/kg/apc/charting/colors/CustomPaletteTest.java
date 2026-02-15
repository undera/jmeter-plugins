package kg.apc.charting.colors;

import kg.apc.charting.ColorsDispatcher;
import kg.apc.charting.ColorsDispatcherFactory;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;

public class CustomPaletteTest {

    @Test
    public void testFactoryNullPaletteNull() {
        try (MockedStatic<JMeterUtils> mocked = Mockito.mockStatic(JMeterUtils.class)) {
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            assertEquals("CustomPalette", instance.getClass().getSimpleName());
        }
    }

    @Test
    public void testFactoryEmptyPaletteEmpty() {
        try (MockedStatic<JMeterUtils> mocked = Mockito.mockStatic(JMeterUtils.class)) {
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("");
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            assertEquals("CustomPalette", instance.getClass().getSimpleName());
        }
    }

    @Test
    public void testFactoryEmptyPaletteSingle() {
        try (MockedStatic<JMeterUtils> mocked = Mockito.mockStatic(JMeterUtils.class)) {
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("9C27B0");
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            assertEquals("CustomPalette", instance.getClass().getSimpleName());
        }
    }

    @Test
    public void testFactoryEmptyPaletteMany() {
        try (MockedStatic<JMeterUtils> mocked = Mockito.mockStatic(JMeterUtils.class)) {
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("9C27B0,9C27B0,9C27B0,9C27B0,9C27B0,9C27B0,9C27B1,9C27B2,9C27B3,9C27B4,9C27B5,9C27B6,9C27B7,9C27B8,9C27B9,9C27BA,9C27BB,9C27BC,9C27BD,9C27BD,9C27BE,9C27BF");
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            assertEquals("CustomPalette", instance.getClass().getSimpleName());
        }
    }

    @Test
    public void testFactoryEmptyPaletteInvalidColors() {
        try (MockedStatic<JMeterUtils> mocked = Mockito.mockStatic(JMeterUtils.class)) {
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn("9C27B0,9C27B0,9C27B0,9Ca27B0,9C27B0,9C27B0,9C2f7B1,9C27B2,9C2f7B3,9C27B4,9C27B5,9C27B6,9C27B7,9C27B8,9C27B9,9C27BA,9C27BB,9C27BC,9C27BD,9C27BD,9C27BE,9C27BF");
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            assertEquals("CustomPalette", instance.getClass().getSimpleName());
        }
    }

    @Test
    public void testGetNextColorNull() {
        try (MockedStatic<JMeterUtils> mocked = Mockito.mockStatic(JMeterUtils.class)) {
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            for (int n = 0; n < 2000; n++) {
                Color c = instance.getNextColor();
                Assert.assertNotNull(c);
            }
        }
    }

    @Test
    public void testReset() {
        try (MockedStatic<JMeterUtils> mocked = Mockito.mockStatic(JMeterUtils.class)) {
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            Color first = instance.getNextColor();
            Assert.assertNotNull(first);
            instance.getNextColor();
            instance.getNextColor();
            instance.reset();
            assertEquals(first, instance.getNextColor());
        }
    }

    @Test
    public void testSerialization() {
        try (MockedStatic<JMeterUtils> mocked = Mockito.mockStatic(JMeterUtils.class)) {
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher")).thenReturn("custompalette");
            mocked.when(() -> JMeterUtils.getProperty("jmeterPlugin.customColorsDispatcher.options")).thenReturn(null);
            ColorsDispatcher instance = ColorsDispatcherFactory.getColorsDispatcher();
            new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(instance);
            Assert.assertTrue(true);
        } catch (IOException e) {
            Assert.fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
