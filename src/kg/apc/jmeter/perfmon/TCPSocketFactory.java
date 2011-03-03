package kg.apc.jmeter.perfmon;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

/**
 * This class used to separate real sockets from test sockets
 * @author APC
 */
// FIXME: eliminate this class
class TCPSocketFactory
      extends SocketFactory
{
   @Override
   public Socket createSocket(String string, int i) throws IOException, UnknownHostException
   {
     return new Socket(string, i);
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
