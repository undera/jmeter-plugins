package kg.apc.emulators;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author APC
 */
public class SocketEmulator
      extends Socket
{
   private final SocketEmulatorOutputStream os;
   private final SocketEmulatorInputStream is;

   public SocketEmulator()
   {
      os = new SocketEmulatorOutputStream();
      is = new SocketEmulatorInputStream();
   }

   @Override
   public OutputStream getOutputStream() throws IOException
   {
      return os;
   }

   @Override
   public InputStream getInputStream() throws IOException
   {
      return is;
   }
}
