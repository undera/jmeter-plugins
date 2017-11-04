package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.ChromeDriverConfig;
import kg.apc.emulators.TestJMeterUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ChromeDriverConfigGuiTest {

    private ChromeDriverConfigGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createConfig() {
        gui = new ChromeDriverConfigGui();
    }

    @Test
    public void shouldReturnStaticLabel() {
        assertThat(gui.getStaticLabel(), containsString("Chrome Driver Config"));
    }

    @Test
    public void shouldReturnWikiPage() {
        assertThat(gui.getWikiPage(), is("ChromeDriverConfig"));
    }

    @Test
    public void shouldReturnCanonicalClassNameAsLabelResource() {
        assertThat(gui.getLabelResource(), is(gui.getClass().getCanonicalName()));
    }

    @Test
    public void shouldReturnChromeDriverConfig() {
        assertThat(gui.createTestElement(), is(instanceOf(ChromeDriverConfig.class)));
    }

    @Test
    public void shouldSetChromeDriverPath() {
        gui.chromeServicePath.setText("chromedriver");
        final ChromeDriverConfig testElement = (ChromeDriverConfig) gui.createTestElement();
        assertThat(testElement.getChromeDriverPath(), is("chromedriver"));
    }

    @Test
    public void shouldSetAndroidEnabled() {
        gui.androidEnabled.setSelected(true);
        final ChromeDriverConfig testElement = (ChromeDriverConfig) gui.createTestElement();
        assertThat(testElement.isAndroidEnabled(), is(true));
    }

    @Test
    public void shouldSetHeadlessEnabled() {
        gui.getHeadlessEnabled().setSelected(true);
        final ChromeDriverConfig testElement = (ChromeDriverConfig) gui.createTestElement();
        assertThat(testElement.isHeadlessEnabled(), is(true));
    }

    @Test
    public void shouldResetValuesOnClearGui() {
        gui.chromeServicePath.setText("path");
        gui.androidEnabled.setSelected(true);
        gui.getHeadlessEnabled().setSelected(true);

        gui.clearGui();

        assertThat(gui.chromeServicePath.getText(), is(""));
        assertThat(gui.androidEnabled.isSelected(), is(false));
        assertThat(gui.getHeadlessEnabled().isSelected(), is(false));
    }

    @Test
    public void shouldSetChromeDriverPathOnConfigure() {
        ChromeDriverConfig config = new ChromeDriverConfig();
        config.setChromeDriverPath("chromedriver.path");
        gui.configure(config);

        assertThat(gui.chromeServicePath.getText(), is(config.getChromeDriverPath()));
    }

    @Test
    public void shouldSetAndroidEnabledOnConfigure() {
        ChromeDriverConfig config = new ChromeDriverConfig();
        config.setAndroidEnabled(true);
        gui.configure(config);

        assertThat(gui.androidEnabled.isSelected(), is(config.isAndroidEnabled()));
    }

    @Test
    public void shouldSetHeadlessEnabledOnConfigure() {
        ChromeDriverConfig config = new ChromeDriverConfig();
        config.setHeadlessEnabled(true);
        gui.configure(config);

        assertThat(gui.getHeadlessEnabled().isSelected(), is(config.isHeadlessEnabled()));
    }

    @Test
	public void shouldEnableProxyAndExperimental() throws Exception {
		assertThat(gui.isExperimentalEnabled(), is(true));
		assertThat(gui.isProxyEnabled(), is(true));
	}

}
