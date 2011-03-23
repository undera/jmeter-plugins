package kg.apc.jmeter.samplers;

import java.nio.ByteBuffer;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jorphan.util.JOrphanUtils;

/**
 *
 * @author undera
 */
public class HexStringUDPDecoder implements UDPTrafficEncoder {

    public ByteBuffer encode(String data) {
        return ByteBuffer.wrap(BinaryTCPClientImpl.hexStringToByteArray(data));
    }

    public String decode(ByteBuffer buf) {
        ByteBuffer str = buf.duplicate();
        str.rewind();
        byte[] dst = new byte[str.limit()];
        str.get(dst);
        return JOrphanUtils.baToHexString(dst);
    }
}
