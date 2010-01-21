package kg.apc.jmeter.dcerpc;

import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class DCERPCSamplerUtils
{
   public static short getOpNum(String[] fields)
   {
      short opNum;
      try
      {
         opNum = Short.parseShort(fields[1]);
      }
      catch (NumberFormatException e)
      {
         opNum = Short.MAX_VALUE;
         int report = JMeterContextService.getContext().getThreadNum();
         String var = fields[1];
         var = var.replace("${", "");
         var = var.replace("}", "");
         JMeterVariables vars = JMeterContextService.getContext().getVariables();
         Logger log = LoggingManager.getLoggerForClass();
         log.error(Integer.toString(report) + " Wrong OpNum supplied: " + fields[1] + "=" + vars.get(var), e);
         Iterator it = vars.getIterator();
         while (it.hasNext())
         {
            log.info(it.next().toString());
         }
      }
      return opNum;
   }

   public static RPCPacket[] getPacketsArray(final byte[] stubDataByteArray, final int callID, short opNum)
   {
      final int chunkLenLimit = RPCBindRequest.maxXmitFrag - RPCCallRequest.HEADER_LENGTH;

      int numPackets = (int) Math.ceil((double) stubDataByteArray.length / (double) chunkLenLimit);
      if (numPackets < 1)
      {
         throw new IllegalArgumentException("StubData resulted to invalid packets quantity: "
              + Integer.toString(numPackets)
              + "[" + Integer.toString(stubDataByteArray.length)
              + "/" + Integer.toString(chunkLenLimit) + "]");
      }

      RPCPacket[] result = new RPCCallRequest[numPackets];
      byte packetFlags;
      byte[] stubDataChunk;
      int chunkLen;
      for (int packetNum = 0; packetNum < numPackets; packetNum++)
      {
         packetFlags = getPacketFlags(packetNum, numPackets);

         chunkLen = (packetNum == numPackets - 1) ? stubDataByteArray.length % chunkLenLimit : chunkLenLimit;
         stubDataChunk = new byte[chunkLen];
         System.arraycopy(stubDataByteArray, packetNum * chunkLenLimit, stubDataChunk, 0, chunkLen);
         result[packetNum] = new RPCCallRequest(callID, opNum, stubDataChunk, packetFlags);
      }
      return result;
   }

   public static byte getPacketFlags(int packetNum, int numPackets)
   {
      byte packetFlags = 0;
      if (packetNum == 0)
      {
         packetFlags |= RPCCallRequest.PACKET_FLAG_FIRST;
      }
      if (packetNum == numPackets - 1)
      {
         packetFlags |= RPCCallRequest.PACKET_FLAG_LAST;
      }
      return packetFlags;
   }

   public static String getStubDataHex(String joinedStr)
   {
      String stubDataHex = null;
      try
      {
         stubDataHex = BinaryUtils.hexEncodeTextParts(joinedStr);
      }
      catch (RPCMarshallingException ex)
      {
         Logger log = LoggingManager.getLoggerForClass();
         log.error("Error in hexEncodeTextParts", ex);
      }
      stubDataHex = stubDataHex.replace("\n", "");
      stubDataHex = stubDataHex.replace("\r", "");
      if (stubDataHex.length() % 2 != 0)
      {
         Logger log = LoggingManager.getLoggerForClass();
         log.warn("Uneven HEX: " + stubDataHex);
      }
      return stubDataHex;
   }

   public static RPCPacket[] getRequestsArrayByString(String s)
   {
      RPCPacket[] result;

      String[] fields = s.split("\n");
      if (fields.length < 3)
      {
         throw new IllegalArgumentException("Request must contain at least 3 parameter lines");
      }

      short opNum;
      if (fields[0].toLowerCase().trim().equals("bind"))
      {
         result = new RPCBindRequest[1];
         result[0] = new RPCBindRequest(fields[1], fields[2]);
      }
      else
      {
         final int callID = Integer.parseInt(fields[0]);
         fields[0] = "";
         opNum = getOpNum(fields);
         fields[1] = "";

         String joinedStr = StringUtils.join(fields, "\n");
         String stubDataHex = getStubDataHex(joinedStr);

         final byte[] stubDataByteArray = BinaryTCPClientImpl.hexStringToByteArray(stubDataHex);
         result = getPacketsArray(stubDataByteArray, callID, opNum);
      }

      return result;
   }
}
