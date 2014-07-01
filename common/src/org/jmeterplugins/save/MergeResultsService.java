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

package org.jmeterplugins.save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.save.CSVSaveService;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 
 * @author Félix Henry
 * @author Vincent Daburon
 */
public class MergeResultsService {

	private static final Logger log = LoggingManager.getLoggerForClass();

	private static final String LINE_SEP = System.getProperty("line.separator"); // $NON-NLS-1$

	private MergeResultsService() {
	}

	public static void mergeSamples(String output, String fieldNames,
			List<SampleResult> samples) {
		BufferedWriter out = null;
		SampleEvent event = null;
		try {
			out = new BufferedWriter(new FileWriter(new File(output)));
			out.write(fieldNames);
			out.write(LINE_SEP);
			for (SampleResult res : samples) {
				if (null == res.getSaveConfig()) {
					res.setSaveConfig(SampleSaveConfiguration.staticConfig());
				}
				event = new SampleEvent(res, null);
				out.write(CSVSaveService.resultToDelimitedString(event));
				out.write(LINE_SEP);
			}
		} catch (IOException ioe1) {
			log.warn(ioe1.toString());
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException ioe2) {
					log.warn(ioe2.toString());
				}
			}
		}
	}

}
