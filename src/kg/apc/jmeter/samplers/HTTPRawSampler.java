package kg.apc.jmeter.samplers;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class HTTPRawSampler extends AbstractSampler {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private String data = "GET / HTTP/1.1\r\n"
            + "Connection: close\r\n"
            + "User-Agent: Jakarta Commons-HttpClient/3.1\r\n"
            + "Host: "+getHostName()+":"+getPort()+"\r\n\r\n";
    private SocketAddress addr;
    private ByteBuffer recvBuf=ByteBuffer.allocateDirect(1024*1);
    private ByteBuffer sendBuf;
    //private SocketChannel sock;

    public HTTPRawSampler() {
        super();
        sendBuf=ByteBuffer.wrap(data.getBytes());
        addr=new InetSocketAddress(getHostName(), getPort());
    }

    public final String getHostName()
    {
        String res = JMeterUtils.getPropDefault("host", "192.168.0.1");
        if (res.length()<1) res="192.168.0.1";
        //log.debug("Host:"+res);
        return res;
    }

    private int getPort() {
        int res = 80;
        try { res=Integer.parseInt(JMeterUtils.getPropDefault("port", "80")); }
        catch (Exception e) {
        log.error("", e);}
        return res;
    }

    public SampleResult sample(Entry e) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(NAME);
        res.setSamplerData(getRawRequest());
        res.sampleStart();
        res.setSuccessful(true);
        try {
            res.setResponseData(processIO(res));
        } catch (Exception ex) {
            log.error(getHostName(), ex);
            res.sampleEnd();
            res.setSuccessful(false);
        }

        return res;
    }

    private String getRawRequest() {
        return data;
    }

    private byte[] processIO(SampleResult res) throws Exception {
        log.info("Begin IO");
        SocketChannel sock = SocketChannel.open(addr);
        ByteArrayOutputStream response=new ByteArrayOutputStream();
        //sock.connect(addr);
        //sock.connect(addr);

        sock.write(sendBuf);
        int cnt=0;
        recvBuf.clear();
        while ((cnt=sock.read(recvBuf)) != -1) {
            log.info("Read "+cnt);
            recvBuf.flip();
            byte[] bytes = new byte[cnt];
            recvBuf.get(bytes);
            response.write(bytes);
            recvBuf.clear();
        }
        res.sampleEnd();

        sock.close();
        log.info("End IO");
        return response.toByteArray();
    }
}
