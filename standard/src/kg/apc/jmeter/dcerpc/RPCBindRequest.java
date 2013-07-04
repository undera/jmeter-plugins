package kg.apc.jmeter.dcerpc;

/**
 *
 * @author apc
 */
public class RPCBindRequest
      extends RPCPacket
{
   /**
    *
    */
   public static final short maxXmitFrag = 5840;
   /**
    *
    */
   public static final short maxRecvFrag = 5840;
   /**
    *
    */
   protected static int assocGroup = 0;
   /**
    *
    */
   protected static int numCtxItems = 1; // handling only one item
   /**
    *
    */
   protected static short contextID = 0;
   /**
    *
    */
   protected static byte numTransItems = 1;
   /**
    *
    */
   protected byte[] interfaceUUID;
   /**
    *
    */
   protected static short interfaceVer = 1;
   /**
    *
    */
   protected static short interfaceVerMinor = 0;
   /**
    *
    */
   protected byte[] transferSyntax;
   /**
    *
    */
   protected static int transferSyntaxVer = 2;

   /**
    *
    * @param ainterfaceUUID
    * @param atransferSyntax
    */
   public RPCBindRequest(String ainterfaceUUID, String atransferSyntax)
   {
      packetType = RPCPacket.PACKET_TYPE_BIND;
      interfaceUUID = BinaryUtils.UUIDToByteArray(ainterfaceUUID);
      transferSyntax = BinaryUtils.UUIDToByteArray(atransferSyntax);
   }

   /**
    *
    * @return
    */
   @Override
   protected byte[] getHeaderBytes()
   {
      byte[] header = new byte[56];

      int curPos = 0;
      System.arraycopy(BinaryUtils.shortToByteArray(maxXmitFrag), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(BinaryUtils.shortToByteArray(maxRecvFrag), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(BinaryUtils.intToByteArray(assocGroup), 0, header, curPos, 4);
      curPos += 4;
      System.arraycopy(BinaryUtils.intToByteArray(numCtxItems), 0, header, curPos, 4);
      curPos += 4;
      System.arraycopy(BinaryUtils.shortToByteArray(contextID), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(BinaryUtils.shortToByteArray(numTransItems), 0, header, curPos, 2);
      curPos += 2;

      System.arraycopy(interfaceUUID, 0, header, curPos, 16);
      curPos += 16;
      System.arraycopy(BinaryUtils.shortToByteArray(interfaceVer), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(BinaryUtils.shortToByteArray(interfaceVerMinor), 0, header, curPos, 2);
      curPos += 2;

      System.arraycopy(transferSyntax, 0, header, curPos, 16);
      curPos += 16;
      System.arraycopy(BinaryUtils.intToByteArray(transferSyntaxVer), 0, header, curPos, 4);
      curPos += 4;

      return header;
   }

   /**
    *
    * @return
    */
   @Override
   protected byte[] getBodyBytes()
   {
      byte[] result =
      {
      };
      return result;
   }
}