package kg.apc.emulators;

/**
 *
 * @author undera
 */
public class BinaryTools {

    public static byte[] hexStringToByteArray(String hexEncodedBinary) {
        if (hexEncodedBinary.length() % 2 == 0) {
            char[] sc = hexEncodedBinary.toCharArray();
            byte[] ba = new byte[sc.length / 2];

            for (int i = 0; i < ba.length; i++) {
                int nibble0 = Character.digit(sc[i * 2], 16);
                int nibble1 = Character.digit(sc[i * 2 + 1], 16);
                if (nibble0 == -1 || nibble1 == -1) {
                    throw new IllegalArgumentException(
                            "Hex-encoded binary string contains an invalid hex digit in '" + sc[i * 2] + sc[i * 2 + 1] + "'");
                }
                ba[i] = (byte) ((nibble0 << 4) | (nibble1));
            }

            return ba;
        } else {
            throw new IllegalArgumentException(
                    "Hex-encoded binary string contains an uneven no. of digits");
        }
    }
}
