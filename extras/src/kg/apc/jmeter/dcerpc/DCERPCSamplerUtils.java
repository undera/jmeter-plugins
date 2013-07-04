package kg.apc.jmeter.dcerpc;

import java.util.Iterator;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class DCERPCSamplerUtils
{
   private static final Logger log = LoggingManager.getLoggerForClass();

   /**
    *
    * @param paramStr
    * @return
    */
   public static short getOpNum(String paramStr)
   {
      short opNum;
      try
      {
         opNum = Short.parseShort(paramStr);
      }
      catch (NumberFormatException e)
      {
         opNum = Short.MAX_VALUE;

         int report = JMeterContextService.getContext().getThreadNum();
         log.error(Integer.toString(report) + " Wrong OpNum supplied: " + paramStr + "=", e);

         JMeterVariables vars = JMeterContextService.getContext().getVariables();
         Iterator it = vars.getIterator();
         while (it.hasNext())
         {
            log.info(it.next().toString());
         }
      }

      return opNum;
   }

   /**
    *
    * @param stubDataByteArray
    * @param callID
    * @param opNum
    * @return
    */
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

   /**
    *
    * @param packetNum
    * @param numPackets
    * @return
    */
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

   /**
    *
    * @param joinedStr
    * @return
    */
   public static String getStubDataHex(String joinedStr)
   {
      String stubDataHex = null;
      try
      {
         stubDataHex = DCERPCMarshalling.marshalData(joinedStr);
      }
      catch (RPCMarshallingException ex)
      {
         log.error("Error in hexEncodeTextParts", ex);
      }
      stubDataHex = stubDataHex.replaceAll("[^0-9^a-f^A-F]", "");

      // God save the Queen! (and request)
      if (stubDataHex.length() % 2 != 0)
      {
         log.warn("Uneven hex, data will be trimmed to even: " + stubDataHex);
         stubDataHex = stubDataHex.substring(0, stubDataHex.length() - 1);
         stubDataHex += JOrphanUtils.baToHexString("!!!DATA WAS TRIMMED!!!".getBytes());
      }
      return stubDataHex;
   }

   /**
    *
    * @param paramsStr
    * @param dataStr
    * @return
    */
   public static RPCPacket[] getRequestsArrayByString(String paramsStr, String dataStr)
   {
      RPCPacket[] result;

      String[] fields = paramsStr.split("[\t ]");
      short opNum;
      if (fields[0].toLowerCase().trim().equals("bind"))
      {
         if (fields.length < 3)
            throw new IllegalArgumentException("Bind request requires 2 params: Interface UUID and Transfer Syntax");

         result = new RPCBindRequest[1];
         result[0] = new RPCBindRequest(fields[1], fields[2]);
      }
      else
      {
         if (fields.length < 2)
            throw new IllegalArgumentException("Call requires 2 params: CallID and OpNum");

         final int callID = Integer.parseInt(fields[0]);
         opNum = getOpNum(fields[1]);

         String stubDataHex = getStubDataHex(dataStr);

         byte[] stubDataByteArray = stubDataByteArray = BinaryTCPClientImpl.hexStringToByteArray(stubDataHex);
         result = getPacketsArray(stubDataByteArray, callID, opNum);
      }

      return result;
   }
}
