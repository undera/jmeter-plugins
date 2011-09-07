package kg.apc.jmeter.dcerpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import org.apache.jmeter.protocol.tcp.sampler.AbstractTCPClient;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class DCERPCSampler
     extends AbstractTCPClient
{
   private static final Logger log = LoggingManager.getLoggerForClass();
   private String unmarshalOptions = "";

   public void write(OutputStream os, InputStream is)
   {
      throw new UnsupportedOperationException();
   }

   public String read(InputStream is)
   {
      String hexString = "";
      try
      {
         byte packetFlags = RPCPacket.PACKET_FLAG_FIRST;
         byte[] packetBytes;
         byte[] packetWithoutHeader = null;
         ByteArrayOutputStream stubDataStream = new ByteArrayOutputStream();
         while ((packetFlags & RPCPacket.PACKET_FLAG_LAST) != RPCPacket.PACKET_FLAG_LAST)
         {
            packetBytes = readNextPacket(is);

            packetFlags = RPCPacket.getPacketFlags(packetBytes);
            log.debug("Read packet flags: " + Byte.toString(packetFlags));
            try
            {
               packetWithoutHeader = RPCPacket.getPacketWithoutHeader(packetBytes);
            }
            catch (RPCCallException ex)
            {
               log.error("RPC returned error", ex);
               packetWithoutHeader = packetBytes;
            }
            stubDataStream.write(packetWithoutHeader);
         }

         hexString = DCERPCMarshalling.unmarshalData(stubDataStream.toByteArray(), unmarshalOptions);
      }
      catch (RPCMarshallingException ex)
      {
         log.error("Unmarshal error: ", ex);
         hexString = "";
      }
      catch (SocketTimeoutException e)
      {
         // drop out to handle buffer
         log.warn(e.getMessage());
      }
      catch (InterruptedIOException e)
      {
         // drop out to handle buffer
         log.warn(e.getMessage());
      }
      catch (IOException e)
      {
         log.warn("Read error:" + e);
         hexString = "";
      }

      return hexString;
   }

   private byte[] readNextPacket(InputStream is)
        throws IOException
   {
      byte[] buffer = new byte[4096];
      short fragLen = 0;
      int singleReadCount = 0;
      int totalReadCount = 0;
      ByteArrayOutputStream resultsStream = new ByteArrayOutputStream();
      int readLimit = buffer.length;

      log.debug("Reading next packet");

      // read header
      singleReadCount = is.read(buffer, 0, RPCPacket.COMMONHEADERLENGTH);
      if (singleReadCount != RPCPacket.COMMONHEADERLENGTH)
      {
         throw new IOException("Failed to read RPC header bytes");
      }
      resultsStream.write(buffer, 0, singleReadCount);
      totalReadCount += singleReadCount;
      fragLen = RPCPacket.fragLenFromRPCHeader(buffer);
      readLimit = fragLen - totalReadCount;
      log.debug("Read header: " + Integer.toString(totalReadCount) + " bytes, total size: " + Integer.toString(fragLen));

      // read body
      while ((singleReadCount = is.read(buffer, 0, readLimit > buffer.length ? buffer.length : readLimit)) > -1)
      {
         resultsStream.write(buffer, 0, singleReadCount);
         totalReadCount += singleReadCount;
         readLimit = fragLen - totalReadCount;
         log.debug("Read body: " + Integer.toString(totalReadCount) + "/" + Integer.toString(fragLen) + " bytes");
         if (totalReadCount >= fragLen)
         {
            break;
         }
      }

      return resultsStream.toByteArray();
   }

   public void write(OutputStream os, String paramsdata)
   {
      String callParams;
      String stubData;
      int pos = paramsdata.indexOf('\n');
      if (pos < 0)
      {
         callParams = paramsdata;
         stubData = "";
      }
      else
      {
         stubData = paramsdata.substring(pos);
         callParams = paramsdata.substring(0, pos);
      }

      setReadParams(callParams);

      RPCPacket[] callReq = DCERPCSamplerUtils.getRequestsArrayByString(callParams, stubData);
      byte[] reqBytes;
      int packetNum;
      for (packetNum = 0; packetNum < callReq.length; packetNum++)
      {
         reqBytes = callReq[packetNum].getBytes();
         log.debug("Bytes to send: " + Integer.toString(reqBytes.length));

         try
         {
            os.write(reqBytes);
         }
         catch (IOException ex)
         {
            log.error("Request error", ex);
         }
      }
   }

   private void setReadParams(String callParams)
   {
      String[] fields = callParams.split("[\t ]");
      if (fields.length > 2)
      {
         unmarshalOptions = fields[2];
      }
   }
}
