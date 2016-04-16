package kg.apc.io;

public class BinaryUtils {

    public static byte[] shortToByteArray(short value) {
        byte[] result = new byte[2];
        result[1] = (byte) (value >>> 8);
        result[0] = (byte) value;
        return result;
    }


    public static long twoBytesToLongVal(byte byte1, byte byte2) {
        long short1 = (long) (byte1 & 0xFF); // God knows, how long I searched for this...
        long short2 = (long) (byte2 & 0xFF);

        return (short2 << 8) | short1;
    }

}
