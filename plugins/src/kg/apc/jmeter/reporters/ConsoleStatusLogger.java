// TODO: work out clear distributed mode behavior
// TODO: limit precision for error rate
package kg.apc.jmeter.reporters;

import java.io.PrintStream;
import java.io.Serializable;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
public class ConsoleStatusLogger extends AbstractListenerElement
        implements SampleListener, Serializable,
        NoThreadClone, TestListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private PrintStream out;
    private long cur = 0;
    private int count;
    private int threads;
    private int sumRTime;
    private int sumLatency;
    private int errors;
    private long begin;

    private class JMeterLoggerOutputStream extends PrintStream {

        public JMeterLoggerOutputStream(Logger log) {
            super(System.out);
        }

        @Override
        public void println(String msg) {
            log.info(msg);
        }
    }

    @Override
    public synchronized void sampleOccurred(SampleEvent se) {
        // TODO: make the interval configurable
        long sec = System.currentTimeMillis() / 1000;
        if (sec != cur && count > 0) {
            if (cur == 0) {
                begin = sec;
            }

            log.debug(cur + " " + begin);
            flush(sec - begin);
            cur = sec;
        }
        SampleResult res = se.getResult();

        count++;
        sumRTime += res.getTime();
        sumLatency += res.getLatency();
        errors += res.isSuccessful() ? 0 : 1;
        threads = res.getAllThreads();
    }

    private void flush(long sec) {
        String msg = '#' + Long.toString(sec) + '\t';
        msg += "Threads: " + threads + '/' + JMeterContextService.getTotalThreads() + '\t';
        msg += "Samples: " + count + '\t';
        msg += "Latency: " + sumLatency / (count > 0 ? count : 1) + '\t';
        msg += "Resp.Time: " + sumRTime / (count > 0 ? count : 1) + '\t';
        msg += "Errors: " + errors;
        out.println(msg);

        count = 0;
        sumRTime = 0;
        sumLatency = 0;
        errors = 0;
    }

    @Override
    public void sampleStarted(SampleEvent se) {
    }

    @Override
    public void sampleStopped(SampleEvent se) {
    }

    @Override
    public void testStarted() {
        if (JMeter.isNonGUI()) {
            out = System.out;
        } else {
            out = new JMeterLoggerOutputStream(log);
        }
        cur = 0;
    }

    @Override
    public void testStarted(String string) {
        testStarted();
    }

    @Override
    public void testEnded() {
    }

    @Override
    public void testEnded(String string) {
    }

    @Override
    public void testIterationStart(LoopIterationEvent lie) {
    }
}
