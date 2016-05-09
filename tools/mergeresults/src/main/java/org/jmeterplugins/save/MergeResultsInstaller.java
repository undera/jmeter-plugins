package org.jmeterplugins.save;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MergeResultsInstaller {
    public static void main(String[] argv) throws IOException {
        writeOut("/org/jmeterplugins/save/merge-results.properties", false);
    }

    private static void writeOut(String resName, boolean executable) throws IOException {
        File self = new File(MergeResultsInstaller.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        File src = new File(resName);
        String home = self.getParentFile().getParentFile().getParent();
        File dest = new File(home + File.separator + "bin" + File.separator + src.getName());

        InputStream is = MergeResultsInstaller.class.getResourceAsStream(resName);
        Files.copy(is, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        dest.setExecutable(executable);
    }
}
