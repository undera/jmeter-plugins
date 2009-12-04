package kg.apc.jmeter.dcerpc;

public class RPCCallRequest
   extends RPCRequest
{

   protected int allocHint;
   protected int contextID;
   protected int opNum;

   public RPCCallRequest()
   {
      packetType=PACKET_TYPE_REQUEST;
   }

   @Override
   protected byte[] getBodyBytes()
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   protected byte[] getHeaderBytes()
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
