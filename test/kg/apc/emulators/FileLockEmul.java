/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.emulators;

import java.io.IOException;
import java.nio.channels.FileLock;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
class FileLockEmul extends FileLock {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public FileLockEmul() {
        super(null, 0, 0, false);
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
