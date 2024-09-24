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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class HttpSimpleTableServer extends NanoHTTPD implements Stoppable, KeyWaiter {
    private static final Logger log = LoggerFactory.getLogger(HttpSimpleTableServer.class);


    public static final String STS_VERSION = "5.0";
    public static final String ROOT = "/sts/";
    public static final String ROOT2 = "/sts";
    public static final String URI_INITFILE = "INITFILE";
    public static final String URI_READ = "READ";
    public static final String URI_READMULTI = "READMULTI";
    public static final String URI_ADD = "ADD";
    public static final String URI_SAVE = "SAVE";
    public static final String URI_LENGTH = "LENGTH";
    public static final String URI_STATUS = "STATUS";
    public static final String URI_RESET = "RESET";
    public static final String URI_STOP = "STOP";
    public static final String URI_CONFIG = "CONFIG";
    public static final String URI_FIND = "FIND";
    
    public static final String PARM_FILENAME = "FILENAME";
    public static final String PARM_LINE = "LINE";
    public static final String PARM_READ_MODE = "READ_MODE";
    public static final String PARM_ADD_MODE = "ADD_MODE";
    public static final String PARM_UNIQUE = "UNIQUE";
    public static final String PARM_KEEP = "KEEP";
    public static final String PARM_ADD_TIMESTAMP = "ADD_TIMESTAMP";
    public static final String PARM_FIND_MODE= "FIND_MODE";

    public static final String PARM_READMULTI_NB_LINES = "NB_LINES";
    public static final String VAL_FIRST = "FIRST";
    public static final String VAL_LAST = "LAST";
    public static final String VAL_RANDOM = "RANDOM";
    public static final String VAL_TRUE = "TRUE";
    public static final String VAL_FALSE = "FALSE";

    public static final String VAL_FIND_STR_SUBSTRING = "SUBSTRING";
    public static final String VAL_FIND_STR_EQUALS = "EQUALS";
    public static final String VAL_FIND_REGEX_FIND = "REGEX_FIND";
    public static final String VAL_FIND_REGEX_MATCH = "REGEX_MATCH";


    public static final String INDEX = "<html><head><title>Help URL for the dataset</title><head>"
            + "<body><h4>Help Http Simple Table Server (STS) Version : " + STS_VERSION + "<br/>From a data file (default: &lt;JMETER_HOME&gt;/bin/&lt;data_file&gt;) or from the directory set with jmeterPlugin.sts.datasetDirectory</h4>"
            + "<p>The parameter enclosed in square brackets is <b>[optional]</b> and and the values in italics correspond to the <b><i>possible values</i></b> <br />"
            + "<p><b>Load file in memory:</b><br />"
            + "http://hostname:port/sts/INITFILE?FILENAME=file.txt</p>"
            + "<p><b>Get one line from list:</b><br />"
            + "http://hostname:port/sts/READ?FILENAME=file.txt&[READ_MODE=[<i>FIRST</i> (Default),<i>LAST</i>,<i>RANDOM</i>]]&[KEEP=[<i>TRUE</i> (Default),<i>FALSE</i>]]</p>"
            + "<p><b>Get multi lines from list in one request:</b><br />"
            + "http://hostname:port/sts/READMULTI?FILENAME=file.txt&NB_LINES=number of lines to read : Integer &ge; 1 and &le; list size&[READ_MODE=[<i>FIRST</i> (default),<i>LAST</i>,<i>RANDOM</i>]]&[KEEP=[<i>TRUE</i> (Default),<i>FALSE</i>]]<br />"
            + "E.g: http://hostname:port/sts/READMULTI?FILENAME=myfile.txt&NB_LINES=4&READ_MODE=FIRST&KEEP=TRUE<br /></p>"
            + "<p><b>Find a line in a file:</b> (GET OR POST HTTP protocol)<br />The line to find is a string this SUBSTRING (Default) or EQUALS and a regular expression with REGEX_FIND (contains) and REGEX_MATCH (entire region the pattern).<br />"
            + "GET : http://hostname:port/sts/FIND?FILENAME=file.txt&LINE=(BLUE|RED)&[FIND_MODE=[<i>SUBSTRING</i>,<i>EQUALS</i>,<i>REGEX_FIND</i>,<i>REGEX_MATCH</i>]]&[KEEP=[<i>TRUE</i>,<i>FALSE</i>]]<br />"
            + "GET Parameters : FILENAME=file.txt&LINE=RED&[FIND_MODE=[<i>SUBSTRING</i>,<i>EQUALS</i>,<i>REGEX_FIND</i>,<i>REGEX_MATCH</i>]]&[KEEP=[<i>TRUE</i>,<i>FALSE</i>]]<br />"
            + "<br />POST : http://hostname:port/sts/FIND<br />"
            + "POST Parameters : FILENAME=file.txt,LINE=(BLUE|RED) or LINE=BLUE or LINE=B.* or LINE=.*E.* ,[FIND_MODE=[<i>SUBSTRING</i>,<i>EQUALS</i>,<i>REGEX_FIND</i>,<i>REGEX_MATCH</i>]]&[KEEP=[<i>TRUE</i>,<i>FALSE</i>]]<br />"
            + "If NOT find return title KO and \"Error : Not find !\"</p>"
            + "<p><b>Return the number of remaining lines of a linked list:</b><br />"
            + "http://hostname:port/sts/LENGTH?FILENAME=file.txt</p>"
            + "<p><b>Add a line into a file:</b> (GET OR POST HTTP protocol)<br />"
            + "GET : http://hostname:port/sts/ADD?FILENAME=file.txt&LINE=D0001123&[ADD_MODE=[<i>FIRST</i>,<i>LAST</i>]]&[UNIQUE=[<i>FALSE</i>,<i>TRUE</i>]]<br />"
            + "GET Parameters : FILENAME=file.txt&LINE=D0001123&[ADD_MODE=[<i>FIRST</i>,<i>LAST</i>]]&[UNIQUE=[<i>FALSE</i>,<i>TRUE</i>]]<br />"
            + "<br />POST : http://hostname:port/sts/ADD<br />"
            + "POST Parameters : FILENAME=file.txt,LINE=D0001123,[ADD_MODE=[<i>FIRST</i>,<i>LAST</i>]],[UNIQUE=[<i>FALSE</i>,<i>TRUE</i>]]</p>"
            + "<p><b>Save the specified linked list in a file</b> to the default location:<br />"
            + "http://hostname:port/sts/SAVE?FILENAME=file.txt&[ADD_TIMESTAMP=[<i>FALSE</i>,<i>TRUE</i>]]</p>"
            + "<p><b>Display the list of loaded files and the number of remaining lines for each linked list:</b> <br />"
            + "http://hostname:port/sts/STATUS</p>"
            + "<p><b>Remove all of the elements from the specified list:</b> <br />"
            + "http://hostname:port/sts/RESET?FILENAME=file.txt</p>"
            + "<p><b>Show configuration:</b><br />"
            + "http://hostname:port/sts/CONFIG</p>"
            + "<p><b>Shutdown the Simple Table Server:</b><br />"
            + "http://hostname:port/sts/STOP</p></body></html>"
            + "<p><b>Mode daemon :</b><br />"
            + "jmeterPlugin.sts.daemon=[<i>true,false</i>] if <i>true</i> daemon process don't wait keyboards key pressed for nohup command, if <i>false</i> (default) wait keyboards key &lt;ENTER&gt; to Stop<br />"
            + "<h4>To load files at STS Startup use (optional) :</h4>"
            + "<p>E.g: read 3 csv files with comma separator (not a regular expression), files are read from the directory set with jmeterPlugin.sts.datasetDirectory <br />"
            + "jmeterPlugin.sts.initFileAtStartup=file1.csv,file2.csv,file3.csv<br />"
            + "jmeterPlugin.sts.initFileAtStartupRegex=false<br />"
            + "<p>OR<br />E.g: read all files with .csv extension declare with a regular expression (initFileAtStartupRegex=true) from directory set with jmeterPlugin.sts.datasetDirectory <br />"
            + "jmeterPlugin.sts.initFileAtStartup=.+\\.csv<br />"
            + "jmeterPlugin.sts.initFileAtStartupRegex=true<br />"
            + "<h4>Set the Charset to read, write file and send http response :</h4>"
            + "<p>E.g : charset = UTF-8, ISO8859_15 or Cp1252 (Windows)<br />"
            + "jmeterPlugin.sts.charsetEncodingHttpResponse=&lt;charset&gt; (Use UTF-8) in the http header add \"Content-Type:text/html; charset=&lt;charset&gt;\", default JMeter property : sampleresult.default.encoding<br />"
            + "jmeterPlugin.sts.charsetEncodingReadFile=&lt;charset&gt; (set the charset corresponding to characters in the file), default System property : file.encoding<br />"
            + "jmeterPlugin.sts.charsetEncodingWriteFile=&lt;charset&gt; default System property : file.encoding<br />"
            + "</body></html>";
            
    
    private static boolean bStartFromMain = false;
    private int myPort;
    private String myDataDirectory;
    private boolean bTimestamp;
    private Random myRandom;
    private String myCharsetEncodingHttpResponse;
    private String myCharsetEncodingReadFile;
    private String myCharsetEncodingWriteFile;
    private static boolean bIsDemon = false;

    // CRLF ou LF ou CR
    public static String lineSeparator = System.getProperty("line.separator");

    private Map<String, LinkedList<String>> database = new HashMap<String, LinkedList<String>>();

    public HttpSimpleTableServer(int port, boolean timestamp, String dataDir, String charsetEncodingHttpResponse, String charsetEncodingReadFile, String charsetEncodingWriteFile, boolean isDemon) {
        super(port);
        myPort=port;
        myDataDirectory = dataDir;
        bTimestamp = timestamp;
        myRandom = new Random();
        myCharsetEncodingHttpResponse=charsetEncodingHttpResponse;
        myCharsetEncodingReadFile=charsetEncodingReadFile;
        myCharsetEncodingWriteFile=charsetEncodingWriteFile;
        bIsDemon=isDemon;

    }
    
    protected HttpSimpleTableServer() {
    	 super(-1);
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

        Response response = new Response(msg);

        // no cache for the response
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        // add the encoding charset
        response.addHeader("Content-Type", "text/html; charset=" + myCharsetEncodingHttpResponse);
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
        if (uri.equals(ROOT + URI_READMULTI)) {
            msg = readmulti(parms.get(PARM_READ_MODE), parms.get(PARM_KEEP),
                    parms.get(PARM_FILENAME), parms.get(PARM_READMULTI_NB_LINES));
        }
        if (uri.equals(ROOT + URI_FIND) && (Method.POST.equals(method) || (Method.GET.equals(method)))) {
            msg = find(parms.get(PARM_FIND_MODE), parms.get(PARM_LINE), parms.get(PARM_KEEP),
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
            msg = save(parms.get(PARM_FILENAME), parms.get(PARM_ADD_TIMESTAMP));
        }
        if (uri.equals(ROOT + URI_STATUS)) {
            msg = status();
        }
        if (uri.equals(ROOT + URI_RESET)) {
            msg = reset(parms.get(PARM_FILENAME));
        }
        if (uri.equals(ROOT + URI_CONFIG)) {
            msg = showConfig();
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

    private String readmulti(String readMode, String keepMode, String filename, String nbLinesToRead) {
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

        int nbLines = 1;
        try {
            nbLines = Integer.parseInt(nbLinesToRead);
        } catch (NumberFormatException ex) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : Can't parse integer parameter NB_LINES : " + nbLinesToRead + " !</body>"
                    + lineSeparator + "</html>";
        }

        if (nbLines <= 0) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : Parameter NB_LINES must be greater or equals than 1 : " + nbLinesToRead + " !</body>"
                    + lineSeparator + "</html>";
        }

        if (nbLines > database.get(filename).size()) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : Number lines to read greater than file size, " + nbLines + " greater than " +  database.get(filename).size() + " !</body>"
                    + lineSeparator + "</html>";
        }

        String line;
        int index = 0;

        String[] tabString = new String[nbLines];
        for (int i = 0; i < nbLines; i++) {
            if (VAL_LAST.equals(readMode)) {
                index = database.get(filename).size() - 1;
            }
            if (VAL_RANDOM.equals(readMode)) {
                index = myRandom.nextInt(database.get(filename).size());
            }

            line = database.get(filename).remove(index);
            tabString[i] = line;
        }

        // if keep add all lines at the end
        for (int i = 0; i < nbLines; i++) {
            if (VAL_TRUE.equals(keepMode)) {
                database.get(filename).add(tabString[i]);
            }
        }

        StringBuffer sb = new StringBuffer(2048);
        sb.append(lineSeparator);
        for (int i = 0; i < nbLines; i++) {
            sb.append(tabString[i]);
            sb.append("<br />");
            sb.append(lineSeparator);
        }

        return "<html><title>OK</title>" + lineSeparator + "<body>" + sb
                + "</body>" + lineSeparator + "</html>";
    }
    private String find(String findMode, String lineToFind, String keepMode, String filename) {
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

        if (lineToFind == null || lineToFind.length() == 0) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : Cant't find empty line !</body>" + lineSeparator
                    + "</html>";
        }

        if (null == findMode) {
            findMode = VAL_FIND_STR_SUBSTRING;
        }
        if (null == keepMode) {
            keepMode = VAL_TRUE;
        }
        if (!VAL_FIND_STR_SUBSTRING.equals(findMode) && !VAL_FIND_REGEX_FIND.equals(findMode) &&
            !VAL_FIND_STR_EQUALS.equals(findMode) && !VAL_FIND_REGEX_MATCH.equals(findMode)) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : " + PARM_FIND_MODE + " value has to be " + VAL_FIND_STR_EQUALS + ", " + VAL_FIND_STR_SUBSTRING +
                    ", " +  VAL_FIND_REGEX_FIND + " or " + VAL_FIND_REGEX_MATCH + " !</body>"
                    + lineSeparator + "</html>";
        }
        if (!VAL_TRUE.equals(keepMode) && !VAL_FALSE.equals(keepMode)) {
            return "<html><title>KO</title>"
                    + lineSeparator
                    + "<body>Error : " + PARM_KEEP + " has to be TRUE or FALSE !</body>"
                    + lineSeparator + "</html>";
        }

        String line = "NOT_FOUND";
        int index = 0;
        boolean find = false;

        LinkedList linkedList  = database.get(filename);

        if (VAL_FIND_STR_SUBSTRING.equals(findMode)) {
            Iterator<String> iterator = linkedList.iterator();
            while (iterator.hasNext() && !find) {
                String lineTmp = iterator.next();
                if (lineTmp.contains(lineToFind)) {
                    find = true;
                    line = lineTmp;
                } else {
                    index++;
                }
            }
        }

        if (VAL_FIND_STR_EQUALS.equals(findMode)) {
            Iterator<String> iterator = linkedList.iterator();
            while (iterator.hasNext() && !find) {
                String lineTmp = iterator.next();
                if (lineTmp.equals(lineToFind)) {
                    find = true;
                    line = lineTmp;
                } else {
                    index++;
                }
            }
        }

        if (VAL_FIND_REGEX_FIND.equals(findMode) || VAL_FIND_REGEX_MATCH.equals(findMode)) {
            Pattern p = null;
            try {
                p = Pattern.compile(lineToFind);
            } catch (PatternSyntaxException ex) {
                return "<html><title>KO</title>"
                        + lineSeparator
                        + "<body>Error : Regex compile error !</body>"
                        + lineSeparator + "</html>";
            }

            Iterator<String> iterator = linkedList.iterator();
            while (iterator.hasNext() && !find) {
                String lineTmp = iterator.next();
                Matcher m = p.matcher(lineTmp);
                if (VAL_FIND_REGEX_FIND.equals(findMode) && m.find()) {
                    find = true;
                    line = lineTmp;
                } else {
                    if (VAL_FIND_REGEX_MATCH.equals(findMode) && m.matches()) {
                        find = true;
                        line = lineTmp;
                    }
                    else {
                        index++;
                    }
                }
            }
        }

        if (!find) {
            return "<html><title>KO</title>" + lineSeparator
                    + "<body>Error : Not find !</body>"
                    + lineSeparator + "</html>";
        }

        database.get(filename).remove(index);

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

    private String save(String filename, String paramAddTimeStamp) {
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
        boolean bParamAddTimestamp = bTimestamp;
        if (paramAddTimeStamp != null) {
        	bParamAddTimestamp = Boolean.valueOf(paramAddTimeStamp);
        }
        if (bParamAddTimestamp) {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat(
                    "yyyyMMdd'T'HH'h'mm'm'ss's.'");
            saveFilename = ft.format(dNow) + filename;
        }
        try {
            Iterator<String> it = database.get(filename).iterator();
            // add the charset to write the file
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(myDataDirectory,saveFilename)), myCharsetEncodingWriteFile));
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
                // add the charset to read the file
            	bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(f),myCharsetEncodingReadFile), 50 * 1024);
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
    
    private String showConfig() {
        StringBuffer sb = new StringBuffer(1024);
        boolean bLoadAndRunOnStartup = JMeterUtils.getPropDefault("jmeterPlugin.sts.loadAndRunOnStartup",false);
        sb.append("jmeterPlugin.sts.loadAndRunOnStartup=" + bLoadAndRunOnStartup + "<br />");
        sb.append("startFromCli=" + bStartFromMain + "<br />" + lineSeparator);
        sb.append("jmeterPlugin.sts.port=" + myPort + "<br />" + lineSeparator);
        sb.append("jmeterPlugin.sts.datasetDirectory=" + myDataDirectory + "<br />" + lineSeparator);
        sb.append("jmeterPlugin.sts.addTimestamp=" + bTimestamp + "<br />" + lineSeparator);
        sb.append("jmeterPlugin.sts.demon=" + bIsDemon + "<br />" + lineSeparator);
        sb.append("jmeterPlugin.sts.charsetEncodingHttpResponse=" + myCharsetEncodingHttpResponse + "<br />" + lineSeparator);
        sb.append("jmeterPlugin.sts.charsetEncodingReadFile=" + myCharsetEncodingReadFile + "<br />" + lineSeparator);
        sb.append("jmeterPlugin.sts.charsetEncodingWriteFile=" + myCharsetEncodingWriteFile + "<br />" + lineSeparator);
        
        String sInitFileAtStartup = JMeterUtils.getPropDefault("jmeterPlugin.sts.initFileAtStartup", "");
        sb.append("jmeterPlugin.sts.initFileAtStartup=" + sInitFileAtStartup + "<br />" + lineSeparator);
        
        boolean bInitFileAtStartupRegex = JMeterUtils.getPropDefault("jmeterPlugin.sts.initFileAtStartupRegex", false);
        sb.append("jmeterPlugin.sts.initFileAtStartupRegex=" + bInitFileAtStartupRegex + "<br />" + lineSeparator);
        sb.append("databaseIsEmpty=" + database.isEmpty() + "<br />" + lineSeparator);
        return "<html><title>OK</title>" + lineSeparator + "<body>"
                + lineSeparator + sb.toString() + "</body></html>";
    }

    @Override
    public void stopServer() {
        log.info("HTTP Simple Table Server is shutting down...");
        stop();
        if (bStartFromMain) {
        	log.info("... And Exit");
        	System.exit(0);
        }
    }

    public static void main(String[] args) {
        JMeterUtils.loadJMeterProperties("jmeter.properties");
        String dataset = JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.datasetDirectory",
                HttpSimpleTableControl.DEFAULT_DATA_DIR);
        
        String dataSetComplInfo = ", If DATASET_DIR==null then DATASET_DIR is <JMETER_HOME>/bin directory";
        if (dataset != null) {
        	dataSetComplInfo = "";
        }
        
        int port = JMeterUtils.getPropDefault("jmeterPlugin.sts.port",
                HttpSimpleTableControl.DEFAULT_PORT);
        
        boolean timestamp = JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.addTimestamp",
                HttpSimpleTableControl.DEFAULT_TIMESTAMP);
        
        boolean bIsDaemonProc = JMeterUtils.getPropDefault(
                "jmeterPlugin.sts.daemon",
                HttpSimpleTableControl.DEFAULT_DAEMON);
        
        String sDaemonProcFromSystProp = System.getProperty("jmeterPlugin.sts.daemon"); // -DjmeterPlugin.sts.daemon=true or false
        if (sDaemonProcFromSystProp != null) {
        	boolean bIsDaemonTmp = Boolean.parseBoolean(sDaemonProcFromSystProp);
        	bIsDaemonProc = bIsDaemonTmp;
        }

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
        
        // default level
        Configurator.setLevel(log.getName(), Level.INFO);
        // allow override by system properties
        
        String loglevelStr = JMeterUtils.getPropDefault(
                "loglevel", HttpSimpleTableControl.DEFAULT_LOG_LEVEL);
        System.out.println("loglevel=" + loglevelStr);
        
        //Configurator.setLevel(log.getName(), Level.toLevel(loglevelStr));
        Configurator.setRootLevel(Level.toLevel(loglevelStr));
        Configurator.setLevel(log.getName(), Level.toLevel(loglevelStr));
        bStartFromMain=true;
        
        HttpSimpleTableServer serv = new HttpSimpleTableServer(port, timestamp,
                dataset, charsetEncodingHttpResponse, charsetEncodingReadFile, charsetEncodingWriteFile, bIsDaemonProc);

        log.info("Creating HttpSimpleTable from CLI");
        log.info("------------------------------");
        log.info("SERVER_PORT : " + port);
        log.info("DATASET_DIR : " + dataset + dataSetComplInfo);
        log.info("ADD TIMESTAMP : " + timestamp);
        log.info("DEAMON PROCESS : " + bIsDaemonProc);
        log.info("charsetEncodingHttpResponse : " + charsetEncodingHttpResponse);
        log.info("charsetEncodingReadFile : " + charsetEncodingReadFile);
        log.info("charsetEncodingWriteFile : " + charsetEncodingWriteFile);
        log.info("------------------------------");
        log.info("STS_VERSION : " + STS_VERSION);
        ServerRunner.executeInstance(serv);
    }

    // only when start STS from Command Line Interface (script shell : simple-table-server.cmd or simple-table-server.sh)
    public void waitForKey() {
    	
    	try {
    		// load files at STS startup
			initFileAtStartup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if (!bIsDemon) {
	        log.warn("Hit Enter Key on keyboards to Stop and Exit");
	        try {
	            System.in.read();
	        } catch (Throwable ignored) {
	        }
    	} else { // mode daemon
    		log.warn("Mode daemon process, call 'http://hostname:port/sts/STOP' or kill this process to end the Http Simple Table Server");
    		boolean infiniteLoop = true;
    		while (infiniteLoop) {
    			try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					log.warn("Daemon process killed");
				}
    		}
    	}
    }
    
    public void initFileAtStartup() throws IOException {
        final String sInitFileAtStartup = JMeterUtils.getPropDefault("jmeterPlugin.sts.initFileAtStartup", "");
        boolean bInitFileAtStartupRegex = JMeterUtils.getPropDefault("jmeterPlugin.sts.initFileAtStartupRegex", false);
        if (sInitFileAtStartup.length() > 0) {
        	log.info("INITFILE at STS startup");
        	log.info("jmeterPlugin.sts.initFileAtStartup=" + sInitFileAtStartup);
        	log.info("jmeterPlugin.sts.initFileAtStartupRegex=" + bInitFileAtStartupRegex);
        }
        int port = JMeterUtils.getPropDefault("jmeterPlugin.sts.port",HttpSimpleTableControl.DEFAULT_PORT);
        if (sInitFileAtStartup.length() > 0 && bInitFileAtStartupRegex == false) {
            // E.g : jmeterPlugin.sts.initFileAtStartup=file1.csv,file2.csv,file3.csv
           
            String[] tabFileName = org.apache.commons.lang3.StringUtils.splitPreserveAllTokens(sInitFileAtStartup,',');
            for (int i = 0; i < tabFileName.length; i++) {
                String fileName = tabFileName[i].trim();
                log.info("INITFILE : i = " + i + ", fileName = " + fileName);
                URL url = new URL("http://localhost:" + port +"/sts/INITFILE?FILENAME=" + fileName);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        		String inputLine;
        		StringBuffer content = new StringBuffer();
        		while ((inputLine = in.readLine()) != null) {
        		    content.append(inputLine);
        		}
        		in.close();
        		con.disconnect();	
                
                log.info(url.toString() + ", response=" + content);
            }
        }
        
        if (sInitFileAtStartup.length() > 0 && bInitFileAtStartupRegex == true) {
            // E.g : jmeterPlugin.sts.initFileAtStartup=file\d+\.csv regex match : file1.csv, file2.csv, file3.csv, file44.csv ...
           
            String dataDir = JMeterUtils.getPropDefault("jmeterPlugin.sts.datasetDirectory", "."); // default <JMETER_HOME>/bin
            File fDir = new File(dataDir);
            File [] files = fDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.matches(sInitFileAtStartup);
                }
            });
        
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                log.info("INITFILE : i = " + i + ", fileName = " + fileName);
                URL url = new URL("http://localhost:" + port +"/sts/INITFILE?FILENAME=" + fileName);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        		String inputLine;
        		StringBuffer content = new StringBuffer();
        		while ((inputLine = in.readLine()) != null) {
        		    content.append(inputLine);
        		}
        		in.close();
        		con.disconnect();	
                
        		log.info(url.toString() + ", response=" + content);
            }
        }
    }
}