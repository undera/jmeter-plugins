package kg.apc.jmeter.samplers;


import java.io.IOException;
import java.nio.channels.SocketChannel;
import kg.apc.jmeter.util.SocketChannelEmul;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

class SocketChannelWithTimeoutsEmul extends SocketChannelWithTimeouts {
    private static final Logger log = LoggingManager.getLoggerForClass();

        protected SocketChannelWithTimeoutsEmul() throws IOException {
           // super();
            log.debug("Created");
        }

        public static SocketChannelWithTimeouts open() throws IOException{
            return new SocketChannelWithTimeoutsEmul();
        }

        @Override
        protected SocketChannel getSocketChannel() throws IOException {
            log.debug("Get socketChannel");
            return new SocketChannelEmul();
        }

        @Override
        public boolean finishConnect() throws IOException {
            log.debug("FinishConnect");
            return true;
        }
    }