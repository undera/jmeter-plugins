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

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 
 * @author Félix Henry
 * @author Vincent Daburon
 */
public class ServerRunner {
	private static final Logger log = LoggingManager.getLoggerForClass();

	public static void run(Class serverClass) {
		try {
			executeInstance((NanoHTTPD) serverClass.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void executeInstance(NanoHTTPD server) {
		try {
			server.start();
		} catch (IOException ioe) {
			System.err.println("Couldn't start server:\n" + ioe);
			System.exit(-1);
		}

		log.info("Server started, Hit Enter to stop.\n");

		try {
			System.in.read();
		} catch (Throwable ignored) {
		}

		if (!server.isAlive()) {
			log.info("Server already stopped !\n");
			return;
		}
		server.stop();
		log.info("Server stopped.\n");
	}
}
