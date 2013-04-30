package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;

import org.apache.jorphan.util.JOrphanUtils;
import org.xbill.DNS.*;

import com.google.common.collect.Sets;

/**
 *
 * @author undera
 */
public class DNSJavaDecoder implements UDPTrafficDecoder {

    public static final String NL = "\n";
    public static final String SPACE = " ";
    private static final Set<String> opcodes = Sets.newTreeSet();
    static {
        opcodes.add(Opcode.string(Opcode.IQUERY));
        opcodes.add(Opcode.string(Opcode.NOTIFY));
        opcodes.add(Opcode.string(Opcode.QUERY));
        opcodes.add(Opcode.string(Opcode.STATUS));
        opcodes.add(Opcode.string(Opcode.UPDATE));
    }
    

    @Override
    public ByteBuffer encode(String data) {
        return ByteBuffer.wrap(getMessageBytes(data));
    }
    
    protected byte[] getMessageBytes(String data) {
        Message msg = new Message();
        String recs[] = data.split(NL);
        for (int n = 0; n < recs.length; n++) {
            // Set Opcode
            Header head = msg.getHeader();
            String value = recs[n].trim();
            if (opcodes.contains(value)) {
                head.setOpcode(Opcode.value(value));
                msg.setHeader(head);
            }
            else if (recs[n].length() <= 3) {
                
                int val = Integer.parseInt(value);
                if (val < 0) {
                    head.unsetFlag(-val);
                } else {
                    head.setFlag(val);
                }
                msg.setHeader(head);
            } else {
                msg.addRecord(getRecord(value), Section.QUESTION);
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
            throw new RuntimeException("Cannot decode DNS message: "+JOrphanUtils.baToHexString(buf), ex);
        }
        return m.toString().getBytes();
    }
}
