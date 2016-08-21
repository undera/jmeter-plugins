package kg.apc.jmeter.samplers;

import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jorphan.util.JOrphanUtils;

import java.nio.ByteBuffer;

public class HexStringUDPDecoder implements UDPTrafficDecoder {

    public ByteBuffer encode(String data) {
        return ByteBuffer.wrap(BinaryTCPClientImpl.hexStringToByteArray(data));
    }

    public byte[] decode(byte[] buf) {
        return JOrphanUtils.baToHexString(buf).getBytes();
    }
}
