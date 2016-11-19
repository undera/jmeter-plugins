package org.jmeterplugins.repository;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class ChangesMakerTest {
    @Test
    public void getProcessBuilder() throws Exception {
        Assert.assertEquals("a b", URLDecoder.decode("a%20b", "UTF-8"));
    }

    @Test
    public void getRestartFile() throws Exception {

    }

    @Test
    public void getInstallFile() throws Exception {
        Map<Plugin, Boolean> map = new HashMap<>();
        ChangesMaker obj = new ChangesMaker(map);
        Set<Plugin> plugins = new HashSet<>();
        PluginMock p = new PluginMock();
        p.setInstallerClass("test");
        plugins.add(p);
        File res = obj.getInstallFile(plugins);
        assertTrue(res.length() > 0);
    }

    @Test
    public void getMovementsFile() throws Exception {
        // NOOP
    }

}