package kg.apc.jmeter.dcerpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.UUID;
import org.apache.jmeter.protocol.tcp.sampler.AbstractTCPClient;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public class DCERPCSampler
   extends AbstractTCPClient
{

   private static final Logger log = LoggingManager.getLoggerForClass();
   private String InterfaceUUID;
   private String TransferSyntax;
   private int ReadLimit = -1;

   public DCERPCSampler()
   {
      super();
      InterfaceUUID = "c2ce97a0-8b15-11d1-96ab-00a0c9103fcf";
      TransferSyntax = UUID.randomUUID().toString();
   }

   public void write(OutputStream os, InputStream is)
   {
      RPCBindRequest instance = new RPCBindRequest(InterfaceUUID, TransferSyntax);
      byte[] sendPacket = instance.getBytes();
      try
      {
         os.write(sendPacket);
         os.flush();
      }
      catch (IOException e)
      {
         log.warn("Write error", e);
      }
      log.debug("Wrote rpc bind packet: " + JOrphanUtils.baToHexString(sendPacket));
   }

   public String read(InputStream is)
   {
      byte[] buffer = new byte[4096];
      ByteArrayOutputStream w = new ByteArrayOutputStream();
      int x = 0;
      int count = 0;
      try
      {
         while ((x = is.read(buffer)) > -1)
         {
            w.write(buffer, 0, x);
            count++;
            if (count >= ReadLimit)
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

      final String hexString = JOrphanUtils.baToHexString(w.toByteArray());
      log.debug("Read: " + w.size() + ": " + hexString);
      return hexString;
   }

   public void write(OutputStream os, String s)
   {
      throw new UnsupportedOperationException("Not applicable");
   }
}
