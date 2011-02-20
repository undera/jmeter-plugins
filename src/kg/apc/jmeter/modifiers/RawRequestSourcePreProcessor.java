package kg.apc.jmeter.modifiers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
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
    public static final String REWIND="rewind";
    private FileChannel file;
    private final ByteBuffer metaBuf = ByteBuffer.allocateDirect(1024);
    private final ByteBuffer oneByte = ByteBuffer.allocateDirect(1);

    public RawRequestSourcePreProcessor() {
        super();
    }

    public synchronized void process(){
        if (file == null) {
            log.info("Creating file object: " + getFileName());
            try {
                file = new FileInputStream(getFileName()).getChannel();
            } catch (FileNotFoundException ex) {
                log.error(getFileName(), ex);
                return;
            }
        }
        String rawData="";

        try {
            rawData=readNextChunk();
        } catch (IOException ex) {
            log.error("Error reading next chunk", ex);
        }
        catch (EndOfFileException ex)
        {
            log.info("End of file reached: "+getFileName());
        }       
            final JMeterVariables vars = JMeterContextService.getContext().getVariables();
        vars.put(getVarName(), rawData);
}

    private synchronized String readNextChunk()
            throws IOException, EndOfFileException
    {
        int capacity = getNextChunkSize();
        if (capacity==0)
        {
            if (getRewindOnEOF())
            {
                log.info("Rewind file");
                file.position(0);
                return readNextChunk();
            }
            else
            {
                throw new EndOfFileException("Zero chunk size, possibly end of file reached.");
            }
        }

        ByteBuffer buf = ByteBuffer.allocateDirect(capacity);
        byte[] dst = new byte[capacity];


        int cnt=file.read(buf);
        //log.debug("Read " + cnt);
        if (cnt!=capacity)
        {
            throw new IOException("Expected chunk size ("+capacity+") differs from read bytes count ("+cnt+")");
        }

        buf.flip();
        buf.get(dst);
        return new String(dst);
    }

    private int getNextChunkSize() throws IOException {
        // FIXME: obviously inefficient
        metaBuf.rewind();
        int count=0;
        while (true) {
            oneByte.rewind();
            if (file.read(oneByte)<1)
            {
                return 0;
            }
            oneByte.rewind();
            byte b = oneByte.get();
            metaBuf.put(b);
            //log.debug("B " + b+" "+metaBuf.remaining());
            count++;
            if (b == 13 || b==10) {
                break;
            }
        }
        metaBuf.rewind();

        byte[] bLine=new byte[count];
        metaBuf.get(bLine);
        String sLine=new String(bLine);
        String[] ar = sLine.split("\\s");
        //log.debug("Chunk size: "+ar[0]);
        
        int res=0;
        try {
            res=Integer.parseInt(ar[0]);
        }
        catch (NumberFormatException ex)
        {
            log.error("Error reading chunk size near: "+sLine, ex);
        }
        return res;
    }

    public String getVarName() {
        return getPropertyAsString(VARIABLE_NAME);
    }

    public void setVarName(String name) {
        setProperty(VARIABLE_NAME, name);
    }

    public String getFileName() {
        return getPropertyAsString(FILENAME);
    }

    public void setFileName(String filename) {
        setProperty(FILENAME, filename);
        file = null;
    }

    public void setRewindOnEOF(boolean isRew)
    {
        setProperty(new BooleanProperty(REWIND, isRew));
    }

    public boolean getRewindOnEOF()
    {
        return getPropertyAsBoolean(REWIND);
    }
}
