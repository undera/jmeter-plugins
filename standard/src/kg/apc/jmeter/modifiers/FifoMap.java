package kg.apc.jmeter.modifiers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author undera
 */
public class FifoMap extends ConcurrentHashMap<String, BlockingQueue<String>> {

    public static final String TIMEOUT_PROP = "kg.apc.jmeter.functions.FifoTimeout";
    public static final String CAPACITY_PROP = "kg.apc.jmeter.functions.FifoCapacity";
    private static FifoMap instance = new FifoMap();

    private FifoMap() {
    }

    public static FifoMap getInstance() {
        return instance;
    }

    private BlockingQueue<String> getFifo(String fifoName) {
        if (super.containsKey(fifoName)) {
            return super.get(fifoName);
        } else {
            BlockingQueue<String> fifo = new LinkedBlockingQueue<String>(JMeterUtils.getPropDefault(FifoMap.TIMEOUT_PROP, Integer.MAX_VALUE));
            super.put(fifoName, fifo);
            return fifo;
        }
    }

    public String get(String fifoName) {
        BlockingQueue<String> fifo = getFifo(fifoName);
        String value;
        value = fifo.peek();
        return value == null ? "" : value;
    }

    public String pop(String fifoName, long timeout) throws InterruptedException {
        BlockingQueue<String> fifo = getFifo(fifoName);
        final String value = fifo.poll(timeout, TimeUnit.SECONDS);
        return value;
    }

    public int length(String fifoName) {
        BlockingQueue<String> fifo = getFifo(fifoName);
        final int size = fifo.size();
        return size;
    }

    public void put(String fifoName, String v) throws InterruptedException {
        getFifo(fifoName).offer(v, Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}
