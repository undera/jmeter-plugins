package org.jmeterplugins.protocol.http.control;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;


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


    public HttpSimpleTableServerEmul(int i, boolean b, String jMeterBinDir) {
        super(i, b, jMeterBinDir);
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
