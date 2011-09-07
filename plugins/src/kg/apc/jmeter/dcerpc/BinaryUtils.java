package kg.apc.jmeter.dcerpc;

import org.apache.jorphan.util.JOrphanUtils;

/**
 *
 * @author apc
 */
public class BinaryUtils
{
   // took here http://snippets.dzone.com/posts/show/93
   /**
    *
    * @param value
    * @return
    */
   public static byte[] intToByteArray(int value)
   {
      byte[] result = new byte[4];
      result[3] = (byte) (value >>> 24);
      result[2] = (byte) (value >>> 16);
      result[1] = (byte) (value >>> 8);
      result[0] = (byte) value;
      return result;
   }

   /**
    *
    * @param value
    * @return
    */
   public static byte[] shortToByteArray(short value)
   {
      byte[] result = new byte[2];
      result[1] = (byte) (value >>> 8);
      result[0] = (byte) value;
      return result;
   }

   /**
    *
    * @param byte1
    * @param byte2
    * @return
    */
   public static int twoHexCharsToIntVal(char byte1, char byte2)
   {
      int nibble0 = Character.digit(byte1, 16);
      int nibble1 = Character.digit(byte2, 16);
      if (nibble0 == -1 || nibble1 == -1)
      {
         throw new IllegalArgumentException("Hex-encoded binary string contains an invalid hex digit in '" + byte1 + byte2 + "'");
      }

      return (int) ((nibble0 << 4) | (nibble1));
   }

   /**
    *
    * @param c
    * @param c0
    * @return
    */
   public static byte twoHexCharsToByteVal(char c, char c0)
   {
      return (byte) twoHexCharsToIntVal(c, c0);
   }

   /**
    *
    * @param ainterfaceUUID
    * @return
    */
   public static byte[] UUIDToByteArray(String ainterfaceUUID)
   {
      byte[] result = new byte[16];
      // allow the "-" in UUID
      char[] chars = ainterfaceUUID.replace("-", "").toCharArray();

      result[0] = twoHexCharsToByteVal(chars[6], chars[7]);
      result[1] = twoHexCharsToByteVal(chars[4], chars[5]);
      result[2] = twoHexCharsToByteVal(chars[2], chars[3]);
      result[3] = twoHexCharsToByteVal(chars[0], chars[1]);

      result[4] = twoHexCharsToByteVal(chars[10], chars[11]);
      result[5] = twoHexCharsToByteVal(chars[8], chars[9]);

      result[6] = twoHexCharsToByteVal(chars[14], chars[15]);
      result[7] = twoHexCharsToByteVal(chars[12], chars[13]);

      result[8] = twoHexCharsToByteVal(chars[16], chars[17]);
      result[9] = twoHexCharsToByteVal(chars[18], chars[19]);

      result[10] = twoHexCharsToByteVal(chars[20], chars[21]);
      result[11] = twoHexCharsToByteVal(chars[22], chars[23]);
      result[12] = twoHexCharsToByteVal(chars[24], chars[25]);
      result[13] = twoHexCharsToByteVal(chars[26], chars[27]);
      result[14] = twoHexCharsToByteVal(chars[28], chars[29]);
      result[15] = twoHexCharsToByteVal(chars[30], chars[31]);

      return result;
   }

   /**
    *
    * @param byte1
    * @param byte2
    * @return
    */
   public static short twoBytesToShortVal(byte byte1, byte byte2)
   {
      short short1 = (short) (byte1 & 0xFF); // God knows, how long I searched for this...
      short short2 = (short) (byte2 & 0xFF);

      return (short) ((short2 << 8) | short1);
   }

   /**
    *
    * @param i
    * @return
    */
   public static String intToHexString(int i)
   {
      return JOrphanUtils.baToHexString(intToByteArray(i));
   }

   /**
    *
    * @param byte1
    * @param byte2
    * @param byte3
    * @param byte4
    * @return
    */
   public static int fourBytesToIntVal(byte byte1, byte byte2, byte byte3, byte byte4)
   {
      int i = 0;
      i += (byte4 & 0xFF) << 24;
      i += (byte3 & 0xFF) << 16;
      i += (byte2 & 0xFF) << 8;
      i += (byte1 & 0xFF) << 0;
      return i;
   }

   /**
    *
    * @param val
    * @return
    */
   public static String doubleToHexString(double val)
   {
      long reversed = Long.reverseBytes(Double.doubleToLongBits(val));
      String res = Long.toHexString(reversed);
      res = "0000000000000000".substring(res.length()) + res;
      return res;
   }

   /**
    *
    * @param val
    * @return
    */
   public static double hexToDouble(String val)
   {
      long reversed = Long.reverseBytes(Long.decode("0x" + val));
      return Double.longBitsToDouble(reversed);
   }
}
