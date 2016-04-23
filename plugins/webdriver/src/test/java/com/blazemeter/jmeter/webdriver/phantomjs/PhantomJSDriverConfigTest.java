package com.blazemeter.jmeter.webdriver.phantomjs;

import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PhantomJSDriverConfig.class)
@PowerMockIgnore("javax.*")
public class PhantomJSDriverConfigTest {

    private PhantomJSDriverConfig config;

    @Before
    public void createConfig() throws IOException {
        config = new PhantomJSDriverConfig();
        File tmp = File.createTempFile("driver", ".cmd");
        tmp.setExecutable(true);
        tmp.deleteOnExit();
        config.setPhantomJsExecutablePath(tmp.getAbsolutePath());
        JMeterVariables variables = new JMeterVariables();
        JMeterContextService.getContext().setVariables(variables);
        try {
            config.threadStarted();
        } catch (UnreachableBrowserException ignored) {
            // TODO: mock it properly
        }
    }

    @After
    public void resetConfig() {
        config.threadFinished();
        JMeterContextService.getContext().setVariables(null);
    }

    @Test
    public void shouldBeAbleToSerialiseAndDeserialise() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bytes);

        output.writeObject(config);
        output.flush();
        output.close();

        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        final PhantomJSDriverConfig deserializedConfig = (PhantomJSDriverConfig) input.readObject();

        assertThat(deserializedConfig, is(config));
    }
}
