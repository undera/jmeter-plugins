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

import org.apache.jmeter.gui.Stoppable;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpSimpleTableServer extends NanoHTTPD implements Stoppable, KeyWaiter {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String STS_VERSION = "1.3";
    public static final String ROOT = "/sts/";
    public static final String ROOT2 = "/sts";
    public static final String URI_INITFILE = "INITFILE";
    public static final String URI_READ = "READ";
    public static final String URI_ADD = "ADD";
    public static final String URI_SAVE = "SAVE";
    public static final String URI_LENGTH = "LENGTH";
    public static final String URI_STATUS = "STATUS";
    public static final String URI_RESET = "RESET";
    public static final String URI_STOP = "STOP";
    public static final String PARM_FILENAME = "FILENAME";
    public static final String PARM_LINE = "LINE";
    public static final String PARM_READ_MODE = "READ_MODE";
    public static final String PARM_ADD_MODE = "ADD_MODE";
    public static final String PARM_UNIQUE = "UNIQUE";
    public static final String PARM_KEEP = "KEEP";
    public static final String VAL_FIRST = "FIRST";
    public static final String VAL_LAST = "LAST";
    public static final String VAL_RANDOM = "RANDOM";
    public static final String VAL_TRUE = "TRUE";
    public static final String VAL_FALSE = "FALSE";

    public static final String INDEX = "<html><head><title>URL for the dataset</title><head>"
            + "<body><h4>From a data file (default: &lt;JMETER_HOME&gt;/bin/[data_file])</h4>"
            + "<p>Load file in memory:<br />"
            + "http://hostname:port/sts/INITFILE?FILENAME=file.txt</p>"
            + "<p>Get one line from list:<br />"
            + "http://hostname:port/sts/READ?READ_MODE=[<i>FIRST</i>,<i>LAST</i>,<i>RANDOM</i>]&KEEP=[<i>TRUE</i>,<i>FALSE</i>]&FILENAME=file.txt</p>"
            + "<p>Return the number of remaining lines of a linked list:<br />"
            + "http://hostname:port/sts/LENGTH?FILENAME=file.txt</p>"
            + "<p>Add a line into a file: (GET OR POST HTTP protocol)<br />"
            + "GET  : http://hostname:port/sts/ADD?FILENAME=file.txt&LINE=D0001123&ADD_MODE=[<i>FIRST</i>,<i>LAST</i>]<br />"
            + "GET Parameters : FILENAME=file.txt&LINE=D0001123&ADD_MODE=[<i>FIRST</i>,<i>LAST</i>]&UNIQUE=[<i>FALSE</i>,<i>TRUE</i>]<br />"
            + "POST : http://hostname:port/sts/ADD<br />"
            + "POST Parameters : FILENAME=file.txt,LINE=D0001123,ADD_MODE=[<i>FIRST</i>,<i>LAST</i>],UNIQUE=[<i>FALSE</i>,<i>TRUE</i>]</p>"
            + "<p>Save the specified linked list in a file to the default location:<br />"
            + "http://hostname:port/sts/SAVE?FILENAME=file.txt</p>"
            + "<p>Display the list of loaded files and the number of remaining lines for each linked list:<br />"
            + "http://hostname:port/sts/STATUS</p>"
            + "<p>Remove all of the elements from the specified list:<br />"
            + "http://hostname:port/sts/RESET?FILENAME=file.txt</p>"
            + "<p>Shutdown the Simple Table Server:<br />"
            + "http://hostname:port/sts/STOP</p></body></html>";
    private String myDataDirectory;
    private boolean bTimestamp;
    private Random myRandom;

    // CRLF ou LF ou CR
    public static String lineSeparator = System.getProperty("line.separator");

    private Map<String, LinkedList<String>> database = new HashMap<String, LinkedList<String>>();

    public HttpSimpleTableServer(int port, boolean timestamp, String dataDir) {
        super(port);
        myDataDirectory = dataDir;
        bTimestamp = timestamp;
        myRandom = new Random();
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        String msg = "<html><title>KO</title>" + lineSeparator
                + "<body>Error : unknown command !</body>" + lineSeparator
                + "</html>";

        Map<String, String> files = new HashMap<String, String>();
        if (Method.POST.equals(method)) {
            try {
                session.parseBody(files);
            } catch (IOException ioe) {
                return new Response(Response.Status.INTERNAL_ERROR,
                        MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: "
                        + ioe.getMessage());
            } catch (ResponseException re) {
                return new Response(re.getStatus(), MIME_PLAINTEXT,
                        re.getMessage());
            }
        }
        Map<String, String> parms = session.getParms();
        if (uri.equals(ROOT) || uri.equals(ROOT2)) {
            msg = INDEX;
        } else {
            msg = doAction(uri, method, parms);
        }

        Response response = new NanoHTTPD.Response(msg);

        // no cache for the response
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");		
        return response;
    }

    protected synchronized String doAction(String uri, Method method,
                                           Map<String, String> parms) {
        String msg = "<html><title>KO</title>" + lineSeparator
                + "<body>Error : unknown command !</body>" + lineSeparator
                + "</html>";
        if (uri.equals(ROOT + URI_INITFILE)) {
            msg = initFile(parms.get(PARM_FILENAME));
        }
        if (uri.equals(ROOT + URI_READ)) {
            msg = read(parms.get(PARM_READ_MODE), parms.get(PARM_KEEP),
                    parms.get(PARM_FILENAME));
        }
        if (uri.equals(ROOT + URI_ADD) && (Method.POST.equals(method) || (Method.GET.equals(method)))) {
            msg = add(parms.get(PARM_ADD_MODE), parms.get(PARM_LINE),
                    parms.get(PARM_UNIQUE), parms.get(PARM_FILENAME));
        }
        if (uri.equals(ROOT + URI_LENGTH)) {
            msg = length(parms.get(PARM_FILENAME));
        }
        if (uri.equals(ROOT + URI_SAVE)) {
            msg = save(parms.get(PARM_FILENAME));
        }
        if (uri.equals(ROOT + URI_STATUS)) {
            msg = status();
        }
        if (uri.equals(ROOT + URI_RESET)) {
            msg = reset(parms.get(PARM_FILENAME));
        }
        if (uri.equals(ROOT + URI_STOP)) {
            stopServer();
        }
        return msg;
    }

    private String status() {
        if (database.isEmpty()) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : Database was empty !</body>"
                    + lineSeparator + "</html>";
        }
        String msg = "";
        for (String key : database.keySet()) {
            msg += key + " = " + database.get(key).size() + "<br />"
                    + lineSeparator;
        }
        return "<html><title>OK</title>" + lineSeparator + "<body>"
                + lineSeparator + msg + "</body></html>";
    }

    private String read(String readMode, String keepMode, String filename) {
        if (null == filename) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : FILENAME parameter was missing !</body>"
                    + lineSeparator + "</html>";
        }
        if (!database.containsKey(filename)) {
            return "<html><title>KO</title>" + lineSeparator + "<body>Error : "
                    + filename + " not loaded yet !</body>" + lineSeparator
                    + "</html>";
        }
        if (database.get(filename).isEmpty()) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : No more line !</body>" + lineSeparator
                    + "</html>";
        }
        if (null == readMode) {
            readMode = VAL_FIRST;
        }
        if (null == keepMode) {
            keepMode = VAL_TRUE;
        }
        if (!VAL_FIRST.equals(readMode) && !VAL_LAST.equals(readMode)
                && !VAL_RANDOM.equals(readMode)) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : READ_MODE value has to be FIRST, LAST or RANDOM !</body>"
                    + lineSeparator + "</html>";
        }
        if (!VAL_TRUE.equals(keepMode) && !VAL_FALSE.equals(keepMode)) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : KEEP value has to be TRUE or FALSE !</body>"
                    + lineSeparator + "</html>";
        }
        String line;
        int index = 0;

        if (VAL_LAST.equals(readMode)) {
            index = database.get(filename).size() - 1;
        }
        if (VAL_RANDOM.equals(readMode)) {
            index = myRandom.nextInt(database.get(filename).size());
        }

        line = database.get(filename).remove(index);

        if (VAL_TRUE.equals(keepMode)) {
            database.get(filename).add(line);
        }

        return "<html><title>OK</title>" + lineSeparator + "<body>" + line
                + "</body>" + lineSeparator + "</html>";
    }

    private String add(String addMode, String line, String uniqueMode, String filename) {
        if (null == filename) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : FILENAME parameter was missing !</body>"
                    + lineSeparator + "</html>";
        }
        if (null == line) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : LINE parameter was missing !</body>"
                    + lineSeparator + "</html>";
        }
        if (!database.containsKey(filename)) {
            database.put(filename, new LinkedList<String>());
        }
        if (null == addMode) {
            addMode = VAL_FIRST;
        }
        if (null == uniqueMode) {
            uniqueMode = VAL_FALSE;
        }
        if (!VAL_FIRST.equals(addMode) && !VAL_LAST.equals(addMode)) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : ADD_MODE value has to be FIRST or LAST !</body>"
                    + lineSeparator + "</html>";
        }
        if (!VAL_TRUE.equals(uniqueMode) && !VAL_FALSE.equals(uniqueMode)) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : UNIQUE value has to be TRUE or FALSE !</body>"
                    + lineSeparator + "</html>";
        }

        if (VAL_TRUE.equals(uniqueMode)) {
            if (database.get(filename).contains(line)) {
                return "<html><title>KO</title>"
                        + lineSeparator
                        + "<body>Error : ENTRY already exists !</body>"
                        + lineSeparator + "</html>";
            }
        }

        if (VAL_FIRST.equals(addMode)) {
            database.get(filename).addFirst(line);
        } else {
            database.get(filename).add(line);
        }

        return "<html><title>OK</title>" + lineSeparator + "<body></body>"
                + lineSeparator + "</html>";
    }

    private String save(String filename) {
        if (null == filename) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : FILENAME parameter was missing !</body>"
                    + lineSeparator + "</html>";
        }
        if (filename.matches(".*[\\\\/:].*") || filename.equals(".")
                || filename.equals("..")) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : Illegal character found !</body>"
                    + lineSeparator + "</html>";
        }
        if (filename.length() > 128) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : Maximum size reached (128) !</body>"
                    + lineSeparator + "</html>";
        }
        if (!database.containsKey(filename)) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : LinkedList not found !</body>"
                    + lineSeparator + "</html>";
        }
        BufferedWriter out = null;
        String saveFilename = filename;
        if (bTimestamp) {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat(
                    "yyyyMMdd'T'HH'h'mm'm'ss's.'");
            saveFilename = ft.format(dNow) + filename;
        }
        try {
            Iterator<String> it = database.get(filename).iterator();
            out = new BufferedWriter(new FileWriter(new File(myDataDirectory,
                    saveFilename)));
            while (it.hasNext()) {
                out.write(it.next());
                out.write(lineSeparator);
            }
        } catch (FileNotFoundException e1) {
            return "<html><title>KO</title>" + lineSeparator + "<body>Error : "
                    + e1.getMessage() + "</body>" + lineSeparator + "</html>";
        } catch (IOException e2) {
            return "<html><title>KO</title>" + lineSeparator + "<body>Error : "
                    + e2.getMessage() + "</body>" + lineSeparator + "</html>";
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e3) {
                    return "<html><title>KO</title>" + lineSeparator
                            + "<body>Error : " + e3.getMessage() + "</body>"
                            + lineSeparator + "</html>";
                }
            }
        }
        return "<html><title>OK</title>" + lineSeparator + "<body>"
                + database.get(filename).size() + "</body>" + lineSeparator
                + "</html>";
    }

    private String length(String filename) {
        if (null == filename) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : FILENAME parameter was missing !</body>"
                    + lineSeparator + "</html>";
        }
        if (!database.containsKey(filename)) {
            return "<html><title>KO</title>" + lineSeparator + "<body>Error : "
                    + filename + " not loaded yet !</body>" + lineSeparator
                    + "</html>";
        }
        return "<html><title>OK</title>" + lineSeparator + "<body>"
                + database.get(filename).size() + "</body>" + lineSeparator
                + "</html>";
    }

    private String reset(String filename) {
        if (null == filename) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : FILENAME parameter was missing !</body>"
                    + lineSeparator + "</html>";
        }
        if (database.containsKey(filename)) {
            database.get(filename).clear();
        }
        return "<html><title>OK</title>" + lineSeparator + "<body></body>"
                + lineSeparator + "</html>";
    }

    private String initFile(String filename) {
        if (null == filename) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : FILENAME parameter was missing !</body>"
                    + lineSeparator + "</html>";
        }
        if (filename.matches(".*[\\\\/:].*") || filename.equals(".")
                || filename.equals("..")) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : Illegal character found !</body>"
                    + lineSeparator + "</html>";
        }
        if (filename.length() > 128) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : Maximum size reached (128) !</body>"
                    + lineSeparator + "</html>";
        }

        LinkedList<String> lines = new LinkedList<String>();
        BufferedReader bufferReader = null;
        File f = new File(myDataDirectory, filename);
        if (f.exists()) {
            try {
                bufferReader = new BufferedReader(new FileReader(f), 50 * 1024);
                String line;
                while ((line = bufferReader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (FileNotFoundException e1) {
                return "<html><title>KO</title>" + lineSeparator
                        + "<body>Error : " + e1.getMessage() + "</body>"
                        + lineSeparator + "</html>";
            } catch (IOException e2) {
                return "<html><title>KO</title>" + lineSeparator
                        + "<body>Error : " + e2.getMessage() + "</body>"
                        + lineSeparator + "</html>";
            } finally {
                if (null != bufferReader) {
                    try {
                        bufferReader.close();
                    } catch (IOException e3) {
                        return "<html><title>KO</title>" + lineSeparator
                                + "<body>Error : " + e3.getMessage()
                                + "</body>" + lineSeparator + "</html>";
                    }
                }
            }
            database.put(filename, lines);
            return "<html><title>OK</title>" + lineSeparator + "<body>"
                    + lines.size() + "</body>" + lineSeparator + "</html>";
        }
        return "<html><title>KO</title>" + lineSeparator
                + "<body>Error : file not found !</body>" + lineSeparator
                + "</html>";
    }

    @Override
    public void stopServer() {
        log.info("HTTP Simple Table Server is shutting down...");
        stop();
    }

    public static void main(String args[]) {
        JMeterUtils.loadJMeterProperties("jmeter.properties");
        String dataset = JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.datasetDirectory",
                HttpSimpleTableControl.DEFAULT_DATA_DIR);
        int port = JMeterUtils.getPropDefault("jmeterPlugin.sts.port",
                HttpSimpleTableControl.DEFAULT_PORT);
        boolean timestamp = JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.addTimestamp",
                HttpSimpleTableControl.DEFAULT_TIMESTAMP);

        // default level
        LoggingManager.setPriority("INFO");
        // allow override by system properties
        LoggingManager.setLoggingLevels(System.getProperties());

        HttpSimpleTableServer serv = new HttpSimpleTableServer(port, timestamp,
                dataset);

        log.info("Creating HttpSimpleTable ...");
        log.info("------------------------------");
        log.info("SERVER_PORT : " + port);
        log.info("DATASET_DIR : " + dataset);
        log.info("TIMESTAMP : " + timestamp);
        log.info("------------------------------");
        log.info("STS_VERSION : " + STS_VERSION);
        ServerRunner.executeInstance(serv);
    }

    public void waitForKey() {
        log.info("Hit Enter to stop");
        try {

            System.in.read();
        } catch (Throwable ignored) {
        }
    }
}