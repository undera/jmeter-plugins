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

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * BeanInfo for {@link RedisDataSet}
 */
public class RedisDataSetBeanInfo extends BeanInfoSupport {
    private static final Logger LOGGER = LoggingManager.getLoggerForClass();

    // These names must agree case-wise with the variable and property names
    private static final String VARIABLE_NAMES = "variableNames";    //$NON-NLS-1$
    private static final String DELIMITER = "delimiter";             //$NON-NLS-1$
    private static final String REDIS_KEY = "redisKey";             //$NON-NLS-1$
    private static final String GET_MODE = "getMode";               //$NON-NLS-1$
    private static final String HOST = "host";             //$NON-NLS-1$
    private static final String PORT = "port";             //$NON-NLS-1$
    private static final String TIMEOUT = "timeout";             //$NON-NLS-1$
    private static final String PASSWORD = "password";             //$NON-NLS-1$
    private static final String DATABASE = "database";             //$NON-NLS-1$
    
    private static final String MAX_ACTIVE = "maxActive";                 //$NON-NLS-1$
    private static final String MAX_IDLE = "maxIdle";                 //$NON-NLS-1$
    private static final String MAX_WAIT = "maxWait";                 //$NON-NLS-1$
    private static final String MIN_IDLE = "minIdle";                 //$NON-NLS-1$
    private static final String WHEN_EXHAUSTED_ACTION = "whenExhaustedAction";                 //$NON-NLS-1$
    private static final String TEST_WHILE_IDLE = "testWhileIdle";                 //$NON-NLS-1$
    private static final String TEST_ON_BORROW = "testOnBorrow";                 //$NON-NLS-1$
    private static final String TEST_ON_RETURN = "testOnReturn";                 //$NON-NLS-1$
    private static final String TIME_BETWEEN_EVICTION_RUNS_MS = "timeBetweenEvictionRunsMillis";                 //$NON-NLS-1$
    private static final String SOFT_MIN_EVICTABLE_IDLE_TIME_MS = "softMinEvictableIdleTimeMillis";                 //$NON-NLS-1$
    private static final String NUM_TEST_PER_EVICTION_RUN = "numTestsPerEvictionRun";                 //$NON-NLS-1$
    private static final String MIN_EVICTABLE_IDLE_TIME_MS = "minEvictableIdleTimeMillis";                 //$NON-NLS-1$
    
//    // Access needed from CSVDataSet
//    static final String[] GET_MODE_TAGS = new String[3];
//    static final int GET_MODE_REMOVE    = 0;
//    static final int GET_MODE_KEEP  = 1;
//
//    // Store the resource keys
//    static {
//        GET_MODE_TAGS[GET_MODE_REMOVE]    = "getMode.remove"; //$NON-NLS-1$
//        GET_MODE_TAGS[GET_MODE_KEEP]  = "getMode.keep"; //$NON-NLS-1$
//    }
    
    public RedisDataSetBeanInfo() {
        super(RedisDataSet.class);
        try {
            createPropertyGroup("redis_data",             //$NON-NLS-1$
                    new String[] { REDIS_KEY, VARIABLE_NAMES, DELIMITER, GET_MODE });
    
            PropertyDescriptor p = property(REDIS_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    
            p = property(VARIABLE_NAMES);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    
            p = property(DELIMITER);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, ",");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            
    //        p = property(GET_MODE, TypeEditor.ComboStringEditor);
    //        p.setValue(RESOURCE_BUNDLE, getBeanDescriptor().getValue(RESOURCE_BUNDLE));
    //        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
    //        p.setValue(DEFAULT, GET_MODE_TAGS[GET_MODE_REMOVE]);
    //        p.setValue(NOT_OTHER, Boolean.FALSE);
    //        p.setValue(NOT_EXPRESSION, Boolean.FALSE);
    //        p.setValue(TAGS, GET_MODE_TAGS);
            
            p = property(GET_MODE, RedisDataSet.GetMode.class); //$NON-NLS-1$
            p.setValue(DEFAULT, RedisDataSet.GetMode.RANDOM_REMOVE.ordinal());
            p.setValue(NOT_UNDEFINED, Boolean.TRUE); // must be defined
    
            
            createPropertyGroup("redis_connection",             //$NON-NLS-1$
                    new String[] { HOST, PORT, TIMEOUT, PASSWORD, DATABASE });
    
            p = property(HOST);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");        //$NON-NLS-1$
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
            p.setValue(DEFAULT, ""); //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            
            p = property(DATABASE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, RedisDataSet.DEFAULT_DATABASE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE); 
            
            
            createPropertyGroup("pool_config",             //$NON-NLS-1$
                    new String[] { MIN_IDLE, MAX_IDLE, MAX_ACTIVE, MAX_WAIT, WHEN_EXHAUSTED_ACTION,
                        TEST_ON_BORROW, TEST_ON_RETURN, TEST_WHILE_IDLE, TIME_BETWEEN_EVICTION_RUNS_MS,
                        NUM_TEST_PER_EVICTION_RUN, MIN_EVICTABLE_IDLE_TIME_MS, SOFT_MIN_EVICTABLE_IDLE_TIME_MS});
            
            
            p = property(MIN_IDLE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "0");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            
            p = property(MAX_IDLE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "10");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    
            p = property(MAX_ACTIVE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "20");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    
            p = property(MAX_WAIT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "30000");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    
            p = property(WHEN_EXHAUSTED_ACTION, RedisDataSet.WhenExhaustedAction.class); //$NON-NLS-1$
            p.setValue(DEFAULT, RedisDataSet.WhenExhaustedAction.GROW.ordinal());
            p.setValue(NOT_UNDEFINED, Boolean.TRUE); // must be defined
            
            p = property(TEST_ON_BORROW);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
    
            p = property(TEST_ON_RETURN);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
            
            p = property(TEST_WHILE_IDLE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
    
            
            p = property(TIME_BETWEEN_EVICTION_RUNS_MS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "30000");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    
            p = property(NUM_TEST_PER_EVICTION_RUN);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "0");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    
            p = property(MIN_EVICTABLE_IDLE_TIME_MS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "60000");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
    
            p = property(SOFT_MIN_EVICTABLE_IDLE_TIME_MS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "60000");        //$NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        } catch (NoSuchMethodError e) {
            LOGGER.error("Error initializing component GraphGeneratorListener due to missing method, if your version is lower than 2.10, this" +
                    "is expected to fail, if not check project dependencies");
        }
    }
}
