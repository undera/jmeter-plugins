package org.jmeterplugins.repository;

import org.junit.Test;

import java.util.LinkedList;

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
        cmd.showHelp(System.out);

        try {
            cmd.processParams(new LinkedList<>().listIterator());
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

}