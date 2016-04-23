package com.googlecode.jmeter.plugins.webdriver.config.gui;

import com.googlecode.jmeter.plugins.webdriver.config.WebDriverConfig;
import com.googlecode.jmeter.plugins.webdriver.proxy.ProxyType;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WebDriverConfigGuiTest {

    private WebDriverConfigGui gui;

    @BeforeClass
    public static void setupJMeterEnv() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Before
    public void createGui() {
        gui = new WebDriverConfigGuiImpl();
    }

    @Test
    public void shouldImplementJMeterGUIComponent() {
        assertThat(gui, is(CoreMatchers.instanceOf(JMeterGUIComponent.class)));
    }

    @Test
    public void shouldCreateSystemProxyByDefault() {
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getProxyType(), is(ProxyType.SYSTEM));
    }

    @Test
    public void shouldCreateSystemProxy() {
        gui.systemProxy.setSelected(true);
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getProxyType(), is(ProxyType.SYSTEM));
    }

    @Test
    public void shouldCreateNoProxy() {
        gui.directProxy.setSelected(true);
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getProxyType(), is(ProxyType.DIRECT));
    }

    @Test
    public void shouldCreateManualProxy() {
        gui.manualProxy.setSelected(true);
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getProxyType(), is(ProxyType.MANUAL));
    }

    @Test
    public void shouldCreateProxyPacProxy() {
        gui.pacUrlProxy.setSelected(true);
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getProxyType(), is(ProxyType.PROXY_PAC));
    }

    @Test
    public void shouldCreateAutoDectetProxy() {
        gui.autoDetectProxy.setSelected(true);
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getProxyType(), is(ProxyType.AUTO_DETECT));
    }

    @Test
    public void shouldSetPacUrl() {
        gui.pacUrl.setText("http://pac.url");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getProxyPacUrl(), is("http://pac.url"));
    }

    @Test
    public void shouldSetHttpHost() {
        gui.httpProxyHost.setText("host");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getHttpHost(), is("host"));
    }

    @Test
    public void shouldSetHttpPort() {
        gui.httpProxyPort.setText("123");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getHttpPort(), is(123));
    }

    @Test
    public void shouldSetHttpPortTo8080ByDefault() {
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getHttpPort(), is(8080));
    }

    @Test
    public void shouldUseHttpSettingsForAllProtocolsByDefault() {
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.isUseHttpSettingsForAllProtocols(), is(true));
    }

    @Test
    public void shouldBeAbleToDisableHttpSettingsForAllProtocols() {
        gui.useHttpSettingsForAllProtocols.setSelected(false);
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.isUseHttpSettingsForAllProtocols(), is(false));
    }

    @Test
    public void shouldSetHttpsHost() {
        gui.httpsProxyHost.setText("host");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getHttpsHost(), is("host"));
    }

    @Test
    public void shouldSetHttpsPort() {
        gui.httpsProxyPort.setText("123");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getHttpsPort(), is(123));
    }

    @Test
    public void shouldSetHttpsPortTo8080ByDefault() {
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getHttpsPort(), is(8080));
    }

    @Test
    public void shouldSetFtpHost() {
        gui.ftpProxyHost.setText("host");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getFtpHost(), is("host"));
    }

    @Test
    public void shouldSetFtpPort() {
        gui.ftpProxyPort.setText("123");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getFtpPort(), is(123));
    }

    @Test
    public void shouldSetFtpPortTo8080ByDefault() {
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getFtpPort(), is(8080));
    }

    @Test
    public void shouldSetSocksHost() {
        gui.socksProxyHost.setText("host");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getSocksHost(), is("host"));
    }

    @Test
    public void shouldSetSocksPort() {
        gui.socksProxyPort.setText("123");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getSocksPort(), is(123));
    }

    @Test
    public void shouldSetSocksPortTo8080ByDefault() {
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getSocksPort(), is(8080));
    }

    @Test
    public void shouldSetNoProxyList() {
        gui.noProxyList.setText("somehost,otherhost");
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getNoProxyHost(), is("somehost,otherhost"));
    }

    @Test
    public void shouldSetLocalhostInNoProxyListByDefault() {
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.getNoProxyHost(), is("localhost"));
    }

    @Test
    public void shouldResetValuesOnClearGui() {
        gui.directProxy.setSelected(true);
        gui.pacUrl.setText("pac-url");
        gui.httpProxyHost.setText("http-host");
        gui.httpProxyPort.setText("123");
        gui.useHttpSettingsForAllProtocols.setSelected(false);
        gui.httpsProxyHost.setText("https-host");
        gui.httpsProxyPort.setText("123");
        gui.ftpProxyHost.setText("ftp-host");
        gui.ftpProxyPort.setText("123");
        gui.socksProxyHost.setText("socks-host");
        gui.socksProxyPort.setText("123");
        gui.noProxyList.setText("no-proxy-host");

        gui.clearGui();

        assertThat(gui.systemProxy.isSelected(), is(true));
        assertThat(gui.pacUrl.getText(), is(""));
        assertThat(gui.httpProxyHost.getText(), is(""));
        assertThat(gui.httpProxyPort.getText(), is("8080"));
        assertThat(gui.useHttpSettingsForAllProtocols.isSelected(), is(true));
        assertThat(gui.httpsProxyHost.getText(), is(""));
        assertThat(gui.httpsProxyPort.getText(), is("8080"));
        assertThat(gui.ftpProxyHost.getText(), is(""));
        assertThat(gui.ftpProxyPort.getText(), is("8080"));
        assertThat(gui.socksProxyHost.getText(), is(""));
        assertThat(gui.socksProxyPort.getText(), is("8080"));
        assertThat(gui.noProxyList.getText(), is("localhost"));
    }

    @Test
    public void shouldSetValuesOnConfigure() {
        WebDriverConfig config = new WebDriverConfigImpl();
        config.setProxyType(ProxyType.AUTO_DETECT);
        config.setProxyPacUrl("pac-url");
        config.setHttpHost("http-host");
        config.setHttpPort(123);
        config.setUseHttpSettingsForAllProtocols(false);
        config.setHttpsHost("https-host");
        config.setHttpPort(123);
        config.setFtpHost("ftp-host");
        config.setFtpPort(123);
        config.setSocksHost("socks-host");
        config.setSocksPort(123);
        config.setNoProxyHost("host,otherhost");

        gui.configure(config);

        assertThat(gui.autoDetectProxy.isSelected(), is(true));
        assertThat(gui.pacUrl.getText(), is(config.getProxyPacUrl()));
        assertThat(gui.httpProxyHost.getText(), is(config.getHttpHost()));
        assertThat(gui.httpProxyPort.getText(), is(String.valueOf(config.getHttpPort())));
        assertThat(gui.useHttpSettingsForAllProtocols.isSelected(), is(config.isUseHttpSettingsForAllProtocols()));
        assertThat(gui.httpsProxyHost.getText(), is(config.getHttpsHost()));
        assertThat(gui.httpsProxyPort.getText(), is(String.valueOf(config.getHttpsPort())));
        assertThat(gui.ftpProxyHost.getText(), is(config.getFtpHost()));
        assertThat(gui.ftpProxyPort.getText(), is(String.valueOf(config.getFtpPort())));
        assertThat(gui.socksProxyHost.getText(), is(config.getSocksHost()));
        assertThat(gui.socksProxyPort.getText(), is(String.valueOf(config.getSocksPort())));
        assertThat(gui.noProxyList.getText(), is(config.getNoProxyHost()));
    }

    @Test
    public void shouldSetExperimentalValuesOnConfigure() {
        WebDriverConfig config = new WebDriverConfigImpl();
        config.setRecreateBrowserOnIterationStart(true);

        gui.configure(config);

        assertThat(gui.recreateBrowserOnIterationStart.isSelected(), is(true));
    }

    @Test
    public void shouldSetNoProxyOnConfigure() {
        WebDriverConfig config = new WebDriverConfigImpl();
        config.setProxyType(ProxyType.DIRECT);
        gui.configure(config);

        assertThat(gui.directProxy.isSelected(), is(true));
    }

    @Test
    public void shouldSetAutoDetectProxyOnConfigure() {
        WebDriverConfig config = new WebDriverConfigImpl();
        config.setProxyType(ProxyType.AUTO_DETECT);
        gui.configure(config);

        assertThat(gui.autoDetectProxy.isSelected(), is(true));
    }

    @Test
    public void shouldSetManualProxyOnConfigure() {
        WebDriverConfig config = new WebDriverConfigImpl();
        config.setProxyType(ProxyType.MANUAL);
        gui.configure(config);

        assertThat(gui.manualProxy.isSelected(), is(true));
    }

    @Test
    public void shouldSetPacUrlProxyOnConfigure() {
        WebDriverConfig config = new WebDriverConfigImpl();
        config.setProxyType(ProxyType.PROXY_PAC);
        gui.configure(config);

        assertThat(gui.pacUrlProxy.isSelected(), is(true));
    }

    @Test
    public void shouldSetSystemProxyOnConfigure() {
        WebDriverConfig config = new WebDriverConfigImpl();
        config.setProxyType(ProxyType.SYSTEM);
        gui.configure(config);

        assertThat(gui.systemProxy.isSelected(), is(true));
    }

    @Test
    public void shouldImplementItemListener() {
        assertThat(gui, is(CoreMatchers.instanceOf(ItemListener.class)));
    }

    @Test
    public void shouldEnablePacUrlWhenPacUrlProxySelected() {
        gui.pacUrlProxy.setSelected(true);
        assertThat(gui.pacUrl.isEnabled(), is(true));
    }

    @Test
    public void shouldDisablePacUrlWhenPacUrlProxyNotSelected() {
        gui.pacUrlProxy.setSelected(false);
        assertThat(gui.pacUrl.isEnabled(), is(false));
    }

    @Test
    public void shouldDisablePacUrlByDefault() {
        assertThat(gui.pacUrl.isEnabled(), is(false));
    }

    @Test
    public void shouldEnableAllManualProxiesWhenManualProxySelectedAndUseHttpForAllProtocolsNotSelected() {
        gui.manualProxy.setSelected(true);
        gui.useHttpSettingsForAllProtocols.setSelected(false);

        assertThat(gui.httpProxyHost.isEnabled(), is(true));
        assertThat(gui.httpProxyPort.isEnabled(), is(true));
        assertThat(gui.useHttpSettingsForAllProtocols.isEnabled(), is(true));
        assertThat(gui.httpsProxyHost.isEnabled(), is(true));
        assertThat(gui.httpsProxyPort.isEnabled(), is(true));
        assertThat(gui.ftpProxyHost.isEnabled(), is(true));
        assertThat(gui.ftpProxyPort.isEnabled(), is(true));
        assertThat(gui.socksProxyHost.isEnabled(), is(true));
        assertThat(gui.socksProxyPort.isEnabled(), is(true));
        assertThat(gui.noProxyList.isEnabled(), is(true));
    }

    @Test
    public void shouldEnableHttpAndNoProxyListWhenManualProxySelectedAndUseHttpForAllProtocolsSelected() {
        gui.manualProxy.setSelected(true);
        gui.useHttpSettingsForAllProtocols.setSelected(true);

        assertThat(gui.httpProxyHost.isEnabled(), is(true));
        assertThat(gui.httpProxyPort.isEnabled(), is(true));
        assertThat(gui.useHttpSettingsForAllProtocols.isEnabled(), is(true));
        assertThat(gui.httpsProxyHost.isEnabled(), is(false));
        assertThat(gui.httpsProxyPort.isEnabled(), is(false));
        assertThat(gui.ftpProxyHost.isEnabled(), is(false));
        assertThat(gui.ftpProxyPort.isEnabled(), is(false));
        assertThat(gui.socksProxyHost.isEnabled(), is(false));
        assertThat(gui.socksProxyPort.isEnabled(), is(false));
        assertThat(gui.noProxyList.isEnabled(), is(true));
    }

    @Test
    public void shouldDisableManualProxiesWhenManualProxyNotSelectedAndUseHttpForAllProtocolsNotSelected() {
        gui.manualProxy.setSelected(false);
        gui.useHttpSettingsForAllProtocols.setSelected(false);

        assertThat(gui.httpProxyHost.isEnabled(), is(false));
        assertThat(gui.httpProxyPort.isEnabled(), is(false));
        assertThat(gui.useHttpSettingsForAllProtocols.isEnabled(), is(false));
        assertThat(gui.httpsProxyHost.isEnabled(), is(false));
        assertThat(gui.httpsProxyPort.isEnabled(), is(false));
        assertThat(gui.ftpProxyHost.isEnabled(), is(false));
        assertThat(gui.ftpProxyPort.isEnabled(), is(false));
        assertThat(gui.socksProxyHost.isEnabled(), is(false));
        assertThat(gui.socksProxyPort.isEnabled(), is(false));
        assertThat(gui.noProxyList.isEnabled(), is(false));
    }

    @Test
    public void shouldDisableManualProxiesWhenManualProxyNotSelectedAndUseHttpForAllProtocolsSelected() {
        gui.manualProxy.setSelected(false);
        gui.useHttpSettingsForAllProtocols.setSelected(true);

        assertThat(gui.httpProxyHost.isEnabled(), is(false));
        assertThat(gui.httpProxyPort.isEnabled(), is(false));
        assertThat(gui.useHttpSettingsForAllProtocols.isEnabled(), is(false));
        assertThat(gui.httpsProxyHost.isEnabled(), is(false));
        assertThat(gui.httpsProxyPort.isEnabled(), is(false));
        assertThat(gui.ftpProxyHost.isEnabled(), is(false));
        assertThat(gui.ftpProxyPort.isEnabled(), is(false));
        assertThat(gui.socksProxyHost.isEnabled(), is(false));
        assertThat(gui.socksProxyPort.isEnabled(), is(false));
        assertThat(gui.noProxyList.isEnabled(), is(false));
    }

    @Test
    public void shouldDisableManualProxiesWhenSwitchingToNonManualProxy() {
        gui.manualProxy.setSelected(true);
        gui.useHttpSettingsForAllProtocols.setSelected(false);
        gui.systemProxy.setSelected(true);

        assertThat(gui.httpProxyHost.isEnabled(), is(false));
        assertThat(gui.httpProxyPort.isEnabled(), is(false));
        assertThat(gui.useHttpSettingsForAllProtocols.isEnabled(), is(false));
        assertThat(gui.httpsProxyHost.isEnabled(), is(false));
        assertThat(gui.httpsProxyPort.isEnabled(), is(false));
        assertThat(gui.ftpProxyHost.isEnabled(), is(false));
        assertThat(gui.ftpProxyPort.isEnabled(), is(false));
        assertThat(gui.socksProxyHost.isEnabled(), is(false));
        assertThat(gui.socksProxyPort.isEnabled(), is(false));
        assertThat(gui.noProxyList.isEnabled(), is(false));
    }

    @Test
    public void shouldSetRecreateBrowser() {
        gui.recreateBrowserOnIterationStart.setSelected(true);
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.isRecreateBrowserOnIterationStart(), is(true));
    }

    @Test
    public void shouldSetDevMode() {
        gui.devMode.setSelected(true);
        final WebDriverConfig testElement = (WebDriverConfig) gui.createTestElement();
        assertThat(testElement.isDevMode(), is(true));
    }



    private static class WebDriverConfigGuiImpl extends WebDriverConfigGui {

        public WebDriverConfigGuiImpl() {
            setLayout(new BorderLayout(0, 5));
            add(createProxyPanel(), BorderLayout.CENTER);
        }

        @Override
        protected JPanel createBrowserPanel() {
            return new VerticalPanel();
        }

        @Override
        protected String browserName() {
            return "Mock Web Driver";
        }

        @Override
        protected String getWikiPage() {
            return "dummy";
        }

        @Override
        public String getLabelResource() {
            return null;
        }

        @Override
        public TestElement createTestElement() {
            final WebDriverConfigImpl config = new WebDriverConfigImpl();
            modifyTestElement(config);
            return config;
        }

		@Override
		protected boolean isProxyEnabled() {
			return true;
		}

		@Override
		protected boolean isExperimentalEnabled() {
			return true;
		}
    }

    private static class WebDriverConfigImpl extends WebDriverConfig {
        @Override
        protected WebDriver createBrowser() {
            return null;
        }
    }
}
