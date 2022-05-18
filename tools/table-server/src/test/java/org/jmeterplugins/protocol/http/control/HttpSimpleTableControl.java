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

package org.jmeterplugins.protocol.http.control;

import java.io.IOException;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.util.JMeterUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test element that implements the Workbench HTTP Simple Table function
 */
public class HttpSimpleTableControl extends AbstractTestElement {

    private static final long serialVersionUID = 233L;

    private static final Logger log = LoggerFactory.getLogger(HttpSimpleTableControl.class);

    private transient HttpSimpleTableServer server;

    public static final int DEFAULT_PORT = 9191;

    public static final String DEFAULT_PORT_S = Integer.toString(DEFAULT_PORT);

    public static final String DEFAULT_DATA_DIR = JMeterUtils.getJMeterBinDir();

    public static final boolean DEFAULT_TIMESTAMP = true;
    
    public static final boolean DEFAULT_DAEMON = false;

    public static final String DEFAULT_TIMESTAMP_S = Boolean
            .toString(DEFAULT_TIMESTAMP);
    
    public static final String DEFAULT_CHARSET_ENCODING_HTTP_RESPONSE = "UTF-8";
    public static final String DEFAULT_CHARSET_ENCODING_READ_FILE = "UTF-8";
    public static final String DEFAULT_CHARSET_ENCODING_WRITE_FILE = "UTF-8";
    		
    public static final String DEFAULT_LOG_LEVEL = "INFO";

    public static final String PORT = "HttpSimpleTableControlGui.port"; 

    public static final String DATA_DIR = "HttpSimpleTableControlGui.dir"; 

    public static final String TIMESTAMP = "HttpSimpleTableControlGui.timestamp"; 

    public HttpSimpleTableControl() {
        setPort(JMeterUtils.getPropDefault("jmeterPlugin.sts.port",
                DEFAULT_PORT));
        setTimestamp(JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.addTimestamp", DEFAULT_TIMESTAMP));
        setDataDir(JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.datasetDirectory", DEFAULT_DATA_DIR));
    }

    private void setPort(int port) {
        setProperty(new IntegerProperty(PORT, port));
    }

    public void setPort(String port) {
        setProperty(PORT, port);
    }

    public int getPort() {
        return getPropertyAsInt(PORT, DEFAULT_PORT);
    }

    public String getPortString() {
        return getPropertyAsString(PORT, DEFAULT_PORT_S);
    }

    public boolean getTimestamp() {
        return getPropertyAsBoolean(TIMESTAMP, DEFAULT_TIMESTAMP);
    }

    public void setTimestamp(boolean timestamp) {
        setProperty(new BooleanProperty(TIMESTAMP, timestamp));
    }

    public String getDataDir() {
        return getPropertyAsString(DATA_DIR, DEFAULT_DATA_DIR);
    }

    public void setDataDir(String dataDir) {
        setProperty(DATA_DIR, dataDir);
    }


    public void startHttpSimpleTable() throws IOException {
    	boolean daemon = HttpSimpleTableControl.DEFAULT_DAEMON; //false
    	
        String charsetEncodingHttpResponse = JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.charsetEncodingHttpResponse",
                HttpSimpleTableControl.DEFAULT_CHARSET_ENCODING_HTTP_RESPONSE);
        
        String charsetEncodingReadFile = JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.charsetEncodingReadFile",
                HttpSimpleTableControl.DEFAULT_CHARSET_ENCODING_READ_FILE);
        
        
        String charsetEncodingWriteFile = JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.charsetEncodingWriteFile",
                HttpSimpleTableControl.DEFAULT_CHARSET_ENCODING_WRITE_FILE);
        
       
        String fileEncodingSystem = System.getProperty("file.encoding");
        if (fileEncodingSystem != null) {
        	if (JMeterUtils.getProperty("jmeterPlugin.sts.charsetEncodingReadFile") == null) {
        		charsetEncodingReadFile=fileEncodingSystem;
        	}
        	if (JMeterUtils.getProperty("jmeterPlugin.sts.charsetEncodingWriteFile") == null) {
        		charsetEncodingWriteFile=fileEncodingSystem; 
        	}
        }
        
        String  samplerDefaultEncoding = JMeterUtils.getProperty("sampleresult.default.encoding");
        if (samplerDefaultEncoding != null) {
        	if (JMeterUtils.getProperty("jmeterPlugin.sts.charsetEncodingHttpResponse") == null) {
        		charsetEncodingHttpResponse = samplerDefaultEncoding;
        	}
        }
        log.info("Creating HttpSimpleTable from GUI or when JMeter start");
        log.info("------------------------------");
        log.info("SERVER_PORT : " + getPort());
        log.info("DATASET_DIR : " + getDataDir());
        log.info("ADD TIMESTAMP : " + getTimestamp());
        log.info("DAEMON PROCESS : " + daemon);
        log.info("charsetEncodingHttpResponse : " + charsetEncodingHttpResponse);
        log.info("charsetEncodingReadFile : " + charsetEncodingReadFile);
        log.info("charsetEncodingWriteFile : " + charsetEncodingWriteFile);
        log.info("------------------------------");
        log.info("STS_VERSION : " + HttpSimpleTableServer.STS_VERSION);  
        
        System.out.println("Creating HttpSimpleTable from GUI or when JMeter start");
        System.out.println("------------------------------");
        System.out.println("SERVER_PORT : " + getPort());
        System.out.println("DATASET_DIR : " + getDataDir());
        System.out.println("ADD TIMESTAMP : " + getTimestamp());
        System.out.println("DAEMON PROCESS : " + daemon);
        System.out.println("charsetEncodingHttpResponse : " + charsetEncodingHttpResponse);
        System.out.println("charsetEncodingReadFile : " + charsetEncodingReadFile);
        System.out.println("charsetEncodingWriteFile : " + charsetEncodingWriteFile);
        System.out.println("------------------------------");
        System.out.println("STS_VERSION : " + HttpSimpleTableServer.STS_VERSION);  
        
        
         server = new HttpSimpleTableServer(getPort(), getTimestamp(),
                getDataDir(), charsetEncodingHttpResponse, charsetEncodingReadFile, charsetEncodingWriteFile, daemon);
        server.start();
        GuiPackage instance = GuiPackage.getInstance();
        if (instance != null) {
            instance.register(server);
        }
    }

    public void stopHttpSimpleTable() {
        if (server != null) {
            server.stopServer();
            GuiPackage instance = GuiPackage.getInstance();
            if (instance != null) {
                instance.unregister(server);
            }
            server = null;
        }
    }

    @Override
    public boolean canRemove() {
        return null == server;
    }

    public boolean isServerAlive() {
        return server != null && server.isAlive();
    }
}
