package com.blazemeter.jmeter;


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