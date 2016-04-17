package com.blazemeter.jmeter.webdriver.phantomjs;

import kg.apc.emulators.TestJMeterUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PhantomJSDriverConfigGuiTest {

    private PhantomJSDriverConfigGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createConfig() {
        gui = new PhantomJSDriverConfigGui();
    }

    @Test
    public void shouldReturnStaticLabel() {
        assertThat(gui.getStaticLabel(), containsString("PhantomJS Driver Config"));
    }

    @Test
    public void shouldReturnCanonicalClassNameAsLabelResource() {
        assertThat(gui.getLabelResource(), is(gui.getClass().getCanonicalName()));
    }

    @Test
    public void shouldReturnPhantomJSDriverConfig() {
        assertThat(gui.createTestElement(), is(instanceOf(PhantomJSDriverConfig.class)));
    }

    @Test
	public void aCycle() throws Exception {
		final PhantomJSDriverConfig testElement = (PhantomJSDriverConfig) gui.createTestElement();
        gui.modifyTestElement(testElement);
        gui.configure(testElement);
        gui.clearGui();
	}
}
