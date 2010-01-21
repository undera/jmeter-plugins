package kg.apc.jmeter.dcerpc;

import java.io.IOException;
import java.io.OutputStream;

class SocketEmulatorOutputStream
     extends OutputStream
{
   private StringBuffer buffer;

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

   String getWrittenBytes()
   {
      final String toString = buffer.toString();
      buffer.setLength(0);
      return toString;
   }
}
