package kg.apc.jmeter.config.redis;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BeanInfo for {@link RedisDataSet}
 */
public class RedisDataSetBeanInfo extends BeanInfoSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDataSetBeanInfo.class);

    // These names must agree case-wise with the variable and property names
    private static final String VARIABLE_NAMES = "variableNames";
    private static final String DELIMITER = "delimiter";
    private static final String REDIS_KEY = "redisKey";
    private static final String REDIS_DATA_TYPE = "redisDataType";
    private static final String RECYCLE_DATA_ON_USE = "recycleDataOnUse";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String SSL = "ssl";
    private static final String TIMEOUT = "timeout";
    private static final String PASSWORD = "password";
    private static final String DATABASE = "database";
    private static final String MAX_ACTIVE = "maxActive";
    private static final String MAX_IDLE = "maxIdle";
    private static final String MAX_WAIT = "maxWait";
    private static final String MIN_IDLE = "minIdle";
    private static final String BLOCK_WHEN_EXHAUSTED = "blockWhenExhausted";
    private static final String TEST_WHILE_IDLE = "testWhileIdle";
    private static final String TEST_ON_BORROW = "testOnBorrow";
    private static final String TEST_ON_RETURN = "testOnReturn";
    private static final String TIME_BETWEEN_EVICTION_RUNS_MS = "timeBetweenEvictionRunsMillis";
    private static final String SOFT_MIN_EVICTABLE_IDLE_TIME_MS = "softMinEvictableIdleTimeMillis";
    private static final String NUM_TEST_PER_EVICTION_RUN = "numTestsPerEvictionRun";
    private static final String MIN_EVICTABLE_IDLE_TIME_MS = "minEvictableIdleTimeMillis";

    public RedisDataSetBeanInfo() {
        super(RedisDataSet.class);
        try {
            createRedisDataProperties();
            createRedisConnectionProperties();
            createPoolConfigProperties();
        } catch (NoSuchMethodError e) {
            LOGGER.error("Error initializing component GraphGeneratorListener due to missing method, if your version is lower than 2.10, this is expected to fail, if not check project dependencies");
        }
    }

    private void createRedisDataProperties() {
        createPropertyGroup("redis_data",
                new String[]{REDIS_KEY, VARIABLE_NAMES, DELIMITER, REDIS_DATA_TYPE, RECYCLE_DATA_ON_USE});

        createAndSetProperty(REDIS_KEY, "", Boolean.TRUE);
        createAndSetProperty(VARIABLE_NAMES, "", Boolean.TRUE);
        createAndSetProperty(DELIMITER, ",", Boolean.TRUE);
        createAndSetProperty(RECYCLE_DATA_ON_USE, Boolean.TRUE, Boolean.TRUE);
        createAndSetProperty(REDIS_DATA_TYPE, RedisDataSet.RedisDataType.REDIS_DATA_TYPE_LIST.ordinal(), Boolean.TRUE);
    }

    private void createRedisConnectionProperties() {
        createPropertyGroup("redis_connection",
                new String[]{HOST, PORT, SSL, TIMEOUT, PASSWORD, DATABASE});

        createAndSetProperty(HOST, "", Boolean.TRUE);
        createAndSetProperty(PORT, RedisDataSet.DEFAULT_PORT, Boolean.TRUE);
        createAndSetProperty(SSL, Boolean.FALSE, Boolean.TRUE);
        createAndSetProperty(TIMEOUT, RedisDataSet.DEFAULT_TIMEOUT, Boolean.TRUE);
        createAndSetProperty(PASSWORD, "", Boolean.TRUE);
        createAndSetProperty(DATABASE, RedisDataSet.DEFAULT_DATABASE, Boolean.TRUE);
    }

    private void createPoolConfigProperties() {
        createPropertyGroup("pool_config",
                new String[]{MIN_IDLE, MAX_IDLE, MAX_ACTIVE, MAX_WAIT, BLOCK_WHEN_EXHAUSTED,
                        TEST_ON_BORROW, TEST_ON_RETURN, TEST_WHILE_IDLE, TIME_BETWEEN_EVICTION_RUNS_MS,
                        NUM_TEST_PER_EVICTION_RUN, MIN_EVICTABLE_IDLE_TIME_MS, SOFT_MIN_EVICTABLE_IDLE_TIME_MS});

        createAndSetProperty(MIN_IDLE, RedisDataSet.DEFAULT_MIN_IDLE, Boolean.TRUE);
        createAndSetProperty(MAX_IDLE, RedisDataSet.DEFAULT_MAX_IDLE, Boolean.TRUE);
        createAndSetProperty(MAX_ACTIVE, RedisDataSet.DEFAULT_MAX_ACTIVE, Boolean.TRUE);
        createAndSetProperty(MAX_WAIT, "30000", Boolean.TRUE);
        createAndSetProperty(BLOCK_WHEN_EXHAUSTED, Boolean.FALSE, Boolean.TRUE);
        createAndSetProperty(TEST_ON_BORROW, Boolean.FALSE, Boolean.TRUE);
        createAndSetProperty(TEST_ON_RETURN, Boolean.FALSE, Boolean.TRUE);
        createAndSetProperty(TEST_WHILE_IDLE, Boolean.FALSE, Boolean.TRUE);
        createAndSetProperty(TIME_BETWEEN_EVICTION_RUNS_MS, "30000", Boolean.TRUE);
        createAndSetProperty(NUM_TEST_PER_EVICTION_RUN, "0", Boolean.TRUE);
        createAndSetProperty(MIN_EVICTABLE_IDLE_TIME_MS, "60000", Boolean.TRUE);
        createAndSetProperty(SOFT_MIN_EVICTABLE_IDLE_TIME_MS, "60000", Boolean.TRUE);
    }

    private void createAndSetProperty(String propertyName, Object defaultValue, Boolean notUndefined) {
        PropertyDescriptor p = property(propertyName);
        p.setValue(NOT_UNDEFINED, notUndefined);
        p.setValue(DEFAULT, defaultValue);
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    }

    private void createAndSetProperty(String propertyName, Object defaultValue, Boolean notUndefined, Boolean notOther) {
        PropertyDescriptor p = property(propertyName);
        p.setValue(NOT_UNDEFINED, notUndefined);
        p.setValue(DEFAULT, defaultValue);
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        p.setValue(NOT_OTHER, notOther);
    }
}
