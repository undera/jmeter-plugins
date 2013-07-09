package kg.apc.emulators;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

/**
 *
 * @author APC
 */
public class TestSocketFactory
      extends SocketFactory
{
   private Socket socket;

   public TestSocketFactory()
   {
      socket = new SocketEmulator();
   }

   @Override
   public Socket createSocket(String string, int i) throws IOException, UnknownHostException
   {
      return socket;
   }

   @Override
   public Socket createSocket() throws IOException
   {
      return socket;
   }

   @Override
   public Socket createSocket(String string, int i, InetAddress ia, int i1) throws IOException, UnknownHostException
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public Socket createSocket(InetAddress ia, int i) throws IOException
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public Socket createSocket(InetAddress ia, int i, InetAddress ia1, int i1) throws IOException
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
