package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.AndroidDriverConfig;
import kg.apc.emulators.TestJMeterUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Sergey Marakhov
 * @author Linh Pham
 */
public class AndroidDriverConfigGuiTest {

    private AndroidDriverConfigGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createConfig() {
        gui = new AndroidDriverConfigGui();
    }

    @Test
    public void shouldReturnStaticLabel() {
        assertThat(gui.getStaticLabel(), containsString("Android Driver Config"));
    }

    @Test
    public void shouldReturnWikiPage() {
        assertThat(gui.getWikiPage(), is("AndroidDriverConfig"));
    }

    @Test
    public void shouldReturnCanonicalClassNameAsLabelResource() {
        assertThat(gui.getLabelResource(), is(gui.getClass().getCanonicalName()));
    }

    @Test
    public void shouldReturnAndroidDriverConfig() {
        assertThat(gui.createTestElement(), is(instanceOf(AndroidDriverConfig.class)));
    }

    @Test
    public void shouldSetAndroidDriverHostPort() {
        gui.androidDriverHostPort.setText("some_port");
        final AndroidDriverConfig testElement = (AndroidDriverConfig) gui.createTestElement();
        assertThat(testElement.getAndroidDriverHostPort(), is("some_port"));
    }

    @Test
    public void shouldResetValuesOnClearGui() {
        gui.androidDriverHostPort.setText("some_port");
        gui.clearGui();

        assertThat(gui.androidDriverHostPort.getText(), is("8080"));
    }

    @Test
    public void shouldSetAndroidDriverHostPortOnConfigure() {
        AndroidDriverConfig config = new AndroidDriverConfig();
        config.setAndroidDriverHostPort("some_port");
        gui.configure(config);

        assertThat(gui.androidDriverHostPort.getText(), is(config.getAndroidDriverHostPort()));
    }
}
