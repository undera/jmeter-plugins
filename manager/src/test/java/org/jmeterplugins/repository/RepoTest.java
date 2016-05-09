package org.jmeterplugins.repository;

import junit.framework.AssertionFailedError;
import net.sf.json.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepoTest {
    @Test
    public void testAll() throws IOException {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        String up = File.separator + "..";
        String repos = path + up + up + up + File.separator + "site" + File.separator + "dat" + File.separator + "repo";
        File dir = new File(repos);
        File[] files = dir.listFiles();
        List<String> problems = new ArrayList<>();
        for (File f : files) {
            System.out.println("Checking repo: " + f.getCanonicalPath());
            String content = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())), "UTF-8");
            JSON json = JSONSerializer.toJSON(content, new JsonConfig());
            JSONArray list = (JSONArray) json;
            for (Object o : list) {
                JSONObject spec = (JSONObject) o;
                Plugin plugin = Plugin.fromJSON(spec);
                try {
                    System.out.println("Checking plugin: " + plugin);
                    plugin.setCandidateVersion(plugin.getMaxVersion());

                    plugin.download(new GenericCallback<String>() {
                        @Override
                        public void notify(String s) {
                        }
                    });
                } catch (Throwable e) {
                    problems.add(f.getName() + ":" + plugin);
                    System.err.println("Problem with " + plugin);
                    e.printStackTrace(System.err);
                }
            }
        }
        if (problems.size() > 0) {
            throw new AssertionFailedError(problems.toString());
        }
    }

}
