package org.jmeterplugins.protocol.http.control;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class HttpSimpleTableServerEmul extends HttpSimpleTableServer {
    private static final Logger log = LoggerFactory.getLogger(HttpSimpleTableServerEmul.class);

    @Override
    public void start() throws IOException {
        myServerSocket = new ServerSocketEmul();
        myThread = new ThreadEmul();
        myThread.start();
    }

    @Override
    public void stop() {
        ((ThreadEmul) myThread).finish();
    }


    public HttpSimpleTableServerEmul(int i, boolean b, String jMeterBinDir, String charsetEncodingHttpResponse, String charsetEncodingReadFile, String charsetEncodingWriteFile, boolean isDemon ) {
        super(i, b, jMeterBinDir, charsetEncodingHttpResponse, charsetEncodingReadFile, charsetEncodingWriteFile, isDemon);
    }

    /*
    public HttpSimpleTableServerEmul() {
    	String jMeterBinDir = TestJMeterUtils.getTempDir();
    	int i = -1;
    	boolean bIsStartup = false;
    	String charset = "UTF-8";
    	boolean bIsDaemon = false;
    	super(i, bIsStartup,  jMeterBinDir, charset, charset, charset, bIsDaemon);
    }
 */   
    protected HttpSimpleTableServerEmul() {
    	super();
    }
    
    
    @Override
    public void waitForKey() {
        log.info("Not waiting for key");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ThreadEmul extends Thread {
        private boolean finished = false;

        public ThreadEmul() {
        }

        @Override
        public void run() {
            while (!finished) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void finish() {
            finished = true;
        }
    }
}
