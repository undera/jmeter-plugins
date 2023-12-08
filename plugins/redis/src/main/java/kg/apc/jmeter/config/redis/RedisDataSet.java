/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package kg.apc.jmeter.config.redis;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JsseSSLManager;
import org.apache.jmeter.util.SSLManager;
import org.slf4j.LoggerFactory;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.args.ListDirection;

/**
 * Redis DataSet using a Redis Set or List.
 * @since 2.11
 */
public class RedisDataSet extends ConfigTestElement
        implements TestBean, LoopIterationListener, NoConfigMerge, TestStateListener {


    private interface PollingStrategy {
        public String getDataFromConnection(Jedis conn, String key) throws JedisDataException;
    }

    /**
     *  Pops an element from a redis list from right (LIFO)
     */
    private class PopFromListStrategy implements PollingStrategy {
        @Override
        public String getDataFromConnection(Jedis conn, String key) throws JedisDataException {
            log.debug("Executing lpop against redis list");
            String line = conn.lpop(key);
            return line;
        }
    }

    private class QueueStrategy implements PollingStrategy {
        @Override
        public String getDataFromConnection(Jedis conn, String key) throws JedisDataException {
            log.debug("Executing lmove against redis list");
            String line = conn.lmove(key, key, ListDirection.RIGHT, ListDirection.LEFT);
            return line;
        }
    }
    private class PopFromSetStrategy implements PollingStrategy {
        @Override
        public String getDataFromConnection(Jedis conn, String key) throws JedisDataException {
            log.debug("Executing spop against redis set");
            String line = conn.spop(key);
            return line;
        }
    }

    private class CopyFromSetStrategy implements PollingStrategy {
        @Override
        public String getDataFromConnection(Jedis conn, String key) throws JedisDataException {
            log.debug("Executing srandmember against redis set");
            String line = conn.srandmember(key);
            return line;
        }
    }

    // backwards compatible (Redis versions below 6.2)
    private class PopAndPushBackListStrategy implements PollingStrategy {
        @Override
        public String getDataFromConnection(Jedis conn, String key) throws JedisDataException {
            log.debug("Executing lpop against redis list");
            String line = conn.lpop(key);
            log.debug("Executing rpush against redis list");
            conn.rpush(key, line);
            return line;
        }
    }

    private class PopAndPushBackSetStrategy implements PollingStrategy {
        @Override
        public String getDataFromConnection(Jedis conn, String key) throws JedisDataException {
            log.debug("Executing spop against redis set");
            String line = conn.spop(key);
            log.debug("Executign sadd against redis set");
            conn.sadd(key, line);
            return line;
        }
    }

    public enum RedisDataType {
        REDIS_DATA_TYPE_LIST((byte)0),
        REDIS_DATA_TYPE_SET((byte)1);

        private byte value;
        RedisDataType(byte value) { this.value = value; }
        public int getValue() { return value; }
    }

    private PollingStrategy selectPollingStrategy(){

        switch (redisDataType) {
            case REDIS_DATA_TYPE_SET:
                if (!getRecycleDataOnUse()) {
                    return new PopFromSetStrategy();
                } else {
                    if (getPropertyAsBoolean("plugins.redis.legacy", false)) {
                        return new PopAndPushBackSetStrategy();
                    }
                    return new CopyFromSetStrategy();
                }
            case REDIS_DATA_TYPE_LIST:
                if (!getRecycleDataOnUse()) {
                    return new PopFromListStrategy();
                } else {
                    if (getPropertyAsBoolean("plugins.redis.legacy", false)){
                        return new PopAndPushBackListStrategy();
                    }
                    return new QueueStrategy();
                }
            default:
                log.error("Redis configuration not supported!");
                break;
        }
        return null;
    }

    private static final long serialVersionUID = 7383500755324202605L;

    private static final Logger log = LoggerFactory.getLogger(RedisDataSet.class);

    public static final Integer DEFAULT_PORT = Protocol.DEFAULT_PORT;
    public static final Integer DEFAULT_TIMEOUT = Protocol.DEFAULT_TIMEOUT;
    public static final Integer DEFAULT_DATABASE = Protocol.DEFAULT_DATABASE;
    public static final Integer DEFAULT_MAX_ACTIVE = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;
    public static final Integer DEFAULT_MAX_IDLE = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;
    public static final Integer DEFAULT_MIN_IDLE = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

    /* Connection configuration */
    private String host = "localhost";
    private String port = String.valueOf(DEFAULT_PORT);
    private boolean ssl = false;
    private String timeout = String.valueOf(DEFAULT_TIMEOUT);
    private String password;
    private String database = String.valueOf(DEFAULT_DATABASE);

    /* Data configuration */
    private String redisKey;
    private String variableNames;
    private String delimiter = ",";
    private boolean recycleDataOnUse = true;
    private RedisDataType redisDataType = RedisDataType.REDIS_DATA_TYPE_LIST;

    /* Pool configuration */
    private int maxIdle = DEFAULT_MAX_IDLE;
    private int minIdle = DEFAULT_MIN_IDLE;
    private int maxActive = DEFAULT_MAX_ACTIVE;
    private long maxWait;
    private boolean blockWhenExhausted;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean testWhileIdle;
    private long timeBetweenEvictionRunsMillis;
    private int numTestsPerEvictionRun;
    private long minEvictableIdleTimeMillis;
    private long softMinEvictableIdleTimeMillis;
    private transient String[] vars;

    private transient JedisPool pool;

    private PollingStrategy pollingStrategy;


    @Override
    public void iterationStart(LoopIterationEvent event) {
        Jedis connection = null;
        if (null == pollingStrategy) {
            pollingStrategy = selectPollingStrategy();
        }
        try {
            connection = pool.getResource();
            String line = null;
            // Get data from list's head
            try {
                line = pollingStrategy.getDataFromConnection(connection, redisKey);
            } catch (JedisDataException jde) {
                log.error("Failed to retrieve data from redis key {}", redisKey);
            }
            if (null == line) {
                throw new JMeterStopThreadException("End of redis data detected");
            }
            final String names = variableNames;
            if (vars == null) {
                vars = JOrphanUtils.split(names, ",");
            }

            final JMeterContext context = getThreadContext();
            JMeterVariables threadVars = context.getVariables();
            String[] values = JOrphanUtils.split(line, delimiter, false);
            for (int a = 0; a < vars.length && a < values.length; a++) {
                threadVars.put(vars[a], values[a]);
            }

        } finally {
            pool.returnResource(connection);
        }
    }

    @Override
    public void testEnded() {
        testEnded("");
    }

    @Override
    public void testEnded(String host) {
        int idleConn = pool.getNumIdle();
        for(int i=0;i<idleConn;i++){
           pool.getResource();
        }
        pool.destroy();
    }

    @Override
    public void testStarted() {
        testStarted("");
    }

    @Override
    public Object clone() {
        RedisDataSet clonedElement = (RedisDataSet)super.clone();
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
            if (pn.equals("redisDataType")) {
                final Object objectValue = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for(Enum<RedisDataType> e : RedisDataType.values()) {
                        final String propName = e.toString();
                        if (objectValue.equals(rb.getObject(propName))) {
                            final int tmpType = e.ordinal();
                            if (log.isDebugEnabled()) {
                                log.debug("Converted " + pn + "=" + objectValue + " to data type=" + tmpType  + " using Locale: " + rb.getLocale());
                            }
                            super.setProperty(pn, tmpType);
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
        config.setMaxWaitMillis(getMaxWait());
        config.setBlockWhenExhausted(getBlockWhenExhausted());
        config.setTestOnBorrow(getTestOnBorrow());
        config.setTestOnReturn(getTestOnReturn());
        config.setTestWhileIdle(getTestWhileIdle());
        config.setTimeBetweenEvictionRunsMillis(getTimeBetweenEvictionRunsMillis());
        config.setNumTestsPerEvictionRun(getNumTestsPerEvictionRun());
        config.setMinEvictableIdleTimeMillis(getMinEvictableIdleTimeMillis());
        config.setSoftMinEvictableIdleTimeMillis(getSoftMinEvictableIdleTimeMillis());

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
        SSLSocketFactory sslSocketFactory = null;
        SSLParameters sslParameters = null;
        HostnameVerifier hostnameVerifier = null;
        if (this.ssl) {
            SSLManager sslManager = SSLManager.getInstance();
            try {
                SSLContext sslContext = ((JsseSSLManager)sslManager).getContext();
                sslSocketFactory = sslContext.getSocketFactory();
                sslParameters = sslContext.getDefaultSSLParameters();
                hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            }catch(GeneralSecurityException ex) {
                throw new IllegalStateException("Unable to get SSLContext from SSLManager", ex);
            }
        }
        this.pool = new JedisPool(config, this.host, port, timeout, password, database, this.ssl, sslSocketFactory, sslParameters, hostnameVerifier);

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
     * @return the ssl
     */
    public boolean getSsl() {
        return ssl;
    }

    /**
     * @param ssl the ssl to set
     */
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
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
     * @return the delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * @param delimiter the delimiter to set
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
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

    /**
     * @return the blockWhenExhausted
     */
    public boolean getBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    /**
     * @param blockWhenExhausted the blockWhenExhausted to set
     */
    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    /**
     * @return the testOnBorrow
     */
    public boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    /**
     * @param testOnBorrow the testOnBorrow to set
     */
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    /**
     * @return the testOnReturn
     */
    public boolean getTestOnReturn() {
        return testOnReturn;
    }

    /**
     * @param testOnReturn the testOnReturn to set
     */
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    /**
     * @return the testWhileIdle
     */
    public boolean getTestWhileIdle() {
        return testWhileIdle;
    }

    /**
     * @param testWhileIdle the testWhileIdle to set
     */
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    /**
     * @return the timeBetweenEvictionRunsMillis
     */
    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    /**
     * @param timeBetweenEvictionRunsMillis the timeBetweenEvictionRunsMillis to set
     */
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * @return the numTestsPerEvictionRun
     */
    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    /**
     * @param numTestsPerEvictionRun the numTestsPerEvictionRun to set
     */
    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    /**
     * @return the minEvictableIdleTimeMillis
     */
    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    /**
     * @param minEvictableIdleTimeMillis the minEvictableIdleTimeMillis to set
     */
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * @return the softMinEvictableIdleTimeMillis
     */
    public long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    /**
     * @param softMinEvictableIdleTimeMillis the softMinEvictableIdleTimeMillis to set
     */
    public void setSoftMinEvictableIdleTimeMillis(
            long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    /**
     *
     * @return
     */
    public boolean getRecycleDataOnUse() {
        return recycleDataOnUse;
    }

    /**
     *
     * @param recycleDataOnUse
     */
    public void setRecycleDataOnUse(boolean recycleDataOnUse) {
        this.recycleDataOnUse = recycleDataOnUse;
    }

    /**
     *
     * @return
     */
    public int getRedisDataType() {
        return redisDataType.ordinal();
    }

    /**
     *
     * @param dataType
     */
    public void setRedisDataType(int dataType) {
        try {
            this.redisDataType = RedisDataType.values()[dataType];
        } catch (ArrayIndexOutOfBoundsException e) {
            // Default to List
            this.redisDataType = RedisDataSet.RedisDataType.REDIS_DATA_TYPE_LIST;

        }
    }
}
