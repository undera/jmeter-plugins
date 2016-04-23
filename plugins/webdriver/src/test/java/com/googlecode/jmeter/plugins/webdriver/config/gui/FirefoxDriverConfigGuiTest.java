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
    public void shouldOverrideUserAgent() {
        gui.userAgentOverrideCheckbox.setSelected(true);
        gui.userAgentOverrideText.setText("some user agent");
        final FirefoxDriverConfig testElement = (FirefoxDriverConfig) gui.createTestElement();
        assertThat(testElement.getUserAgentOverride(), is("some user agent"));
    }

    @Test
    public void shouldNotOverrideUserAgent() {
        gui.userAgentOverrideCheckbox.setSelected(false);
        gui.userAgentOverrideText.setText("some user agent");
        final FirefoxDriverConfig testElement = (FirefoxDriverConfig) gui.createTestElement();
        assertThat(testElement.getUserAgentOverride(), is(not("some user agent")));
    }

    @Test
    public void shouldResetValuesOnClearGui() {
        gui.userAgentOverrideText.setText("user agent");
        gui.userAgentOverrideCheckbox.setSelected(true);

        gui.clearGui();

        assertThat(gui.userAgentOverrideText.getText(), is(""));
        assertThat(gui.userAgentOverrideCheckbox.isSelected(), is(false));
    }

    @Test
    public void shouldSetFirefoxDriverConfigOnConfigure() {
        FirefoxDriverConfig config = new FirefoxDriverConfig();
        config.setUserAgentOverride("user-agent");
        config.setUserAgentOverridden(true);
        gui.configure(config);

        assertThat(gui.userAgentOverrideText.getText(), is(config.getUserAgentOverride()));
        assertThat(gui.userAgentOverrideCheckbox.isSelected(), is(config.isUserAgentOverridden()));
    }
    
    @Test
	public void shouldEnableProxyAndExperimental() throws Exception {
		assertThat(gui.isExperimentalEnabled(), is(true));
		assertThat(gui.isProxyEnabled(), is(true));
	}
}
