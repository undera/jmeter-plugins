package kg.apc.jmeter.dcerpc;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.jorphan.util.JOrphanUtils;

public abstract class BinaryUtils
{
   private static final char MARSHAL_REGULAR = ' ';
   private static final char MARSHAL_LENGTH_PREFIXED = 'L';
   private static final char MARSHAL_NULL_TERMINATED = 'Z';
   private static final char MARSHAL_LENGTH_PREFIXED_NULL_TERMINATED = 'N';
   private static final char MARSHAL_DOUBLE_LENGTH_PREFIXED_NULL_TERMINATED = 'D';
   private static final char MARSHAL_QUARTER_LENGTH_PREFIXED = 'Q';
   private static final char MARSHAL_BPP_INVOKE_XML = 'X';
   private static final char MARSHAL_FIXED_LENGTH = 'F';
   private static final char MARSHAL_INTEGER = 'I';
   private static final int TEXT_VISUALIZE_LENGTH_THRESHOLD = 5;

   // TODO: change to streams usage
   public static String baToHexStringWithText(byte ba[])
   {
      StringBuffer result_buffer = new StringBuffer();
      StringBuffer binary_buffer = new StringBuffer();
      ByteArrayOutputStream text_buffer = new ByteArrayOutputStream();

      for (int i = 0; i < ba.length; i++)
      {
         int j = ba[i] & 0xff;

         if (isNotVisibleChar(j))
         {
            addTextOrBinaryBuffer(result_buffer, text_buffer, binary_buffer);
            result_buffer.append((j < 16 ? "0" : "") + Integer.toHexString(j));
         }
         else
         {
            text_buffer.write(j);
            binary_buffer.append((j < 16 ? "0" : "") + Integer.toHexString(j));
         }
      }

      addTextOrBinaryBuffer(result_buffer, text_buffer, binary_buffer);

      return result_buffer.toString();
   }

   private static void addTextOrBinaryBuffer(StringBuffer result_buffer, ByteArrayOutputStream text_buffer, StringBuffer binary_buffer)
   {
      if (text_buffer.size() >= TEXT_VISUALIZE_LENGTH_THRESHOLD)
      {
         //result_buffer.append('{' + text_buffer.toString() + '}');

         try
         {
            String cp1251str = new String(text_buffer.toByteArray(), "Cp1251");
            String utf8str=new String(cp1251str.getBytes("UTF-8"), "UTF-8");
            result_buffer.append('{' + utf8str + '}');
         }
         catch (UnsupportedEncodingException ex)
         {
            Logger.getLogger(BinaryUtils.class.getName()).log(Level.SEVERE, null, ex);
            result_buffer.append('{' + text_buffer.toString() + '}');
         }
      }
      else
      {
         result_buffer.append(binary_buffer);
      }
      text_buffer.reset();
      binary_buffer.setLength(0);
   }

   public static boolean isNotVisibleChar(int j)
   {
      if (j == 9 || j == 10 || j == 13 || j > 191)
      {
         return false;
      }
      else
      {
         return (j < 32 || j > 126 || j == 123 || j == 125);
      }
   }

   public static String hexEncodeTextParts(String in_str) throws RPCMarshallingException
   {
      Pattern patt = Pattern.compile("\\{([^\\}]*)\\}");
      Matcher m = patt.matcher(in_str);

      String replaceStr = null;
      while (m.find())
      {
         char marshalMode = MARSHAL_REGULAR;
         String txt = m.group(1);
         int pos = txt.indexOf(':');
         if (pos == 1)
         {
            marshalMode = txt.charAt(0);
            txt = txt.substring(2);
         }

         try
         {
            replaceStr = marshalAs(marshalMode, txt);
         }
         catch (RPCMarshallingException e)
         {
            replaceStr = marshalAs(MARSHAL_REGULAR, m.group(1));
         }

         in_str = in_str.replace("{" + m.group(1) + "}", replaceStr);
      }
      return in_str;
   }

   private static String marshalAs(char marshalMode, String txt) throws RPCMarshallingException
   {
      switch (marshalMode)
      {
         case MARSHAL_LENGTH_PREFIXED:
            return intToHexString(txt.length()) + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_NULL_TERMINATED:
            txt += (char) 0;
            return JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_FIXED_LENGTH:
            return marshalAsFixedLength(txt);

         case MARSHAL_LENGTH_PREFIXED_NULL_TERMINATED:
            txt += (char) 0;
            return intToHexString(txt.length()) + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_DOUBLE_LENGTH_PREFIXED_NULL_TERMINATED:
            txt += (char) 0;
            return intToHexString(txt.length())
                  + intToHexString(0)
                  + intToHexString(txt.length())
                  + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_BPP_INVOKE_XML:
            return intToHexString(txt.length() + 1)
                  + intToHexString(txt.length())
                  + intToHexString(txt.length())
                  + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_QUARTER_LENGTH_PREFIXED:
            return intToHexString(txt.length())
                  + intToHexString(txt.length())
                  + intToHexString(txt.length())
                  + intToHexString(txt.length())
                  + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_INTEGER:
            int value = Integer.valueOf(txt);
            return BinaryUtils.intToHexString(value);

         case MARSHAL_REGULAR:
            return JOrphanUtils.baToHexString(txt.getBytes());

         default:
            throw new RPCMarshallingException("Unknown marshal mode: " + marshalMode);
      }
   }

   // took here http://snippets.dzone.com/posts/show/93
   public static byte[] intToByteArray(int value)
   {
      byte[] result = new byte[4];
      result[3] = (byte) (value >>> 24);
      result[2] = (byte) (value >>> 16);
      result[1] = (byte) (value >>> 8);
      result[0] = (byte) value;
      return result;
   }

   public static byte[] shortToByteArray(short value)
   {
      byte[] result = new byte[2];
      result[1] = (byte) (value >>> 8);
      result[0] = (byte) value;
      return result;
   }

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

   public static byte twoHexCharsToByteVal(char c, char c0)
   {
      return (byte) twoHexCharsToIntVal(c, c0);
   }

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

   public static short twoBytesToShortVal(byte byte1, byte byte2)
   {
      short short1 = (short) (byte1 & 0xFF); // God knows, how long I searched for this...
      short short2 = (short) (byte2 & 0xFF);

      return (short) ((short2 << 8) | short1);
   }

   public static String intToHexString(int i)
   {
      return JOrphanUtils.baToHexString(intToByteArray(i));
   }

   private static String marshalAsFixedLength(String txt) throws RPCMarshallingException
   {
      int pos = txt.indexOf(':');
      if (pos < 1)
      {
         throw new RPCMarshallingException("Fixed length mode requires length specified as '12:data'");
      }

      int len = Integer.decode(txt.substring(0, pos));
      if (len < 1)
      {
         throw new RPCMarshallingException("Invalid fixed length: " + txt);
      }

      txt = txt.substring(pos + 1);
      for (int n = txt.length(); n < len; n++)
      {
         txt += (char) 0;
      }
      return JOrphanUtils.baToHexString(txt.getBytes());
   }
}
