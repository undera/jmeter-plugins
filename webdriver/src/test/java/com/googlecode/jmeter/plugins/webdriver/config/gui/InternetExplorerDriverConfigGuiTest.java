package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.InternetExplorerDriverConfig;
import kg.apc.emulators.TestJMeterUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class InternetExplorerDriverConfigGuiTest {

    private InternetExplorerDriverConfigGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createConfig() {
        gui = new InternetExplorerDriverConfigGui();
    }

    @Test
    public void shouldReturnStaticLabel() {
        assertThat(gui.getStaticLabel(), containsString("Internet Explorer Driver Config"));
    }

    @Test
    public void shouldReturnWikiPage() {
        assertThat(gui.getWikiPage(), is("InternetExplorerConfig"));
    }

    @Test
    public void shouldReturnCanonicalClassNameAsLabelResource() {
        assertThat(gui.getLabelResource(), is(gui.getClass().getCanonicalName()));
    }

    @Test
    public void shouldReturnInternetExplorerDriverConfig() {
        assertThat(gui.createTestElement(), is(instanceOf(InternetExplorerDriverConfig.class)));
    }

    @Test
    public void shouldSetInternetExplorerDriverPath() {
        gui.ieServicePath.setText("iedriver");
        final InternetExplorerDriverConfig testElement = (InternetExplorerDriverConfig) gui.createTestElement();
        assertThat(testElement.getInternetExplorerDriverPath(), is("iedriver"));
    }

    @Test
    public void shouldResetValuesOnClearGui() {
        gui.ieServicePath.setText("path");

        gui.clearGui();

        assertThat(gui.ieServicePath.getText(), is(""));
    }

    @Test
    public void shouldSetInternetExplorerDriverPathOnConfigure() {
        InternetExplorerDriverConfig config = new InternetExplorerDriverConfig();
        config.setInternetExplorerDriverPath("iedriver.path");
        gui.configure(config);

        assertThat(gui.ieServicePath.getText(), is(config.getInternetExplorerDriverPath()));
    }

}
