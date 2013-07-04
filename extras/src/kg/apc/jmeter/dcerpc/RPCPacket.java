package kg.apc.jmeter.dcerpc;

// this class holds common knowlege for rpc requests

import org.apache.jorphan.util.JOrphanUtils;

/**
 *
 * @author apc
 */
public abstract class RPCPacket
{
   /* packet types */
   /**
    *
    */
   public static final byte PACKET_TYPE_UNKNOWN = -1;
   /**
    *
    */
   public static final byte PACKET_TYPE_REQUEST = 0;
   /**
    *
    */
   public static final byte PACKET_TYPE_RESPONSE = 2;
   /**
    *
    */
   public static final byte PACKET_TYPE_FAULT = 3;
   /**
    *
    */
   public static final byte PACKET_TYPE_BIND = 11;
   /**
    *
    */
   public static final byte PACKET_TYPE_BINDACK = 12;
   /**
    *
    */
   public static final byte PACKET_TYPE_BINDNACK = 13;
   // packet flags
   /**
    *
    */
   public static final byte PACKET_FLAG_FIRST = 0x1;
   /**
    *
    */
   public static final byte PACKET_FLAG_LAST = 0x2;
   /* packet offsets */
   /**
    *
    */
   public static final short COMMONHEADERLENGTH = 16;
   /* common packet fields */
   /**
    *
    */
   protected static byte version = 5;
   /**
    *
    */
   protected static byte versionminor = 0;
   /**
    *
    */
   protected byte packetType = PACKET_TYPE_UNKNOWN;
   /**
    *
    */
   protected byte packetFlags = PACKET_FLAG_FIRST | PACKET_FLAG_LAST;
   /**
    *
    */
   protected static byte byteOrder = 16;
   /**
    *
    */
   protected static byte character = 0;
   /**
    *
    */
   protected static byte floatingPoint = 0;
   /**
    *
    */
   protected short fragLength;
   /**
    *
    */
   protected static short authLength = 0;
   /**
    *
    */
   protected int callID = 1;

   /**
    *
    * @return
    */
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
      System.arraycopy(BinaryUtils.shortToByteArray(fragLength), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(BinaryUtils.shortToByteArray(authLength), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(BinaryUtils.intToByteArray(callID), 0, header, curPos, 4);
      curPos += 4;

      return header;
   }

   /**
    *
    * @return
    */
   public final byte[] getBytes()
   {
      byte[] body = getBodyBytes();

      byte[] header = getHeaderBytes();

      fragLength = (short) (COMMONHEADERLENGTH + header.length + body.length);

      byte[] commonheader = getCommonHeaderBytes();

      byte[] result = new byte[fragLength];
      System.arraycopy(commonheader, 0, result, 0, commonheader.length);
      System.arraycopy(header, 0, result, commonheader.length, header.length);
      System.arraycopy(body, 0, result, commonheader.length + header.length, body.length);

      return result;
   }

   /**
    *
    * @return
    */
   abstract protected byte[] getHeaderBytes();

   /**
    *
    * @return
    */
   abstract protected byte[] getBodyBytes();

   /**
    *
    * @param toByteArray
    * @return
    */
   public static byte getPacketFlags(byte[] toByteArray)
   {
      return toByteArray[3];
   }

   /**
    *
    * @param resultBytes
    * @return
    * @throws RPCCallException
    */
   public static byte[] getPacketWithoutHeader(byte[] resultBytes) throws RPCCallException
   {
      final byte packetType = resultBytes[2];
      byte[] returnBytes = null;
      short offset = COMMONHEADERLENGTH;

      switch (packetType)
      {
         case PACKET_TYPE_BINDNACK:
            throw new RPCCallException("BindNack received");

         case PACKET_TYPE_FAULT:
            throw new RPCCallException("RPC Fault received");

         case PACKET_TYPE_BINDACK:
            break;

         case PACKET_TYPE_RESPONSE:
            offset += 8;
            break;

         default:
            throw new UnsupportedOperationException("Unsupported packet type: " + Byte.toString(packetType) + ", full HEX: " +JOrphanUtils.baToHexString(resultBytes));
      }

      returnBytes = new byte[resultBytes.length - offset];
      System.arraycopy(resultBytes, offset,
            returnBytes, 0, resultBytes.length - offset);

      return returnBytes;
   }

   /**
    *
    * @param buffer
    * @return
    */
   public static short fragLenFromRPCHeader(byte[] buffer)
   {
      return BinaryUtils.twoBytesToShortVal(buffer[8], buffer[9]);
   }
}
