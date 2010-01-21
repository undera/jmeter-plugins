package kg.apc.jmeter.dcerpc;

import java.io.IOException;
import java.io.InputStream;

class SocketEmulatorInputStream
     extends InputStream
{
   private byte[] bytes;
   private int pos = 0;

   void setBytesToRead(byte[] hexStringToByteArray)
   {
      bytes = hexStringToByteArray;
   }

   @Override
   public int read()
        throws IOException
   {
      if (bytes == null)
         throw new IOException("Expected data was not set");

      if (pos >= bytes.length)
      {
         bytes = null;
         pos = 0;
         return -1;
      }
      else
         return bytes[pos++];
   }
}
