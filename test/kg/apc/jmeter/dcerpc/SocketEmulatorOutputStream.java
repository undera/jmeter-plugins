package kg.apc.jmeter.dcerpc;

import java.io.IOException;
import java.io.OutputStream;

class SocketEmulatorOutputStream
     extends OutputStream
{
   private String buffer;

   public SocketEmulatorOutputStream()
   {
      buffer="";
   }

   @Override
   public void write(int b)
        throws IOException
   {
      buffer+=(b>10?"":"0")+Integer.toHexString(b);
   }

   String getWrittenBytes()
   {
      return buffer;
   }
}
