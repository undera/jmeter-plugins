package org.jmeterplugins.repository;

import junit.framework.AssertionFailedError;
import net.sf.json.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RepoTest {
    private final Set<String> cache = new HashSet<>();

    @Test
    public void testAll() throws IOException {
        System.err.println("Env: " + System.getenv());
        String travis = System.getenv("TRAVIS");
        if (travis != null) {
            System.out.println("Not running test inside Travis CI");
        }

        List<String> problems = new ArrayList<>();
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        String up = File.separator + "..";
        String repos = path + up + up + up + File.separator + "site" + File.separator + "dat" + File.separator + "repo";
        File dir = new File(repos);

        System.out.println("Working with " + dir.getCanonicalPath());
        File[] files = dir.listFiles();
        assert files != null;
        for (File f : files) {
            System.out.println("Checking repo: " + f.getCanonicalPath());
            String content = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())), "UTF-8");
            JSON json = JSONSerializer.toJSON(content, new JsonConfig());
            JSONArray list = (JSONArray) json;
            for (Object o : list) {
                JSONObject spec = (JSONObject) o;
                checkPlugin(problems, f, spec);
            }
        }

        if (problems.size() > 0) {
            throw new AssertionFailedError(problems.toString() + "\n" + System.getenv());
        }
    }

    private void checkPlugin(List<String> problems, File f, JSONObject spec) {
        Plugin plugin = Plugin.fromJSON(spec);
        try {
            System.out.println("Checking plugin: " + plugin);
            plugin.setCandidateVersion(plugin.getMaxVersion());
            plugin.download(dummy);
        } catch (Throwable e) {
            problems.add(f.getName() + ":" + plugin);
            System.err.println("Problem with " + plugin);
            e.printStackTrace(System.err);
        }

        Map<String, String> libs = plugin.getLibs(plugin.getCandidateVersion());
        for (String id : libs.keySet()) {
            if (!cache.contains(libs.get(id))) {
                try {
                    Downloader dwn = new Downloader(dummy);
                    dwn.download(id, new URI(libs.get(id)));
                    cache.add(libs.get(id));
                } catch (Throwable e) {
                    problems.add(f.getName() + ":" + plugin + ":" + id);
                    System.err.println("Problem with " + id);
                    e.printStackTrace(System.err);
                }
            }
        }

    }

    private GenericCallback<String> dummy = new GenericCallback<String>() {
        @Override
        public void notify(String s) {
        }
    };
}
