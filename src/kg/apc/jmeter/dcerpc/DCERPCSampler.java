package kg.apc.jmeter.dcerpc;

import java.io.InputStream;
import java.io.OutputStream;
import org.apache.jmeter.protocol.tcp.sampler.AbstractTCPClient;

public class DCERPCSampler
   extends AbstractTCPClient
{

   public void write(OutputStream os, InputStream is)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void write(OutputStream os, String s)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public String read(InputStream is)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
