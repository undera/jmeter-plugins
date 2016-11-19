package org.jmeterplugins.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PluginManagerCMDInstaller {
    public static void main(String[] argv) throws IOException {
        writeOut("/org/jmeterplugins/repository/PluginsManagerCMD.bat", false);
        writeOut("/org/jmeterplugins/repository/PluginsManagerCMD.sh", true);
    }

    private static void writeOut(String resName, boolean executable) throws IOException {
        String path = PluginManagerCMDInstaller.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        File self = new File(URLDecoder.decode(path, "UTF-8"));
        File src = new File(resName);
        String home = self.getParentFile().getParentFile().getParent();
        File dest = new File(home + File.separator + "bin" + File.separator + src.getName());

        try (InputStream is = PluginManagerCMDInstaller.class.getResourceAsStream(resName);) {
            Files.copy(is, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            dest.setExecutable(executable);
        }
    }
}
