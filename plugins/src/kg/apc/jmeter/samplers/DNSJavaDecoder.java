package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.xbill.DNS.*;

/**
 *
 * @author undera
 */
public class DNSJavaDecoder implements UDPTrafficDecoder {

    public static final String NL = "\n";
    public static final String SPACE = " ";

    @Override
    public ByteBuffer encode(String data) {
        return ByteBuffer.wrap(getMessageBytes(data));
    }
    
    protected byte[] getMessageBytes(String data) {
        Message msg = new Message();
        String recs[] = data.split(NL);
        for (int n = 0; n < recs.length; n++) {
            if (recs[n].length() <= 3) {
                Header head = msg.getHeader();
                int val = Integer.parseInt(recs[n].trim());
                if (val < 0) {
                    head.unsetFlag(-val);
                } else {
                    head.setFlag(val);
                }
                msg.setHeader(head);
            } else {
                msg.addRecord(getRecord(recs[n].trim()), Section.QUESTION);
            }
        }
        return msg.toWire();
    }

    protected Record getRecord(String recstr) {
        String[] fields = recstr.split(SPACE);
        if (fields.length != 3) {
            throw new IllegalArgumentException("Wrong DNS query string: " + recstr);
        }
        return Record.newRecord(Name.fromConstantString(fields[0]), Type.value(fields[1]), DClass.value(fields[2]));
    }

    @Override
    public byte[] decode(byte[] buf) {
        Message m;
        try {
            m = new Message(buf);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot decode DNS message", ex);
        }
        return m.toString().getBytes();
    }
}
