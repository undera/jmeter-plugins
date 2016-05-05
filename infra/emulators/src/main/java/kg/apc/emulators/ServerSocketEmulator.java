package kg.apc.emulators;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerSocketEmulator extends ServerSocket {

    public ServerSocketEmulator() throws IOException {
    }

    public Socket accept() throws IOException {
        return new SocketEmulator();
    }
}
