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

package org.jmeterplugins.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 
 * @author Félix Henry
 * @author Vincent Daburon
 */
public class HtmlGraphVisualizationGenerator {
	private static final Logger log = LoggingManager.getLoggerForClass();

	// CRLF ou LF ou CR
	public static final String LINE_SEP = System.getProperty("line.separator");

	// array of supported extensions
	static final String[] EXTENSIONS = new String[] { "gif", "png", "bmp",
			"jpg" };
	// filter to identify images based on their extensions
	static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

		@Override
		public boolean accept(final File dir, final String name) {
			for (final String ext : EXTENSIONS) {
				if (name.endsWith("." + ext)) {
					return (true);
				}
			}
			return (false);
		}
	};

	public static void main(String[] args) {

		JMeterUtils.loadJMeterProperties("jmeter.properties");
		String path = JMeterUtils
				.getPropDefault("jmeterPlugin.gvg.imageDirectory", ".");

		File folder = new File(path);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(new File(
					"index-graphviz.html")));

			log.info("Generating index-graphviz.html...");

			out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.write(LINE_SEP);
			out.write("<html>");
			out.write(LINE_SEP);
			out.write("<head>");
			out.write(LINE_SEP);
			out.write("<title>Graphs generated with JMeter Plugins</title>");
			out.write(LINE_SEP);
			out.write("</head>");
			out.write(LINE_SEP);
			out.write("<body><br/><br/>");
			out.write(LINE_SEP);

			out.write("<h1> Generated date " + new java.util.Date().toString() + "</h1><br/>");
			out.write(LINE_SEP);
			if (folder.isDirectory()) { // make sure it's a directory
				for (final File f : folder.listFiles(IMAGE_FILTER)) {
					out.write("<h2>" + f.getName() + "</h2><br/>");
					out.write(LINE_SEP);
					out.write("<img class=\"photo\" src=\"./" + f.getName() + "\"\\><br/><br/>");
					out.write(LINE_SEP);
				}
			}
			out.write(LINE_SEP);
			out.write("</body>");
			out.write(LINE_SEP);
			out.write("</html>");

			log.info("Done!");

		} catch (final IOException e1) {
			System.out.println(e1.getMessage());

		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e2) {
					System.out.println(e2.getMessage());
				}
			}
		}
	}
}
