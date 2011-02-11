package kg.apc.jmeter.samplers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class HTTPRawSampler extends AbstractSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final String hostname = "";
    private String data = "GET / HTTP/1.1\r\nConnection: close\r\nUser-Agent: Jakarta Commons-HttpClient/3.1\r\nHost: :8080\r\n\r\n";
    private SocketAddress addr=new InetSocketAddress(hostname, 8080);

    public SampleResult sample(Entry e) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(NAME);
        res.setSamplerData(getRawRequest());
        res.sampleStart();
        res.setSuccessful(true);
        try {
            res.setResponseData(processIO(res));
        } catch (UnknownHostException ex) {
            res.sampleEnd();
            log.error(hostname, ex);
            res.setSuccessful(false);
        } catch (IOException ex) {
            res.sampleEnd();
            log.error("Exception in sampler", ex);
            res.setSuccessful(false);
        }


        return res;
    }

    private String getRawRequest() {
        return data;
    }

    private byte[] processIO(SampleResult res) throws UnknownHostException, IOException {
        log.debug("HREF");
        SocketChannel sock=SocketChannel.open();
        sock.connect(addr);
        sock.write(ByteBuffer.wrap(getRawRequest().getBytes()));
        ByteBuffer buf = ByteBuffer.allocate(0);
        sock.read(buf);
        //sock.close();
        log.debug("Test "+new String(buf.array()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(HTTPRawSampler.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        Socket sock = getSocket();
        sock.setSoTimeout(1000);
        OutputStream os = sock.getOutputStream();
        os.write(getRawRequest().getBytes());
        InputStream is = sock.getInputStream();
        ByteArrayOutputStream buf = new ByteArrayOutputStream(0);
        int b;
        while ((b = is.read()) > 0) {
            buf.write(b);
        }
        is.close();
        os.close();
         * 
         */
        sock.close();
        res.sampleEnd();

        return buf.array();
    }
}
