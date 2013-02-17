// идея - добавить каскадный демаршалинг, чтобы можно было одновременно инты и стринги иметь
package kg.apc.jmeter.dcerpc;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.jorphan.util.JOrphanUtils;

/**
 *
 * @author apc
 */
public class DCERPCMarshalling
{
   // marshal
   private static final char MARSHAL_REGULAR = ' ';
   private static final char MARSHAL_LENGTH_PREFIXED = 'L';
   private static final char MARSHAL_NULL_TERMINATED = 'Z';
   private static final char MARSHAL_LENGTH_PREFIXED_NULL_TERMINATED = 'N';
   private static final char MARSHAL_DOUBLE_LENGTH_PREFIXED_NULL_TERMINATED = 'D';
   private static final char MARSHAL_QUARTER_LENGTH_PREFIXED = 'Q';
   private static final char MARSHAL_BPP_PREPARE_TRANS = 'P';
   private static final char MARSHAL_BPP_INVOKE_XML = 'X';
   private static final char MARSHAL_FIXED_LENGTH = 'F';
   private static final char MARSHAL_INTEGER = 'I';
   private static final char MARSHAL_DOUBLE = 'B';
   // unmarshal
   private static final char UNMARSHAL_INTEGERS = 'I';
   private static final char UNMARSHAL_STRING_PARTS = 'S';
   private static final char UNMARSHAL_SINGLE_XML = 'X';
   private static final char UNMARSHAL_NONE = 'N';
   private static final int DEFAULT_UNMARSHAL_STRING_LEN = 5;

   /**
    *
    * @param ba
    * @param unmarshalOptions
    * @return
    * @throws RPCMarshallingException
    */
   public static String unmarshalData(byte ba[], String unmarshalOptions)
        throws RPCMarshallingException
   {
      char unmarshalMode = UNMARSHAL_NONE;
      String[] options = unmarshalOptions.split(":");
      if (options[0].length() == 1)
      {
         unmarshalMode = options[0].charAt(0);
      }

      switch (unmarshalMode)
      {
         case UNMARSHAL_INTEGERS:
            int limit;
            if (options.length < 2)
            {
               limit = 0;
            }
            else
            {
               limit = Integer.decode(options[1]);
               if (limit < 1)
               {
                  throw new RPCMarshallingException("Invalid limit: " + unmarshalOptions);
               }
            }
            return unmarshalConvertIntegers(ba, limit);

         case UNMARSHAL_STRING_PARTS:
            int len;
            if (options.length < 2)
            {
               len = DEFAULT_UNMARSHAL_STRING_LEN;
            }
            else
            {
               len = Integer.decode(options[1]);
               if (len < 1)
               {
                  throw new RPCMarshallingException("Invalid length: " + unmarshalOptions);
               }
            }
            return unmarshalConvertStringParts(ba, len);

         case UNMARSHAL_SINGLE_XML:
            String convertedText = unmarshalConvertStringParts(ba, 1);
            // идея -  implement unmarshal xml
            return convertedText;

         default:
            return JOrphanUtils.baToHexString(ba);
      }
   }

   private static String unmarshalConvertStringParts(byte[] ba, int lengthThreshold)
   {
      StringBuffer result_buffer = new StringBuffer();
      StringBuffer binary_buffer = new StringBuffer();
      ByteArrayOutputStream text_buffer = new ByteArrayOutputStream();
      for (int i = 0; i < ba.length; i++)
      {
         int j = ba[i] & 0xff;
         if (isNotVisibleChar(j))
         {
            addTextOrBinaryBuffer(result_buffer, text_buffer, binary_buffer, lengthThreshold);
                result_buffer.append(j < 16 ? "0" : "").append(Integer.toHexString(j));
         }
         else
         {
            text_buffer.write(j);
                binary_buffer.append(j < 16 ? "0" : "").append(Integer.toHexString(j));
         }
      }
      addTextOrBinaryBuffer(result_buffer, text_buffer, binary_buffer, lengthThreshold);
      return result_buffer.toString();
   }

   private static void addTextOrBinaryBuffer(StringBuffer result_buffer, ByteArrayOutputStream text_buffer, StringBuffer binary_buffer, int lengthThreshold)
   {
      if (text_buffer.size() >= lengthThreshold)
      {
            result_buffer.append('{').append(text_buffer.toString()).append('}');
      }
      else
      {
         result_buffer.append(binary_buffer);
      }
      text_buffer.reset();
      binary_buffer.setLength(0);
   }

   private static boolean isNotVisibleChar(int j)
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

   /**
    *
    * @param in_str
    * @return
    * @throws RPCMarshallingException
    */
   public static String marshalData(String in_str)
        throws RPCMarshallingException
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

   private static String marshalAs(char marshalMode, String txt)
        throws RPCMarshallingException
   {
      switch (marshalMode)
      {
         case MARSHAL_LENGTH_PREFIXED:
            return BinaryUtils.intToHexString(txt.length()) + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_NULL_TERMINATED:
            txt += (char) 0;
            return JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_FIXED_LENGTH:
            return marshalAsFixedLength(txt);

         case MARSHAL_LENGTH_PREFIXED_NULL_TERMINATED:
            txt += (char) 0;
            return BinaryUtils.intToHexString(txt.length()) + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_DOUBLE_LENGTH_PREFIXED_NULL_TERMINATED:
            txt += (char) 0;
            return BinaryUtils.intToHexString(txt.length())
                 + BinaryUtils.intToHexString(0)
                 + BinaryUtils.intToHexString(txt.length())
                 + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_BPP_INVOKE_XML:
            return BinaryUtils.intToHexString(txt.length() + 1)
                 + BinaryUtils.intToHexString(txt.length())
                 + BinaryUtils.intToHexString(txt.length())
                 + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_QUARTER_LENGTH_PREFIXED:
            return BinaryUtils.intToHexString(txt.length())
                 + BinaryUtils.intToHexString(txt.length())
                 + BinaryUtils.intToHexString(txt.length())
                 + BinaryUtils.intToHexString(txt.length())
                 + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_BPP_PREPARE_TRANS:
            return BinaryUtils.intToHexString(txt.length())
                 + BinaryUtils.intToHexString(txt.length())
                 + BinaryUtils.intToHexString(200000)
                 + BinaryUtils.intToHexString(txt.length())
                 + JOrphanUtils.baToHexString(txt.getBytes());

         case MARSHAL_INTEGER:
            return BinaryUtils.intToHexString(Integer.valueOf(txt));

         case MARSHAL_DOUBLE:
            return BinaryUtils.doubleToHexString(Double.valueOf(txt));

         case MARSHAL_REGULAR:
            return JOrphanUtils.baToHexString(txt.getBytes());

         default:
            throw new RPCMarshallingException("Unknown marshal mode: " + marshalMode);
      }
   }

   private static String marshalAsFixedLength(String txt)
        throws RPCMarshallingException
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

   private static String unmarshalConvertIntegers(byte[] ba, int limit)
   {
      String integers = "";
      int count = 0;
      int pos = 0;
      while (pos < ba.length - ba.length % 4)
      {
         integers += Integer.toString(BinaryUtils.fourBytesToIntVal(ba[pos], ba[pos + 1], ba[pos + 2], ba[pos + 3])) + ",";
         pos += 4;

         if (limit > 0 && ++count >= limit)
         {
            break;
         }
      }

      byte[] tail = new byte[ba.length - pos];
      System.arraycopy(ba, pos, tail, 0, tail.length);
      System.err.println(tail);

      return (integers.length() > 0 ? "{" + integers + "}" : "") + JOrphanUtils.baToHexString(tail);
   }
}
