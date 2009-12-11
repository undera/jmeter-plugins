package kg.apc.jmeter.dcerpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import org.apache.jmeter.protocol.tcp.sampler.AbstractTCPClient;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.apache.xpath.compiler.OpCodes;

public class DCERPCSampler
   extends AbstractTCPClient
{

   private static final Logger log = LoggingManager.getLoggerForClass();
   private String InterfaceUUID;
   private String TransferSyntax;
   private int ReadLimit = 0;
   private boolean isBindDone = false;
   private int callID = 1;
   private short opNum = 0;

   public void write(OutputStream os, InputStream is)
   {
      if (!isBindDone)
      {
         try
         {
            doRPCBind(os, is);
            isBindDone = true;
         }
         catch (IOException ex)
         {
            log.warn("Bind error:" + ex);
            java.util.logging.Logger.getLogger(DCERPCSampler.class.getName()).log(Level.SEVERE, null, ex);
         }
      }

      callID++;
      RPCCallRequest callReq = new RPCCallRequest(callID, opNum, new byte[0]);
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

   public String read(InputStream is)
   {
      byte[] buffer = new byte[4096];
      ByteArrayOutputStream resultsStream = new ByteArrayOutputStream();
      int readCount = 0;
      int count = 0;
      try
      {
         while ((readCount = is.read(buffer)) > -1)
         {
            resultsStream.write(buffer, 0, readCount);
            count += readCount;
            log.debug("Read " + Integer.toString(count) + "/" + Integer.toString(ReadLimit) + " bytes");
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

      final String hexString = JOrphanUtils.baToHexString(resultsStream.toByteArray());
      log.debug("Read: " + resultsStream.size() + ": " + hexString);

      resetReadLimit();
      return hexString;
   }

   public void write(OutputStream os, String s)
   {
      throw new UnsupportedOperationException("Not applicable");
   }

   private void resetReadLimit()
   {
      setReadLimit(-1);
   }

   /**
    * @param ReadLimit the ReadLimit to set
    */
   public void setReadLimit(int ReadLimit)
   {
      this.ReadLimit = ReadLimit;
   }

   /**
    * @return the InterfaceUUID
    */
   public String getInterfaceUUID()
   {
      return InterfaceUUID;
   }

   /**
    * @param InterfaceUUID the InterfaceUUID to set
    */
   public void setInterfaceUUID(String InterfaceUUID)
   {
      this.InterfaceUUID = InterfaceUUID;
   }

   /**
    * @return the TransferSyntax
    */
   public String getTransferSyntax()
   {
      return TransferSyntax;
   }

   /**
    * @param TransferSyntax the TransferSyntax to set
    */
   public void setTransferSyntax(String TransferSyntax)
   {
      this.TransferSyntax = TransferSyntax;
   }

   private void doRPCBind(OutputStream os, InputStream is) throws IOException
   {
      // bind request
      RPCBindRequest bindRequest = new RPCBindRequest(getInterfaceUUID(), getTransferSyntax());
      os.write(bindRequest.getBytes());

      setReadLimit(60);
      String bindResult = read(is);
      log.debug("Bind result: " + bindResult);
   }
}
