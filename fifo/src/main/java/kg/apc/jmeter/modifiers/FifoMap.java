package kg.apc.jmeter.modifiers;

import org.apache.jmeter.util.JMeterUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FifoMap extends ConcurrentHashMap<String, BlockingQueue<Object>> {

    public static final String TIMEOUT_PROP = "kg.apc.jmeter.functions.FifoTimeout";
    public static final String CAPACITY_PROP = "kg.apc.jmeter.functions.FifoCapacity";
    private static FifoMap instance = new FifoMap();

    private FifoMap() {
    }

    public static FifoMap getInstance() {
        return instance;
    }

    private BlockingQueue<Object> getFifo(String fifoName) {
        if (super.containsKey(fifoName)) {
            return super.get(fifoName);
        } else {
            BlockingQueue<Object> fifo = new LinkedBlockingQueue<Object>(JMeterUtils.getPropDefault(FifoMap.CAPACITY_PROP, Integer.MAX_VALUE));
            super.put(fifoName, fifo);
            return fifo;
        }
    }

    public Object get(String fifoName) {
        BlockingQueue<Object> fifo = getFifo(fifoName);
        Object value;
        value = fifo.peek();
        return value == null ? "" : value;
    }

    public Object pop(String fifoName, long timeout) throws InterruptedException {
        BlockingQueue<Object> fifo = getFifo(fifoName);
        return fifo.poll(timeout, TimeUnit.SECONDS);
    }

    public Object pop(String fifoName) throws InterruptedException {
        return pop(fifoName, Long.MAX_VALUE);
    }

    public int length(String fifoName) {
        BlockingQueue<Object> fifo = getFifo(fifoName);
        return fifo.size();
    }

    public void put(String fifoName, Object v) throws InterruptedException {
        getFifo(fifoName).offer(v, Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}
