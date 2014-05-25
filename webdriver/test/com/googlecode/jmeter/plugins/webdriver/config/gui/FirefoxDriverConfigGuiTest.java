package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.FirefoxDriverConfig;
import kg.apc.emulators.TestJMeterUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class FirefoxDriverConfigGuiTest {

    private FirefoxDriverConfigGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createConfig() {
        gui = new FirefoxDriverConfigGui();
    }

    @Test
    public void shouldReturnStaticLabel() {
        assertThat(gui.getStaticLabel(), containsString("Firefox Driver Config"));
    }

    @Test
    public void shouldReturnWikiPage() {
        assertThat(gui.getWikiPage(), is("FirefoxDriverConfig"));
    }

    @Test
    public void shouldReturnCanonicalClassNameAsLabelResource() {
        assertThat(gui.getLabelResource(), is(gui.getClass().getCanonicalName()));
    }

    @Test
    public void shouldReturnFirefoxDriverConfig() {
        assertThat(gui.createTestElement(), is(instanceOf(FirefoxDriverConfig.class)));
    }

    @Test
    public void shouldSetUserAgentOverride() {
        gui.useragentOverride.setText("some user agent");
        final FirefoxDriverConfig testElement = (FirefoxDriverConfig) gui.createTestElement();
        assertThat(testElement.getUserAgentOverride(), is("some user agent"));
    }

    @Test
    public void shouldResetValuesOnClearGui() {
        gui.useragentOverride.setText("user agent");

        gui.clearGui();

        assertThat(gui.useragentOverride.getText(), is(""));
    }

    @Test
    public void shouldSetUserAgentOverrideOnConfigure() {
        FirefoxDriverConfig config = new FirefoxDriverConfig();
        config.setUserAgentOverride("user-agent");
        gui.configure(config);

        assertThat(gui.useragentOverride.getText(), is(config.getUserAgentOverride()));
    }
}
