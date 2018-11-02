package kg.apc.jmeter.config.redis;

import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.AbstractScopedTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.beans.BeanInfo;
import org.apache.jmeter.testbeans.TestBean;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.Serializable;
import java.util.ResourceBundle;

public class RedisDataWriter extends AbstractScopedTestElement implements TestBean, PostProcessor, Serializable, TestStateListener {

    public enum AddMode {
        LST_RPUSH((byte)0),
        SET_ADD((byte)1);
        private byte value;
        private AddMode(byte value) {
            this.value = value;
        }

        public byte getValue() { return value; }
    }

    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final Integer DEFAULT_PORT = Protocol.DEFAULT_PORT;
    public static final Integer DEFAULT_TIMEOUT = Protocol.DEFAULT_TIMEOUT;
    public static final Integer DEFAULT_DATABASE = Protocol.DEFAULT_DATABASE;

    private String host;
    private String port;
    private String timeout;
    private String password;
    private String database;

    private String redisKey;
    private String variableNames;
    private RedisDataWriter.AddMode addMode;

    private int maxIdle;
    private int minIdle;
    private int maxActive;
    private long maxWait;

    private transient JedisPool pool;


    @Override
    public void process() {
        JMeterContext context = getThreadContext();
        SampleResult previousResult = context.getPreviousResult();
        if (previousResult == null) {
            return;
        }
        log.debug("RedisDataWriter processing result");

        try {
            String redisKey = getRedisKey();
            String redisValue = getVariableNames();

            Jedis connection = this.pool.getResource();
            //System.out.println("testStarted:" + this.pool + "this:" + this + "redisValue:" + redisValue);

            try {
                if (addMode.equals(AddMode.LST_RPUSH)) {
                    connection.rpush(redisKey, redisValue);
                } else {
                    connection.sadd(redisKey, redisValue);
                }
            } finally {
                if (connection != null) {
                    this.pool.returnResource(connection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testEnded() {
        testEnded("");
    }

    @Override
    public void testEnded(String host) {
        pool.destroy();
    }

    @Override
    public void testStarted() {
        testStarted("");
    }

    @Override
    public Object clone() {
        RedisDataWriter clonedElement = (RedisDataWriter)super.clone();
        clonedElement.pool = this.pool;
        return clonedElement;

    }

    /**
     * Override the setProperty method in order to convert
     * the original String calcMode property.
     * This used the locale-dependent display value, so caused
     * problems when the language was changed.
     * Note that the calcMode StringProperty is replaced with an IntegerProperty
     * so the conversion only needs to happen once.
     */
    @Override
    public void setProperty(JMeterProperty property) {
        if (property instanceof StringProperty) {
            final String pn = property.getName();
            if ("getMode".equals(pn)) {
                final Object objectValue = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for(Enum<RedisDataWriter.AddMode> e : RedisDataWriter.AddMode.values()) {
                        final String propName = e.toString();
                        if (objectValue.equals(rb.getObject(propName))) {
                            final int tmpMode = e.ordinal();
                            if (log.isDebugEnabled()) {
                                log.debug("Converted " + pn + "=" + objectValue + " to mode=" + tmpMode  + " using Locale: " + rb.getLocale());
                            }
                            super.setProperty(pn, tmpMode);
                            return;
                        }
                    }
                    log.warn("Could not convert " + pn + "=" + objectValue + " using Locale: " + rb.getLocale());
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            }
        }
        super.setProperty(property);
    }

    @Override
    public void testStarted(String distributedHost) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(getMaxActive());
        config.setMaxIdle(getMaxIdle());
        config.setMinIdle(getMinIdle());
        config.setMaxWaitMillis(getMaxWait()*1000);

        int port = Protocol.DEFAULT_PORT;
        if(!JOrphanUtils.isBlank(this.port)) {
            port = Integer.parseInt(this.port);
        }
        int timeout = Protocol.DEFAULT_TIMEOUT;
        if(!JOrphanUtils.isBlank(this.timeout)) {
            timeout = Integer.parseInt(this.timeout);
        }
        int database = Protocol.DEFAULT_DATABASE;
        if(!JOrphanUtils.isBlank(this.database)) {
            database = Integer.parseInt(this.database);
        }
        String password = null;
        if(!JOrphanUtils.isBlank(this.password)) {
            password = this.password;
        }
        this.pool = new JedisPool(config, this.host, port, timeout, password, database);
        //System.out.println("testStarted:" + this.pool + "this:" + this);
    }


    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the timeout
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * @return the redisKey
     */
    public String getRedisKey() {
        return redisKey;
    }

    /**
     * @param redisKey the redisKey to set
     */
    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    /**
     * @return the variableNames
     */
    public String getVariableNames() {
        return variableNames;
    }

    /**
     * @param variableNames the variableNames to set
     */
    public void setVariableNames(String variableNames) {
        this.variableNames = variableNames;
    }

    /**
     * @return the maxIdle
     */
    public int getMaxIdle() {
        return maxIdle;
    }

    /**
     * @param maxIdle the maxIdle to set
     */
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    /**
     * @return the minIdle
     */
    public int getMinIdle() {
        return minIdle;
    }

    /**
     * @param minIdle the minIdle to set
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * @return the maxActive
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * @param maxActive the maxActive to set
     */
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * @return the maxWait
     */
    public long getMaxWait() {
        return maxWait;
    }

    /**
     * @param maxWait the maxWait to set
     */
    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }


    public int getAddMode() {
        return addMode.ordinal();
    }

    public void setAddMode(int mode) {
        this.addMode = AddMode.values()[mode];
    }
}
