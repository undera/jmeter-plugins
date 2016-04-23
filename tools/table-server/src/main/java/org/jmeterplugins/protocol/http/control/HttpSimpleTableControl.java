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
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Test element that implements the Workbench HTTP Simple Table function
 */
public class HttpSimpleTableControl extends AbstractTestElement {

    private static final long serialVersionUID = 233L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    private transient HttpSimpleTableServer server;

    public static final int DEFAULT_PORT = 9191;

    public static final String DEFAULT_PORT_S = Integer.toString(DEFAULT_PORT);

    public static final String DEFAULT_DATA_DIR = JMeterUtils.getJMeterBinDir();

    public static final boolean DEFAULT_TIMESTAMP = true;

    public static final String DEFAULT_TIMESTAMP_S = Boolean
            .toString(DEFAULT_TIMESTAMP);

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
        log.info("Creating HTTP Simple Table Server...");
        log.info("Port=" + getPort());
        log.info("Dataset directory=" + getDataDir());
        log.info("Timestamp=" + getTimestamp());
        log.info("STS Version=" + HttpSimpleTableServer.STS_VERSION);
        server = new HttpSimpleTableServer(getPort(), getTimestamp(),
                getDataDir());
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
