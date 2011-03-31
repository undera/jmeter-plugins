package kg.apc.jmeter.samplers;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author undera
 */
class HTTPRawSamplerDirectFile extends HTTPRawSampler {

    @Override
    protected byte[] processIO(SampleResult res) throws Exception {
        FileInputStream is=new FileInputStream(new File(getRequestData()));
        FileChannel source=is.getChannel();

        ByteBuffer sendBuf = ByteBuffer.allocateDirect(1024*4);// is it efficient enough?
        SocketChannel sock = (SocketChannel) getSocketChannel();
        while(source.read(sendBuf)>0)
        {
            sock.write(sendBuf);
            sendBuf.rewind();
        }

        return readResponse(sock, res);
    }
}
