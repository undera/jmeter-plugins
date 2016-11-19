package org.jmeterplugins.repository;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.AssertionFailedError;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.io.FileUtils;

/**
 * @deprecated in favor of python-based builder
 */
public class RepoTest {
    private final Map<String, String> cache = new HashMap<>();
    private String s = File.separator;
    private File repo = new File(System.getProperty("project.build.directory", "target") + s + "jpgc-repo");
    private File lib = new File(repo.getAbsolutePath() + s + "lib");
    private File libExt = new File(lib.getAbsolutePath() + s + "ext");
    private JARSource jarSource = new JARSourceHTTP("https://jmeter-plugins.org/repo/");

    public RepoTest() {
        try {
            FileUtils.deleteDirectory(lib);
        } catch (IOException e) {
            e.printStackTrace();
        }
        libExt.mkdirs();
    }

    public void testAll() throws IOException {
        Map<String, String> env = System.getenv();
        if (env.containsKey("TRAVIS")) {
            System.out.println("Not running test inside Travis CI");
            return;
        }

        List<String> problems = new ArrayList<>();
        File[] files = getRepoFiles();

        JSONArray merged = new JSONArray();
        for (File repoFile : files) {
            System.out.println("Checking repo: " + repoFile.getCanonicalPath());
            String content = new String(Files.readAllBytes(Paths.get(repoFile.getAbsolutePath())), "UTF-8");
            JSON json = JSONSerializer.toJSON(content, new JsonConfig());
            JSONArray list = (JSONArray) json;
            for (Object item : list) {
                JSONObject spec = (JSONObject) item;
                checkPlugin(problems, repoFile, spec);
                merged.add(spec);
            }
        }

        if (problems.size() > 0) {
            throw new AssertionFailedError(problems.toString());
        }

        try (PrintWriter out = new PrintWriter(new File(repo.getAbsolutePath() + s + "all.json"));) {
            out.print(merged.toString(1));
        }
    }

    private File[] getRepoFiles() throws IOException {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        String up = File.separator + "..";
        String repos = path + up + up + up + File.separator + "site" + File.separator + "dat" + File.separator + "repo";
        File dir = new File(repos);

        System.out.println("Working with " + dir.getCanonicalPath());
        File[] files = dir.listFiles();
        assert files != null;
        return files;
    }

    private void checkPlugin(List<String> problems, File repoFile, JSONObject spec) {
        Plugin plugin = Plugin.fromJSON(spec);
        if (plugin.isVirtual()) {
            return;
        }

        String maxVersion = plugin.getMaxVersion();
        JSONObject maxVerObject = spec.getJSONObject("versions").getJSONObject(maxVersion);

        JSONObject newVersions = new JSONObject();
        newVersions.put(maxVersion, maxVerObject);

        try {
            System.out.println("Checking plugin: " + plugin);
            plugin.setCandidateVersion(maxVersion);
            plugin.download(jarSource, dummy);

            if (!plugin.isVersionFrozenToJMeter()) {
                File jar = new File(plugin.getTempName());
                File dest = new File(plugin.getDestName());
                File to = new File(libExt.getAbsolutePath() + File.separator + dest.getName());
                jar.renameTo(to);

                maxVerObject.put("downloadUrl", "lib/ext/" + dest.getName());
            }
        } catch (Throwable e) {
            problems.add(repoFile.getName() + ":" + plugin);
            System.err.println("Problem with " + plugin);
            e.printStackTrace(System.err);
        }

        checkLibs(problems, repoFile, plugin, maxVerObject);

        if (!maxVerObject.isEmpty()) {
            newVersions.put(maxVersion, maxVerObject);
            spec.put("versions", newVersions);
        }
    }

    private void checkLibs(List<String> problems, File repoFile, Plugin plugin, JSONObject maxVerObject) {
        Map<String, String> libs = plugin.getLibs(plugin.getCandidateVersion());
        for (String id : libs.keySet()) {
            if (!cache.containsKey(libs.get(id))) {
                try {
                    JARSource.DownloadResult dwn = jarSource.getJAR(id, libs.get(id), dummy);

                    File jar = new File(dwn.getTmpFile());
                    File dest = new File(lib.getAbsolutePath() + File.separator + dwn.getFilename());
                    jar.renameTo(dest);

                    cache.put(libs.get(id), dwn.getFilename());
                } catch (Throwable e) {
                    problems.add(repoFile.getName() + ":" + plugin + ":" + id);
                    System.err.println("Problem with " + id);
                    e.printStackTrace(System.err);
                }
            }

            maxVerObject.getJSONObject("libs").put(id, "lib/" + cache.get(libs.get(id)));
        }
    }

    private GenericCallback<String> dummy = new GenericCallback<String>() {
        @Override
        public void notify(String s) {
        }
    };
}
