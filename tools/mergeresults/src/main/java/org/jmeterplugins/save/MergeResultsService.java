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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kg.apc.jmeter.vizualizers.CorrectedResultCollector;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.save.CSVSaveService;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class MergeResultsService {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String TESTRESULTS_START_V1_1_PREVER = "<testResults version=\""; 

    private static final String TESTRESULTS_START_V1_1_POSTVER = "\">"; 

    private static final String TESTRESULTS_END = "</testResults>"; 

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; 

    public static final String FILENAME = "filename"; 

    /** AutoFlush on each line */
    private static final boolean SAVING_AUTOFLUSH = JMeterUtils.getPropDefault(
            "jmeter.save.saveservice.autoflush", false); 

    private PrintWriter out;
    private static final Map<String, FileEntry> files = new HashMap<>();

    private void initializeFileOutput(String filename,
            SampleSaveConfiguration saveConfig) throws IOException {
        if (filename != null) {
            if (out == null) {
                try {
                    out = getFileWriter(filename, saveConfig);
                } catch (FileNotFoundException e) {
                    out = null;
                }
            }
        }
    }

    private static PrintWriter getFileWriter(String filename,
            SampleSaveConfiguration saveConfig) throws IOException {
        if (filename == null || filename.length() == 0) {
            return null;
        }
        filename = FileServer.resolveBaseRelativeName(filename);
        FileEntry fe = files.get(filename);
        PrintWriter writer;

        if (fe == null) {
            // Find the name of the directory containing the file
            // and create it - if there is one
            File pdir = new File(filename).getParentFile();
            if (pdir != null) {
                // returns false if directory already exists, so need to check
                // again
                if (pdir.mkdirs()) {
                    log.info("Folder " + pdir.getAbsolutePath()
                            + " was created");
                }
                // else if might have been created by another process so not a
                // problem
                if (!pdir.exists()) {
                    log.warn("Error creating directories for "
                            + pdir.toString());
                }
            }
            writer = new PrintWriter(new OutputStreamWriter(
                    new BufferedOutputStream(new FileOutputStream(filename)),
                    SaveService.getFileEncoding("UTF-8")), SAVING_AUTOFLUSH); 
            log.debug("Opened file: " + filename);
            files.put(filename, new FileEntry(writer, saveConfig));
        } else {
            writer = fe.pw;
        }
        writeFileStart(writer, saveConfig);
        return writer;
    }

    private static void writeFileStart(PrintWriter writer,
            SampleSaveConfiguration saveConfig) {
        if (saveConfig.saveAsXml()) {
            writer.print(XML_HEADER);
            // Write the EOL separately so we generate LF line ends on Unix and
            // Windows
            writer.print("\n"); 
            String pi = saveConfig.getXmlPi();
            if (pi.length() > 0) {
                writer.println(pi);
            }
            writer.print(TESTRESULTS_START_V1_1_PREVER);
            writer.print(SaveService.getVERSION());
            writer.print(TESTRESULTS_START_V1_1_POSTVER);
            // Write the EOL separately so we generate LF line ends on Unix and
            // Windows
            writer.print("\n"); 
        } else if (saveConfig.saveFieldNames()) {
            writer.println(CSVSaveService
                    .printableFieldNamesToString(saveConfig));
        }
    }

    /*
     * Keep track of the file writer and the configuration, as the instance used
     * to close them is not the same as the instance that creates them. This
     * means one cannot use the saved PrintWriter or use getSaveConfig()
     */
    private static class FileEntry {
        final PrintWriter pw;
        final SampleSaveConfiguration config;

        FileEntry(PrintWriter _pw, SampleSaveConfiguration _config) {
            pw = _pw;
            config = _config;
        }
    }

    private void finalizeFileOutput() {
        for (Map.Entry<String, MergeResultsService.FileEntry> me : files
                .entrySet()) {
            log.debug("Closing: " + me.getKey());
            FileEntry fe = me.getValue();
            writeFileEnd(fe.pw, fe.config);
            fe.pw.close();
            if (fe.pw.checkError()) {
                log.warn("Problem detected during use of " + me.getKey());
            }
        }
        files.clear();
    }

    private static void writeFileEnd(PrintWriter pw,
            SampleSaveConfiguration saveConfig) {
        if (saveConfig.saveAsXml()) {
            pw.print("\n"); 
            pw.print(TESTRESULTS_END);
            pw.print("\n");// Added in version 1.1 
        }
    }

    public void mergeSamples(CorrectedResultCollector crc,
            List<SampleResult> samples) {
        SampleEvent event;
        try {
            initializeFileOutput(crc.getFilename(), crc.getSaveConfig());
        } catch (IOException e) {
            log.warn("Error trying to initialize output file " + e.toString());
        }

        for (SampleResult result : samples) {
            SampleSaveConfiguration config = crc.getSaveConfig();
            event = new SampleEvent(result, null);
            try {
                if (config.saveAsXml()) {
                    SaveService.saveSampleResult(event, out);
                } else { // !saveAsXml
                    String savee = CSVSaveService
                            .resultToDelimitedString(event);
                    out.println(savee);
                }
            } catch (Exception err) {
                log.error("Error trying to record a sample", err);
                // should throw exception back to caller
            }
        }
        finalizeFileOutput();
    }

}
