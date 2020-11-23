/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.emulators;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

class FileLockEmul extends FileLock {
    private static final Logger log = LoggerFactory.getLogger(FileLockEmul.class);

    public FileLockEmul() {
        super((FileChannel) null, 0, 0, false);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void release() throws IOException {
        log.debug("Release lock");
    }

}
