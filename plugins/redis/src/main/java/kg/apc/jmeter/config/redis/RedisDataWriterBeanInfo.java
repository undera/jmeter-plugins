package kg.apc.jmeter.config.redis;


import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.beans.PropertyDescriptor;

/**
 * BeanInfo for {@link RedisDataWriter}
 */
public class RedisDataWriterBeanInfo extends BeanInfoSupport {

    private static final Logger LOGGER = LoggingManager.getLoggerForClass();

    private static final String VARIABLE_NAMES = "variableNames";
    private static final String REDIS_KEY = "redisKey";
    private static final String ADD_MODE = "addMode";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String TIMEOUT = "timeout";
    private static final String PASSWORD = "password";
    private static final String DATABASE = "database";

    private static final String MAX_ACTIVE = "maxActive";
    private static final String MAX_IDLE = "maxIdle";
    private static final String MAX_WAIT = "maxWait";
    private static final String MIN_IDLE = "minIdle";


    public RedisDataWriterBeanInfo() {
        super(RedisDataWriter.class);
        try {
            createPropertyGroup("redis_data",
                    new String[] { REDIS_KEY, VARIABLE_NAMES, ADD_MODE });

            PropertyDescriptor p = property(REDIS_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(VARIABLE_NAMES);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(ADD_MODE, RedisDataWriter.AddMode.class);
            p.setValue(DEFAULT, RedisDataWriter.AddMode.LST_RPUSH.ordinal());
            p.setValue(NOT_UNDEFINED, Boolean.TRUE); // must be defined


            createPropertyGroup("redis_connection",
                    new String[] { HOST, PORT, TIMEOUT, PASSWORD, DATABASE });

            p = property(HOST);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(PORT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, RedisDataSet.DEFAULT_PORT);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(TIMEOUT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, RedisDataSet.DEFAULT_TIMEOUT);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(PASSWORD);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(DATABASE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, RedisDataSet.DEFAULT_DATABASE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);


            createPropertyGroup("pool_config",
                    new String[] { MIN_IDLE, MAX_IDLE, MAX_ACTIVE, MAX_WAIT});


            p = property(MIN_IDLE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "0");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(MAX_IDLE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "10");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(MAX_ACTIVE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "500");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(MAX_WAIT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "30000");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        } catch (NoSuchMethodError e) {
            LOGGER.error("Error initializing component GraphGeneratorListener due to missing method, if your version is lower than 2.10, this" +
                    "is expected to fail, if not check project dependencies");
        }
    }
}
