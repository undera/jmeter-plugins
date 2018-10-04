package kg.apc.jmeter.config.redis;

import ai.grakn.redismock.RedisServer;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.junit.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Arrays;

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
        server.stop();
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

        // Confirm that cycling through gets the first "row" of data again
        ds.iterationStart(null);
        ds.iterationStart(null);
        assertEquals("12345678", threadVars.get("ccNum"));
        assertEquals("1234", threadVars.get("ccExpiry"));

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