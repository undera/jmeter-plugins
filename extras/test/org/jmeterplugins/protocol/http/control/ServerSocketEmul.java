package org.jmeterplugins.protocol.http.control;

import java.io.IOException;

public class ServerSocketEmul extends java.net.ServerSocket {
    public ServerSocketEmul() throws IOException {
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
