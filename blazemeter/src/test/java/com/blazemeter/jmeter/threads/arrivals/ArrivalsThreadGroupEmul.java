package com.blazemeter.jmeter.threads.arrivals;

import com.blazemeter.jmeter.control.VirtualUserController;
import org.apache.jmeter.sampler.DebugSampler;

public class ArrivalsThreadGroupEmul extends ArrivalsThreadGroup {
    public ArrivalsThreadGroupEmul() {
        super();
        VirtualUserController vuc = new VirtualUserController();
        vuc.addTestElement(new DebugSampler());
        addTestElement(vuc);
    }

    public long getArrivalsDone() {
        return arrivalsCount.longValue();
    }
}
