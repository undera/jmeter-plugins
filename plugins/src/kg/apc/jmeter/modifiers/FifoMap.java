package kg.apc.jmeter.modifiers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author undera
 */
public class FifoMap extends ConcurrentHashMap<String, LinkedBlockingDeque<String>> {
    public static final String TIMEOUT_PROP="kg.apc.jmeter.functions.FifoTimeout";
    private static FifoMap instance;

    private FifoMap() {
    }

    public static FifoMap getInstance() {
        if (instance == null) {
            instance = new FifoMap();
        }
        return instance;
    }

    private LinkedBlockingDeque<String> getFifo(String fifoName) {
        if (super.containsKey(fifoName)) {
            return super.get(fifoName);
        } else {
            LinkedBlockingDeque<String> fifo = new LinkedBlockingDeque<String>();
            super.put(fifoName, fifo);
            return fifo;
        }
    }

    public String get(String fifoName) {
        LinkedBlockingDeque<String> fifo = getFifo(fifoName);
        String value = fifo.peekLast();
        return value == null ? "" : value;
    }

    public String pop(String fifoName, long timeout) throws InterruptedException {
        LinkedBlockingDeque<String> fifo = getFifo(fifoName);
        final String value = fifo.pollLast(timeout, TimeUnit.SECONDS);
        return value;
    }

    public void put(String fifoName, String v) throws InterruptedException {
        getFifo(fifoName).offerFirst(v, Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}
