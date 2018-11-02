package kg.apc.jmeter.timers.functions;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static junit.framework.TestCase.assertEquals;

public class TSTFeedbackTest {
    private static final Logger log = LoggingManager.getLoggerForClass();

    @BeforeClass
    public static void setUpClass() {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void execute() throws InvalidVariableException {
        Collection<CompoundVariable> parameters = new LinkedList<>();
        parameters.add(new CompoundVariable("tstname"));
        parameters.add(new CompoundVariable("1"));
        parameters.add(new CompoundVariable("10"));
        parameters.add(new CompoundVariable("5"));
        TSTFeedback instance = new TSTFeedback();
        instance.setParameters(parameters);


        String result;
        result = instance.execute(null, null);
        log.debug(result);
        assertEquals("1", result);

        JMeterUtils.setProperty("tstname_concurrency", String.valueOf(10));
        JMeterUtils.setProperty("tstname_cntDelayed", String.valueOf(11));
        JMeterUtils.setProperty("tstname_cntSent", String.valueOf(16));
        JMeterUtils.setProperty("tstname_rps", String.valueOf(15.671562));

        result = instance.execute(null, null);
        log.debug(result);
        assertEquals("5", result);

        JMeterUtils.setProperty("tstname_concurrency", String.valueOf(1));
        JMeterUtils.setProperty("tstname_cntDelayed", String.valueOf(5));
        JMeterUtils.setProperty("tstname_cntSent", String.valueOf(16));
        JMeterUtils.setProperty("tstname_rps", String.valueOf(15.671562));

        result = instance.execute(null, null);
        log.debug(result);
        assertEquals("1", result);

        JMeterUtils.setProperty("tstname_concurrency", String.valueOf(10));
        JMeterUtils.setProperty("tstname_cntDelayed", String.valueOf(6));
        JMeterUtils.setProperty("tstname_cntSent", String.valueOf(16));
        JMeterUtils.setProperty("tstname_rps", String.valueOf(15.671562));

        result = instance.execute(null, null);
        log.debug(result);
        assertEquals("9", result);

        JMeterUtils.setProperty("tstname_concurrency", String.valueOf(10));
        JMeterUtils.setProperty("tstname_cntDelayed", String.valueOf(4));
        JMeterUtils.setProperty("tstname_cntSent", String.valueOf(16));
        JMeterUtils.setProperty("tstname_rps", String.valueOf(15.671562));

        result = instance.execute(null, null);
        log.debug(result);
        assertEquals("10", result);
    }
}