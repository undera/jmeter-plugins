package org.jmeterplugins.protocol.http.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class STSInstaller {
    public static void main(String[] argv) throws IOException {
        writeOut("simple-table-server.bsh", false);
        writeOut("simple-table-server.cmd", false);
        writeOut("simple-table-server.sh", true);
        writeOut("NanoHttpd-2.1.0_License.txt", false);
    }

    private static void writeOut(String resName, boolean executable) throws IOException {
        resName = "/org/jmeterplugins/protocol/http/control/" + resName;
        File self = new File(STSInstaller.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        File src = new File(resName);
        String home = self.getParentFile().getParentFile().getParent();
        File dest = new File(home + File.separator + "bin" + File.separator + src.getName());

        InputStream is = STSInstaller.class.getResourceAsStream(resName);
        Files.copy(is, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        dest.setExecutable(executable);
    }
}
