package com.blazemeter.jmeter;

import static org.junit.Assert.*;

/**
 *
 */
public class StatusNotifierCallbackTest implements StatusNotifierCallback{

    private StringBuilder buffer = new StringBuilder();

    @Override
    public void notifyAbout(String info) {
        buffer.append(info);
    }

    public StringBuilder getBuffer() {
        return buffer;
    }
}