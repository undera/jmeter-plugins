package org.jmeterplugins.repository;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class PluginMock extends Plugin {
    private Set<String> depends = new HashSet<>();
    private Map<String, String> libs = new HashMap<>();

    public PluginMock(String id, String iVer) {
        super(id);
        installedVersion = iVer;
        installedPath = iVer;
        versions = JSONObject.fromObject("{\"1.0\":null,\"0.1\":null,\"0.1.5\":null}", new JsonConfig());
        candidateVersion = getMaxVersion();
    }

    public PluginMock() {
        super("mock");
    }

    public PluginMock(String id) {
        super(id);
    }

    @Override
    public Set<String> getDepends() {
        return depends;
    }

    public void setDepends(Set<String> depends) {
        this.depends = depends;
    }

    @Override
    public Map<String, String> getLibs(String verStr) {
        return libs;
    }

    public void setLibs(Map<String, String> libs) {
        this.libs = libs;
    }

    public void setVersions(JSONObject a) {
        versions = a;
    }

    public void setMarkerClass(String markerClass) {
        this.markerClass = markerClass;
    }

    public void setInstallerClass(String installerClass) {
        this.installerClass = installerClass;
    }
}
