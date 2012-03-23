/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import kg.apc.io.FileSystem;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testelement.TestListener;
import org.apache.log.Logger;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterStopTestNowException;

/**
 *
 * @author direvius
 */
public class LockFile extends ConfigTestElement
      implements TestListener{
    public static Logger log = LoggingManager.getLoggerForClass();
    public static final String FILENAME = "filename";
    public static final String FILEMASK = "filemask";
    
    public void testStarted() {
        testStarted(null);
    }

    public void testStarted(String string) {
        log.debug("[LockFile plugin] Test started captured");
        if(getFilename()!=null && getFilename().length()>0){
            log.info("[LockFile plugin] Checking lockfile at "+getFilename());
            File file = new File(getFilename());
            String path;
            try {
                if(file.getParentFile()!=null) path = file.getParentFile().getCanonicalPath();
                else path = new File(".").getCanonicalPath();
            } catch (IOException ex) {
                log.error("[LockFile plugin] Failed to get path");
                throw new JMeterStopTestNowException("[LockFile plugin] Failed to get path");
            }
            log.info("[LockFile plugin] and by wildcard at "+path+getFilemask());
            if(file.exists()){
                log.error("[LockFile plugin] Lock file found: "+getFilename());
                throw new JMeterStopTestNowException("[LockFile plugin] Lock file found: "+getFilename());
            }else if(getFilemask()!=null && getFilemask().length()>0 && 
                    FileSystem.checkFileExistByPattern(path, getFilemask())){
                log.error("[LockFile plugin] Lock file found by pattern "+getFilemask());
                throw new JMeterStopTestNowException("[LockFile plugin] Lock file found by pattern "+getFilemask());
            }else{
                try {
                    log.info("[LockFile plugin] Create lockfile at "+getFilename());
                    file.createNewFile();
                } catch (IOException e) {
                    log.error("[LockFile plugin] Could not create lock file: "+e.getLocalizedMessage());
                    throw new JMeterStopTestNowException("[LockFile plugin] Could not create lock file: "+e.getLocalizedMessage());
                }
            }
        }else{
            log.debug("[Lockfile plugin] Filename: "+getFilename());
            log.warn("[Lockfile plugin] No lockfile set. Ignore.");
        }
    }

    public void testEnded() {
        testEnded(null);
    }

    public void testEnded(String string) {
        log.debug("[LockFile plugin] Test ended captured");
        if(getFilename()!=null && getFilename().length()>0){
            File file = new File(getFilename());
            if(file.exists()){
                log.info("[LockFile plugin] Remove lockfile from "+getFilename());
                file.delete();
            }
        }
    }

    public void testIterationStart(LoopIterationEvent lie) {
        // DO NOTHING
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        log.debug("[LockFile plugin] Return filename: "+getPropertyAsString(FILENAME));
        return getPropertyAsString(FILENAME);
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        log.debug("[LockFile plugin] Set filename to: "+filename);
        setProperty(FILENAME, filename);
    }
    /**
     * @return the filemask
     */
    public String getFilemask() {
        log.debug("[LockFile plugin] Return filemask: "+getPropertyAsString(FILEMASK));
        return getPropertyAsString(FILEMASK);
    }

    /**
     * @param filename the filename to set
     */
    public void setFilemask(String filemask) {
        log.debug("[LockFile plugin] Set filemask to: "+filemask);
        setProperty(FILEMASK, filemask);
    }
    
}
