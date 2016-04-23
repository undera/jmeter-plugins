package kg.apc.jmeter.samplers;

import java.nio.ByteBuffer;

public interface UDPTrafficDecoder {

    public abstract ByteBuffer encode(String data);

    public abstract byte[] decode(byte[] data);
}
