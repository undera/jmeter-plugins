package com.blazemeter.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.assertEquals;

public class DirectoryListingConfigGuiTest {

    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

//    @Test
    public void showGui() throws Exception {
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            DirectoryListingConfigGui gui = new DirectoryListingConfigGui();
            JDialog frame = new JDialog();
            frame.add(gui);

            frame.setPreferredSize(new Dimension(800, 600));
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            while (frame.isVisible()) {
                Thread.sleep(100);
            }
        }
    }

    @Test
    public void testGui() throws Exception {
        DirectoryListingConfigGui gui = new DirectoryListingConfigGui();

        DirectoryListingConfig element1 = (DirectoryListingConfig) gui.createTestElement();
        DirectoryListingConfig element2 = (DirectoryListingConfig) gui.createTestElement();

        element1.setSourceDirectory("/tmp/csv/");
        element1.setDestinationVariableName("fname");
        element1.setUseFullPath(true);
        element1.setRandomOrder(true);
        element1.setRecursiveListing(true);
        element1.setRewindOnTheEnd(true);
        element1.setIndependentListPerThread(true);
        element1.setReReadDirectoryOnTheEndOfList(true);

        gui.configure(element1);
        gui.modifyTestElement(element2);

        assertEquals(element1.getSourceDirectory(), element2.getSourceDirectory());
        assertEquals(element1.getDestinationVariableName(), element2.getDestinationVariableName());
        assertEquals(element1.getUseFullPath(), element2.getUseFullPath());
        assertEquals(element1.getRandomOrder(), element2.getRandomOrder());
        assertEquals(element1.getRecursiveListing(), element2.getRecursiveListing());
        assertEquals(element1.getRewindOnTheEnd(), element2.getRewindOnTheEnd());
        assertEquals(element1.getIndependentListPerThread(), element2.getIndependentListPerThread());
        assertEquals(element1.getReReadDirectoryOnTheEndOfList(), element2.getReReadDirectoryOnTheEndOfList());

        gui.clearGui();
        gui.modifyTestElement(element2);

        assertEquals("", element2.getSourceDirectory());
        assertEquals("", element2.getDestinationVariableName());
        assertEquals(false, element2.getUseFullPath());
        assertEquals(false, element2.getRandomOrder());
        assertEquals(false, element2.getRecursiveListing());
        assertEquals(true, element2.getRewindOnTheEnd());
        assertEquals(false, element2.getIndependentListPerThread());
        assertEquals(false, element2.getReReadDirectoryOnTheEndOfList());
    }
}