package kg.apc.jmeter.util;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.jmeter.protocol.tcp.sampler.BinaryTCPClientImpl;

/**
 *
 * @author apc
 */
public class SocketEmulatorOutputStream
     extends OutputStream
{
   private StringBuffer buffer;

   /**
    *
    */
   public SocketEmulatorOutputStream()
   {
      buffer=new StringBuffer();
   }

   @Override
   public void write(int b)
        throws IOException
   {
      buffer.append((b>10?"":"0")+Integer.toHexString(b));
   }

   /**
    * 
    * @return
    */
   public String getWrittenBytesAsHexString()
   {
      final String toString = buffer.toString();
      buffer.setLength(0);
      return toString;
   }

   public String getWrittenBytesAsString()
   {
        byte[] res = BinaryTCPClientImpl.hexStringToByteArray(getWrittenBytesAsHexString());
      return res.toString();
   }
}
