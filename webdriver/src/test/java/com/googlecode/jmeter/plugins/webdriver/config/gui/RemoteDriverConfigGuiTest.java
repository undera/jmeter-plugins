package com.googlecode.jmeter.plugins.webdriver.config.gui;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.awt.event.FocusEvent;

import kg.apc.emulators.TestJMeterUtils;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.jmeter.plugins.webdriver.config.RemoteCapability;
import com.googlecode.jmeter.plugins.webdriver.config.RemoteDriverConfig;

public class RemoteDriverConfigGuiTest {

    private RemoteDriverConfigGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createConfig() {
        gui = new RemoteDriverConfigGui();
    }

    @Test
    public void shouldReturnStaticLabel() {
        assertThat(gui.getStaticLabel(), containsString("Remote Driver Config"));
    }

    @Test
    public void shouldReturnWikiPage() {
        assertThat(gui.getWikiPage(), is("RemoteDriverConfig"));
    }

    @Test
    public void shouldReturnCanonicalClassNameAsLabelResource() {
        assertThat(gui.getLabelResource(), is(gui.getClass().getCanonicalName()));
    }

    @Test
    public void shouldReturnRemoteDriverConfig() {
        assertThat(gui.createTestElement(), is(instanceOf(RemoteDriverConfig.class)));
    }
    
    @Test
	public void shouldSetTheSeleniumNodeUrl() throws Exception {
		gui.remoteSeleniumGridText.setText("http://my.awesomegrid.com");
		final RemoteDriverConfig testElement = (RemoteDriverConfig) gui.createTestElement();
        assertThat(testElement.getSeleniumGridUrl(), is("http://my.awesomegrid.com"));
	}

    @Test
    public void shouldResetValuesOnClearGui() {
        gui.remoteSeleniumGridText.setText("http://my.awesomegrid.com");
        
        gui.clearGui();

        assertThat(gui.remoteSeleniumGridText.getText(), is(StringUtils.EMPTY));
    }

    @Test
    public void shouldSetRemoteDriverConfigOnConfigure() {
        RemoteDriverConfig config = new RemoteDriverConfig();
        config.setSeleniumGridUrl("my.awesome.grid.com");
        config.setCapability(RemoteCapability.FIREFOX);
        gui.configure(config);

        assertThat(gui.remoteSeleniumGridText.getText(), is(config.getSeleniumGridUrl()));
        assertThat((RemoteCapability)gui.capabilitiesComboBox.getSelectedItem(), is(config.getCapability()));
    }
    
    @Test
	public void shouldFireAMessageWindowWhenTheFocusIsLost() throws Exception {
    	gui.remoteSeleniumGridText.setText("badURL");
    	FocusEvent focusEvent = new FocusEvent(gui.remoteSeleniumGridText, 1);
    	gui.focusLost(focusEvent);
        assertEquals("The selenium grid URL is malformed", gui.errorMsg.getText());
	}
    
    @Test
	public void shouldNotFireAMessageWindowWhenTheURLIsCorrect() throws Exception {
    	
    	gui.remoteSeleniumGridText.setText("http://my.awesomegrid.com");
    	FocusEvent focusEvent = new FocusEvent(gui.remoteSeleniumGridText, 1);
    	gui.focusLost(focusEvent);
        assertEquals("", gui.errorMsg.getText());
	}
    
    @Test
	public void shouldDisableProxyAndExperimental() throws Exception {
		assertThat(gui.isExperimentalEnabled(), is(false));
		assertThat(gui.isProxyEnabled(), is(false));
	}
}
