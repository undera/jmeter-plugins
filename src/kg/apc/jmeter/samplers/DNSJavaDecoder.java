package kg.apc.jmeter.samplers;

import java.nio.ByteBuffer;
import org.xbill.DNS.*;

/**
 *
 * @author undera
 */
public class DNSJavaDecoder extends HexStringUDPDecoder {

    @Override
    public ByteBuffer encode(String data) {
        Message msg = new Message();
        String recs[] = data.split("\n");
        for (int n = 0; n < recs.length; n++) {
            msg.addRecord(getRecord(recs[n]), Section.QUESTION);
        }
        return ByteBuffer.wrap(msg.toWire());
    }

    private Record getRecord(String recstr) {
        String[] fields = recstr.split(" ");
        return Record.newRecord(Name.fromConstantString(fields[0]), Type.value(fields[1]), DClass.value(fields[2]));
    }

}
