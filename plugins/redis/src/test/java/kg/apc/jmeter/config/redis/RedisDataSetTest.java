package kg.apc.jmeter.config.redis;


import com.github.fppt.jedismock.RedisServer;
import com.github.fppt.jedismock.datastructures.Slice;
import com.github.fppt.jedismock.operations.server.MockExecutor;
import com.github.fppt.jedismock.server.RedisCommandInterceptor;
import com.github.fppt.jedismock.server.Response;
import com.github.fppt.jedismock.server.ServiceOptions;
import com.github.fppt.jedismock.storage.OperationExecutorState;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.junit.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RedisDataSetTest {

    private RedisServer server;
    private JMeterVariables threadVars;

    public RedisDataSetTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {

    }

    @Before
    public void setUp() throws IOException {
        // Set up thread variables
        JMeterContext jmcx = JMeterContextService.getContext();
        jmcx.setVariables(new JMeterVariables());
        threadVars = jmcx.getVariables();


        // Create new mock RedisServer
        server = RedisServer.newRedisServer(6370);
        server.setOptions(ServiceOptions.withInterceptor(new RedisCommandInterceptor() {
            @Override
            public Slice execCommand(OperationExecutorState operationExecutorState, String s, List<Slice> list) {
                Slice response;
                Slice record;
                if ("lmove".equalsIgnoreCase(s)) {
                    Slice from = list.get(0);
                    Slice to = list.get(1);
                    record = MockExecutor.proceed(operationExecutorState, "lpop", Arrays.<Slice>asList(from));
                    MockExecutor.proceed(operationExecutorState, "rpush", Arrays.<Slice>asList(to, record));

                    response = Response.bulkString(Slice.create(record.toString().replaceAll("\\$\\d*", "").trim()));
                } else if ("srandmember".equalsIgnoreCase(s)) {
                    Slice from = list.get(0);
                    record = MockExecutor.proceed(operationExecutorState, "spop",Arrays.<Slice>asList(from));
                    MockExecutor.proceed(operationExecutorState, "sadd", Arrays.<Slice>asList(from, record));
                    response  = Response.bulkString(Slice.create(record.toString().replaceAll("\\$\\d*", "").trim()));
                } else {
                    record = MockExecutor.proceed(operationExecutorState, s, list);
                    response = record;
                }
                // Return error on empty list
                if ("$-1".equals(record.toString().trim())) {
                    //null response
                    response = Response.error("End of list");
                }
                return response;
            }
        }));
        server.start();

        // Setup test data
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost", 6370);
        Jedis jedis = pool.getResource();

        jedis.rpush("testRecycleList", "12345678,1234","12345679,1235", "12345680,1236");
        jedis.rpush("testConsumeList", "12345678,1234","12345679,1235", "12345680,1236");
        jedis.sadd("testRecycleSet", "12345678,1234","12345679,1235", "12345680,1236");
        jedis.sadd("testConsumeSet", "12345678,1234","12345679,1235", "12345680,1236");

        jedis.shutdown();

    }

    @After
    public void tearDown() {
        try {
            server.stop();
        } catch (IOException e) {
            System.err.println("Failed to stop the redis mock");;
        }
    }

    @Test
    public void testGetRecycleDataFromList() {
        RedisDataSet ds = new RedisDataSet();
        ds.setPort("6370");
        ds.setRedisKey("testRecycleList");
        ds.setVariableNames("ccNum,ccExpiry");
        ds.setDelimiter(",");

        ds.testStarted();
        ds.iterationStart(null);

        // FIXME TODO dbSize() not supported by mocking framework
        // assertTrue(jedis.dbSize() == 3);

        assertEquals("12345678", threadVars.get("ccNum"));
        assertEquals("1234", threadVars.get("ccExpiry"));

        // Confirm next iteration gets next "row" of data
        ds.iterationStart(null);
        assertEquals("12345679", threadVars.get("ccNum"));
        assertEquals("1235", threadVars.get("ccExpiry"));

        ds.testEnded(null);
    }

    @Test
    public void testGetConsumeDataFromList() {
        RedisDataSet ds = new RedisDataSet();
        ds.setPort("6370");
        ds.setRedisKey("testConsumeList");
        ds.setVariableNames("ccNum,ccExpiry");
        ds.setDelimiter(",");
        ds.setRecycleDataOnUse(false);

        ds.testStarted();
        ds.iterationStart(null);

        assertEquals("12345678", threadVars.get("ccNum"));
        assertEquals("1234", threadVars.get("ccExpiry"));

        // Confirm next iteration gets next "row" of data
        ds.iterationStart(null);
        assertEquals("12345679", threadVars.get("ccNum"));
        assertEquals("1235", threadVars.get("ccExpiry"));

        // Confirm that cycling through should throw a JMeterStopThreadException now all data is consumed
        boolean bExceptionCaught = false;
        ds.iterationStart(null);
        try {
            ds.iterationStart(null);
        } catch (JMeterStopThreadException e) {
            bExceptionCaught = true;
        }
        assertTrue(bExceptionCaught);

        ds.testEnded();
    }

    @Test
    public void testGetRecycleDataFromSet() {
        RedisDataSet ds = new RedisDataSet();
        ds.setPort("6370");
        ds.setRedisKey("testRecycleSet");
        ds.setVariableNames("ccNum,ccExpiry");
        ds.setDelimiter(",");
        ds.setRecycleDataOnUse(true);
        ds.setRedisDataType(RedisDataSet.RedisDataType.REDIS_DATA_TYPE_SET.ordinal());

        ds.testStarted();

        // Consume all data
        ds.iterationStart(null);
        ds.iterationStart(null);
        ds.iterationStart(null);

        ds.iterationStart(null);

        final String[] expectedNums = new String[] {"12345678", "12345679", "12345680"};
        assertTrue(Arrays.asList(expectedNums).contains(threadVars.get("ccNum")));

        ds.testEnded();
    }

    @Test
    public void testGetConsumeDataFromSet() {
        RedisDataSet ds = new RedisDataSet();
        ds.setPort("6370");
        ds.setRedisKey("testConsumeSet");
        ds.setVariableNames("ccNum,ccExpiry");
        ds.setDelimiter(",");
        ds.setRecycleDataOnUse(false);
        ds.setRedisDataType(RedisDataSet.RedisDataType.REDIS_DATA_TYPE_SET.ordinal());

        ds.testStarted();

        // Consume all data
        ds.iterationStart(null);
        ds.iterationStart(null);
        ds.iterationStart(null);

        // Confirm that cycling through should throw a JMeterStopThreadException now all data is consumed
        boolean bExceptionCaught = false;
        try {
            ds.iterationStart(null);
        } catch (JMeterStopThreadException e) {
            bExceptionCaught = true;
        }
        assertTrue(bExceptionCaught);

        ds.testEnded();
    }

    @Test(expected = JMeterStopThreadException.class)
    public void testDataExhaustionThrowsJMeterStopThreadException() {
        RedisDataSet ds = new RedisDataSet();

        ds.setPort("6370");
        ds.setRedisKey("testKey");

        ds.testStarted();
        ds.iterationStart(null);
        ds.testEnded();
    }
}