package kg.apc.jmeter.dcerpc;

// this class holds common knowlege for rpc requests
public abstract class RPCRequest
{
   /* packet types */
   private static byte COMMONHEADERLENGTH=16;
   public static byte PACKET_TYPE_REQUEST = 0;
   public static byte PACKET_TYPE_BIND = 11;
   /* common packet fields */
   protected static byte version = 5;
   protected static byte versionminor = 0;
   protected byte packetType;
   protected static byte packetFlags = 0x03;
   protected static byte byteOrder = 16;
   protected static byte character = 0;
   protected static byte floatingPoint = 0;
   protected short fragLength;
   protected static short authLength = 0;
   protected int callID=1;

   protected final byte[] getCommonHeaderBytes()
   {
      byte[] header =
      {
         version,
         versionminor,
         packetType,
         packetFlags,
         byteOrder, character, floatingPoint, 0,
         0, 0,
         0, 0,
         0, 0, 0, 0
      };

      int curPos = 8;
      System.arraycopy(shortToByteArray(fragLength), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(shortToByteArray(authLength), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(intToByteArray(callID), 0, header, curPos, 4);
      curPos += 4;

      return header;
   }

   public final byte[] getBytes()
   {
      byte[] body = getBodyBytes();

      byte[] header = getHeaderBytes();

      fragLength=(short) (COMMONHEADERLENGTH + header.length + body.length);

      byte[] commonheader=getCommonHeaderBytes();

      byte[] result = new byte[fragLength];
      System.arraycopy(commonheader, 0, result, 0, commonheader.length);
      System.arraycopy(header, 0, result, commonheader.length, header.length);
      System.arraycopy(body, 0, result, commonheader.length + header.length, body.length);

      return result;
   }

   abstract protected byte[] getHeaderBytes();

   abstract protected byte[] getBodyBytes();

   // took here http://snippets.dzone.com/posts/show/93
   public final byte[] intToByteArray(int value)
   {
      byte[] result = new byte[4];
      result[3]=(byte) (value >>> 24);
      result[2]=(byte) (value >>> 16);
      result[1]=(byte) (value >>> 8);
      result[0]=(byte) value;
      return result;
   }

   public final byte[] shortToByteArray(short value)
   {
      byte[] result = new byte[2];
      result[1]=(byte) (value >>> 8);
      result[0]=(byte) value;
      return result;
   }

   protected final byte[] UUIDToByteArray(String ainterfaceUUID)
   {
      byte[] result = new byte[16];
      // allow the "-" in UUID
      char[] chars = ainterfaceUUID.replace("-", "").toCharArray();
      int i;

      i=Integer.decode("0x" + chars[6]+chars[7]);
      result[0]=(byte) i;
      i=Integer.decode("0x" + chars[4]+chars[5]);
      result[1]=(byte) i;
      i=Integer.decode("0x" + chars[2]+chars[3]);
      result[2]=(byte) i;
      i=Integer.decode("0x" + chars[0]+chars[1]);
      result[3]=(byte) i;

      i=Integer.decode("0x" + chars[10]+chars[11]);
      result[4]=(byte) i;
      i=Integer.decode("0x" + chars[8]+chars[9]);
      result[5]=(byte) i;

      i=Integer.decode("0x" + chars[14]+chars[15]);
      result[6]=(byte) i;
      i=Integer.decode("0x" + chars[12]+chars[13]);
      result[7]=(byte) i;

      i=Integer.decode("0x" + chars[16]+chars[17]);
      result[8]=(byte) i;
      i=Integer.decode("0x" + chars[18]+chars[19]);
      result[9]=(byte) i;

      i=Integer.decode("0x" + chars[20]+chars[21]);
      result[10]=(byte) i;
      i=Integer.decode("0x" + chars[22]+chars[23]);
      result[11]=(byte) i;
      i=Integer.decode("0x" + chars[24]+chars[25]);
      result[12]=(byte) i;
      i=Integer.decode("0x" + chars[26]+chars[27]);
      result[13]=(byte) i;
      i=Integer.decode("0x" + chars[28]+chars[29]);
      result[14]=(byte) i;
      i=Integer.decode("0x" + chars[30]+chars[31]);
      result[15]=(byte) i;

      return result;
   }
}
