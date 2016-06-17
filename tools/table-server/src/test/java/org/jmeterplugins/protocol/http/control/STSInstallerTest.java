package org.jmeterplugins.protocol.http.control;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

public class STSInstallerTest {
    @Test
    public void name() throws Exception {
        File self = new File(STSInstaller.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        String home = self.getParentFile().getParentFile().getParent();
        File dest = new File(home + File.separator + "bin");
        dest.mkdirs();

        STSInstaller.main(new String[0]);

        FileUtils.deleteDirectory(dest);
    }
}