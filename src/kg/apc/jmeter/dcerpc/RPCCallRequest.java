package kg.apc.jmeter.dcerpc;

public class RPCCallRequest
   extends RPCRequest
{

   protected int allocHint;
   protected short contextID;
   protected short opNum;
   private byte[] stubData;

   public RPCCallRequest(int acallID, short aOpnum, byte[] aStubData)
   {
      if (aStubData == null)
      {
         throw new IllegalArgumentException("Stub data cannot be null");
      }

      packetType = PACKET_TYPE_REQUEST;
      callID = acallID;
      opNum = aOpnum;
      stubData = aStubData;
   }

   @Override
   protected byte[] getBodyBytes()
   {
      return stubData;
   }

   @Override
   protected byte[] getHeaderBytes()
   {
      byte[] result = new byte[8];

      int curPos = 0;
      System.arraycopy(intToByteArray(stubData.length), 0, result, curPos, 4);
      curPos += 4;
      System.arraycopy(shortToByteArray(contextID), 0, result, curPos, 2);
      curPos += 2;
      System.arraycopy(shortToByteArray(opNum), 0, result, curPos, 2);
      curPos += 2;

      return result;
   }
}
