package org.jmeterplugins.repository;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;

public class PluginManagerCMDTest {
    @BeforeClass
    public static void setup() {
        TestJMeterUtils.createJmeterEnv();
        URL url = PluginManagerTest.class.getResource("/testVirtualPlugin.json");
        JMeterUtils.setProperty("jpgc.repo.address", url.getFile());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        JMeterUtils.getJMeterProperties().remove("jpgc.repo.address");
    }

    @Test
    public void processParams_status() throws Exception {
        PluginManagerCMD cmd = new PluginManagerCMD();
        LinkedList<String> params = new LinkedList<>();
        params.add("status");
        cmd.processParams(params.listIterator());
    }

    @Test
    public void processParams_install() throws Exception {
        PluginManagerCMD cmd = new PluginManagerCMD();
        LinkedList<String> params = new LinkedList<>();
        params.add("install");
        try {
            cmd.processParams(params.listIterator());
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        params.add("jpgc-dep1,jpgc-dep2=2.0");
        cmd.processParams(params.listIterator());
    }

    @Test
    public void processParams_install_invalid() throws Exception {
        PluginManagerCMD cmd = new PluginManagerCMD();
        LinkedList<String> params = new LinkedList<>();
        params.add("install");
        params.add("jpgc-invalid");
        try {
            cmd.processParams(params.listIterator());
            fail();
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void processParams_uninstall() throws Exception {
        PluginManagerCMD cmd = new PluginManagerCMD();
        LinkedList<String> params = new LinkedList<>();
        params.add("uninstall");
        try {
            cmd.processParams(params.listIterator());
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        params.add("jpgc-dep1,jpgc-dep2=2.0");
        cmd.processParams(params.listIterator());
    }


    @Test
    public void showHelp() throws Exception {
        PluginManagerCMD cmd = new PluginManagerCMD();

        LinkedList<String> params = new LinkedList<>();
        params.add("help");
        int code = cmd.processParams(params.listIterator());
        assertEquals(0, code);

        params.clear();
        params.add("help me pls");
        try {
            cmd.processParams(params.listIterator());
        } catch (Throwable ex) {
            //
        }

        params.clear();
        try {
            cmd.processParams(params.listIterator());
        } catch (IllegalArgumentException ex) {
            //
        }
    }

    @Test
    public void testAvailable() throws Exception {
        PluginManagerCMD cmd = new PluginManagerCMD();
        LinkedList<String> params = new LinkedList<>();
        params.add("available");
        int code = cmd.processParams(params.listIterator());
        assertEquals(0, code);
    }

    @Test
    public void testUpgrades() throws Exception {
        PluginManagerCMD cmd = new PluginManagerCMD();
        LinkedList<String> params = new LinkedList<>();
        params.add("upgrades");
        int code = cmd.processParams(params.listIterator());
        assertEquals(0, code);
    }
}