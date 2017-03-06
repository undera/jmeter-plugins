package org.jmeterplugins.repository;

import org.junit.Test;

import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;

public class PluginManagerCMDTest {
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
        params.add("jpgc-json,jpgc-cmd=2.0");
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
        params.add("jpgc-json=2.0,jpgc-cmd");
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
        code = cmd.processParams(params.listIterator());
        assertEquals(-1, code);

        params.clear();
        code = cmd.processParams(params.listIterator());
        assertEquals(-1, code);
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