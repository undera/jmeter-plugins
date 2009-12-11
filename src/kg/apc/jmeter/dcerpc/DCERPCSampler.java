package kg.apc.jmeter.dcerpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import org.apache.jmeter.protocol.tcp.sampler.AbstractTCPClient;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public class DCERPCSampler
   extends AbstractTCPClient
{

   private static final Logger log = LoggingManager.getLoggerForClass();

   public void write(OutputStream os, InputStream is)
   {
      throw new UnsupportedOperationException();
   }

   public String read(InputStream is)
   {
      byte[] buffer = new byte[4096];
      ByteArrayOutputStream resultsStream = new ByteArrayOutputStream();
      int readCount = 0;
      int count = 0;
      short responseSize = 0;
      try
      {
         while ((readCount = is.read(buffer)) > -1)
         {
            resultsStream.write(buffer, 0, readCount);

            // get responsesize from RPC header at first read
            if (count == 0)
            {
               responseSize = RPCRequest.fragLenFromRPCHeader(buffer);
            }

            count += readCount;
            log.debug("Read " + Integer.toString(count) + "/" + Integer.toString(responseSize) + " bytes");
            if (count >= responseSize)
            {
               break;
            }
         }
      }
      catch (SocketTimeoutException e)
      {
         // drop out to handle buffer
      }
      catch (InterruptedIOException e)
      {
         // drop out to handle buffer
      }
      catch (IOException e)
      {
         log.warn("Read error:" + e);
         return "";
      }

      final String hexString = JOrphanUtils.baToHexString(resultsStream.toByteArray());
      log.debug("Read: " + resultsStream.size() + ": " + hexString);

      return hexString;
   }

   public void write(OutputStream os, String s)
   {
      RPCRequest callReq = getRequestByString(s);
      byte[] reqBytes = callReq.getBytes();
      log.debug("Request bytes to send: " + JOrphanUtils.baToHexString(reqBytes));

      try
      {
         os.write(reqBytes);
      }
      catch (IOException ex)
      {
         log.warn("Request error:" + ex);
         java.util.logging.Logger.getLogger(DCERPCSampler.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public static RPCRequest getRequestByString(String s)
   {
      String[] fields = s.split("\n");
      if (fields.length < 3)
      {
         throw new IllegalArgumentException("Request must contain 3 parameter lines");
      }

      RPCRequest result;
      if (fields[0].toLowerCase().trim().equals("bind"))
      {
         result = new RPCBindRequest(fields[1], fields[2]);
      }
      else
      {
         result = new RPCCallRequest(Integer.parseInt(fields[0]), Short.parseShort(fields[1]), BinaryTCPClientImpl.hexStringToByteArray(fields[2]));
      }
      return result;
   }
}
