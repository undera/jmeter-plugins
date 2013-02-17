package kg.apc.jmeter.dcerpc;

/**
 *
 * @author apc
 */
public class RPCCallRequest
      extends RPCPacket
{
   /**
    *
    */
   public static final short HEADER_LENGTH = 24; // that's not good, but I dont know the case when header is different
   private int allocHint;
   /**
    *
    */
   protected short contextID;
   /**
    *
    */
   protected short opNum;
   private byte[] stubData;
   
   /**
    *
    * @param acallID
    * @param aOpnum
    * @param aStubData
    * @param aPacketFlags
    */
   public RPCCallRequest(int acallID, short aOpnum, byte[] aStubData, byte aPacketFlags)
   {
      if (aStubData == null)
      {
         throw new IllegalArgumentException("Stub data cannot be null");
      }

      packetType = PACKET_TYPE_REQUEST;
      packetFlags=aPacketFlags;
      callID = acallID;
      opNum = aOpnum;
      stubData = aStubData;
      allocHint=stubData.length;
   }

   /**
    *
    * @return
    */
   @Override
   protected byte[] getBodyBytes()
   {
      return stubData;
   }

   /**
    *
    * @return
    */
   @Override
   protected byte[] getHeaderBytes()
   {
      byte[] result = new byte[8];

      int curPos = 0;
      System.arraycopy(BinaryUtils.intToByteArray(allocHint), 0, result, curPos, 4);
      curPos += 4;
      System.arraycopy(BinaryUtils.shortToByteArray(contextID), 0, result, curPos, 2);
      curPos += 2;
      System.arraycopy(BinaryUtils.shortToByteArray(opNum), 0, result, curPos, 2);
      curPos += 2;

      return result;
   }

   /**
    * @param allocHint the allocHint to set
    */
   public void setAllocHint(int allocHint)
   {
      this.allocHint = allocHint;
   }
}