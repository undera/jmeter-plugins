package kg.apc.jmeter.samplers;

import java.nio.ByteBuffer;

/**
 *
 * @author undera
 */
public interface UDPTrafficEncoder {
    public abstract ByteBuffer encode(String data);
    public abstract byte[] decode(ByteBuffer data);
}
