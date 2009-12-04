package kg.apc.jmeter.dcerpc;

public class RPCBindRequest
   extends RPCRequest
{

   protected static short maxXmitFrag = 5840;
   protected static short maxRecvFrag = 5840;
   protected static int assocGroup = 0;
   protected static int numCtxItems = 1; // handling only one item
   protected static short contextID = 0;
   protected static byte numTransItems = 1;
   protected byte[] interfaceUUID;
   protected static short interfaceVer = 1;
   protected static short interfaceVerMinor = 0;
   protected byte[] transferSyntax;
   protected static int transferSyntaxVer = 2;

   public RPCBindRequest(String ainterfaceUUID, String atransferSyntax)
   {
      packetType = RPCRequest.PACKET_TYPE_BIND;
      interfaceUUID = UUIDToByteArray(ainterfaceUUID);
      transferSyntax = UUIDToByteArray(atransferSyntax);
   }

   @Override
   protected byte[] getHeaderBytes()
   {
      byte[] header = new byte[56];

      int curPos = 0;
      System.arraycopy(shortToByteArray(maxXmitFrag), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(shortToByteArray(maxRecvFrag), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(intToByteArray(assocGroup), 0, header, curPos, 4);
      curPos += 4;
      System.arraycopy(intToByteArray(numCtxItems), 0, header, curPos, 4);
      curPos += 4;
      System.arraycopy(shortToByteArray(contextID), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(shortToByteArray(numTransItems), 0, header, curPos, 2);
      curPos += 2;

      System.arraycopy(interfaceUUID, 0, header, curPos, 16);
      curPos += 16;
      System.arraycopy(shortToByteArray(interfaceVer), 0, header, curPos, 2);
      curPos += 2;
      System.arraycopy(shortToByteArray(interfaceVerMinor), 0, header, curPos, 2);
      curPos += 2;


      System.arraycopy(transferSyntax, 0, header, curPos, 16);
      curPos += 16;
      System.arraycopy(intToByteArray(transferSyntaxVer), 0, header, curPos, 4);
      curPos += 4;

      return header;
   }

   @Override
   protected byte[] getBodyBytes()
   {
      byte[] result =
      {
      };
      return result;
   }
}
