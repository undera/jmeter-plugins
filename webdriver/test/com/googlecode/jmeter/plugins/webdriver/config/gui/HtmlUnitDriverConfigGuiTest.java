package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.HtmlUnitDriverConfig;
import kg.apc.emulators.TestJMeterUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class HtmlUnitDriverConfigGuiTest {

    private HtmlUnitDriverConfigGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createConfig() {
        gui = new HtmlUnitDriverConfigGui();
    }

    @Test
    public void shouldReturnStaticLabel() {
        assertThat(gui.getStaticLabel(), containsString("HtmlUnit Driver Config"));
    }

    @Test
    public void shouldReturnWikiPage() {
        assertThat(gui.getWikiPage(), is("HtmlUnitDriverConfig"));
    }

    @Test
    public void shouldReturnCanonicalClassNameAsLabelResource() {
        assertThat(gui.getLabelResource(), is(gui.getClass().getCanonicalName()));
    }

    @Test
    public void shouldReturnHtmlUnitDriverConfig() {
        assertThat(gui.createTestElement(), is(instanceOf(HtmlUnitDriverConfig.class)));
    }
    
    @Test
	public void shouldEnableProxyAndExperimental() throws Exception {
		assertThat(gui.isExperimentalEnabled(), is(true));
		assertThat(gui.isProxyEnabled(), is(true));
	}
}
