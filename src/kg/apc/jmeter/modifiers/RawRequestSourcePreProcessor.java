package kg.apc.jmeter.modifiers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class RawRequestSourcePreProcessor
        extends AbstractTestElement
        implements PreProcessor, NoThreadClone {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String VARIABLE_NAME = "variable_name";
    public static final String FILENAME = "filename";
    private FileChannel file;

    public RawRequestSourcePreProcessor() {
        super();
    }

    public synchronized void process() {
        if (file == null) {
            log.info("Creating file object: "+getFileName());
            try {
                file = new FileInputStream(getFileName()).getChannel();
            } catch (FileNotFoundException ex) {
                log.error(getFileName(), ex);
                return;
            }
        }

        readNextChunk();
    }

    private synchronized void readNextChunk() {
            int capacity = 0;
            ByteBuffer buf = ByteBuffer.allocateDirect(capacity);
            try {
                file.read(buf);
            } catch (IOException ex) {
                log.error("Error reading next chunk, length: "+capacity);
            }
            buf.flip();
            String rawData = buf.toString();
            final JMeterVariables vars = JMeterContextService.getContext().getVariables();
            vars.put(getVarName(), rawData);
    }

    public String getVarName() {
        return getPropertyAsString(VARIABLE_NAME);
    }

    public void setVarName(String name)
    {
        setProperty(VARIABLE_NAME, name);
    }

    public String getFileName() {
        return "/tmp/0-bs.ammo";
        //return getPropertyAsString(FILENAME);
    }

    public void setFileName(String filename)
    {
        setProperty(FILENAME, filename);
    }
}
