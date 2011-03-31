package kg.apc.jmeter.samplers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.apache.jorphan.util.JOrphanUtils;
import org.xbill.DNS.Message;
import org.xbill.DNS.Section;

/**
 *
 * @author undera
 */
@Deprecated
public class DNSJavaDecoderToRawData extends DNSJavaDecoder {

    protected OutputStream os = null;

    @Override
    public ByteBuffer encode(String data) {
        if (os == null) {
            try {
                os = new FileOutputStream(new File("DNSJavaDecoderToRawData.out"));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        Message msg = new Message();
        String recs[] = data.split(NL);
        for (int n = 0; n < recs.length; n++) {
            msg.addRecord(getRecord(recs[n]), Section.QUESTION);
        }

        try {
            final byte[] ba1 = msg.toWire();
            os.write(Integer.toString(ba1.length * 2).getBytes("cp866"));
            os.write('\n');
            os.write(JOrphanUtils.baToHexBytes(ba1));
            os.write('\n');
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return ByteBuffer.wrap(msg.toWire());
    }
}
