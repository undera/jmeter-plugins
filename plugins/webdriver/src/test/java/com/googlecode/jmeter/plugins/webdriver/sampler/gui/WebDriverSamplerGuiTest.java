package com.googlecode.jmeter.plugins.webdriver.sampler.gui;

import com.googlecode.jmeter.plugins.webdriver.sampler.WebDriverSampler;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WebDriverSamplerGuiTest {

    private WebDriverSamplerGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createGui() {
        gui = new WebDriverSamplerGui();
    }

    @Test
    public void shouldImplementJMeterGUIComponent() {
        assertThat(gui, is(CoreMatchers.instanceOf(JMeterGUIComponent.class)));
    }

    @Test
    public void shouldReturnStaticLabel() {
        assertThat(gui.getStaticLabel(), containsString("WebDriver Sampler"));
    }

    @Test
    public void shouldReturnCanonicalClassNameAsLabelResource() {
        assertThat(gui.getLabelResource(), is(gui.getClass().getCanonicalName()));
    }

    @Test
    public void shouldSetParameters() {
        gui.parameters.setText("parameter1 parameter2");
        final WebDriverSampler testElement = (WebDriverSampler) gui.createTestElement();
        assertThat(testElement.getParameters(), is("parameter1 parameter2"));
    }

    @Test
    public void shouldSetScript() {
        gui.script.setText("some script");
        final WebDriverSampler testElement = (WebDriverSampler) gui.createTestElement();
        assertThat(testElement.getScript(), is("some script"));
    }

    @Test
    public void shouldResetValuesOnClearGui() {
        gui.script.setText("script");
        gui.parameters.setText("p1 p2");

        gui.clearGui();

        assertThat(gui.script.getText(), is(WebDriverSampler.defaultScript));
        assertThat(gui.parameters.getText(), is(""));
        assertThat((String) gui.languages.getSelectedItem(), is(WebDriverSampler.DEFAULT_ENGINE));
    }

    @Test
    public void shouldSetValuesOnConfigure() {
        WebDriverSampler sampler = new WebDriverSampler();
        sampler.setParameters("param1 param2");
        sampler.setScript("var script;");

        gui.configure(sampler);

        assertThat(gui.script.getText(), is(sampler.getScript()));
        assertThat(gui.parameters.getText(), is(sampler.getParameters()));
    }
}
