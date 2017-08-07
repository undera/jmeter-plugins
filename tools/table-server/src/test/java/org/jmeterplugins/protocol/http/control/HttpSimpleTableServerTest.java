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

import kg.apc.emulators.TestJMeterUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpSimpleTableServerTest {

    private final String DATA_DIR;
    private static final String CRLF = HttpSimpleTableServer.lineSeparator;

    public HttpSimpleTableServerTest() {
        DATA_DIR = TestJMeterUtils.getTempDir();
    }

    @Test
    public void testGetRequest() throws Exception {
        // create a file to test the STS
        BufferedWriter out;
        String filename = "test-login.csv";
        out = new BufferedWriter(new FileWriter(new File(DATA_DIR, filename)));
        out.write("login1;password1");
        out.write(CRLF);
        out.write("login2;password2");
        out.write(CRLF);

        out.close();


        HttpSimpleTableServer obj = new HttpSimpleTableServerEmul(-1, true, DATA_DIR);

        // HELP (GET)
        String result = sendHttpGet(obj, ""
                + "/sts");
        assertTrue(0 < result.length()
                && result
                .startsWith("<html><head><title>URL for the dataset</title><head>"));

        // HELP (GET)
        result = sendHttpGet(obj, "" + "/sts/");
        assertTrue(0 < result.length()
                && result
                .startsWith("<html><head><title>URL for the dataset</title><head>"));

        // STATUS (GET) : ERROR EMPTY DATABASE
        result = sendHttpGet(obj, ""
                + "/sts/STATUS");
        assertEquals("<html><title>KO</title>" + CRLF + "<body>"
                        + "Error : Database was empty !</body>" + CRLF + "</html>",
                result);

        // INITFILE (GET)
        result = sendHttpGet(obj, "/sts/INITFILE", this.createParm("FILENAME", filename));
        assertEquals("<html><title>OK</title>" + CRLF + "<body>2</body>" + CRLF
                + "</html>", result);

        // INITFILE (GET) : ERROR FILE NOT FOUND
        result = sendHttpGet(obj, "/sts/INITFILE", this.createParm("FILENAME", "unknown.txt"));
        assertEquals("<html><title>KO</title>" + CRLF
                        + "<body>Error : file not found !</body>" + CRLF + "</html>",
                result);

        // INITFILE (GET) : ERROR MISSING FILENAME
        result = sendHttpGet(obj, "/sts/INITFILE", new HashMap<String, String>());
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // Delete the file test-login.csv
        File dataset = new File(DATA_DIR, filename);
        dataset.delete();

        // READ LAST KEEP=TRUE (GET)
        Map<String, String> map1 = this.createParm("FILENAME", filename);
        map1.put("READ_MODE", "LAST");
        result = sendHttpGet(obj, "/sts/READ", map1);
        assertEquals("<html><title>OK</title>" + CRLF
                + "<body>login2;password2</body>" + CRLF + "</html>", result);

        // READ FIRST KEEP=FALSE (GET)
        Map<String, String> map2 = this.createParm("FILENAME", filename);
        map2.put("READ_MODE", "FIRST");
        map2.put("KEEP", "FALSE");
        result = sendHttpGet(obj, "/sts/READ", map2);
        assertEquals("<html><title>OK</title>" + CRLF
                + "<body>login1;password1</body>" + CRLF + "</html>", result);

        // READ (GET) : ERROR UNKNOWN READ_MODE
        Map<String, String> map3 = this.createParm("FILENAME", filename);
        map3.put("READ_MODE", "SECOND");
        result = sendHttpGet(obj, "/sts/READ", map3);
        assertEquals(
                "<html><title>KO</title>"
                        + CRLF
                        + "<body>Error : READ_MODE value has to be FIRST, LAST or RANDOM !</body>"
                        + CRLF + "</html>", result);

        // READ (GET) : ERROR MISSING FILENAME
        Map<String, String> map4 = this.createParm("A", filename);
        map4.put("READ_MODE", "LAST");
        result = sendHttpGet(obj, "/sts/READ", map4);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // READ (GET) : ERROR UNKNOWN FILENAME
        result = sendHttpGet(obj, "/sts/READ", this.createParm("FILENAME", "unexpected.txt"));
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : unexpected.txt not loaded yet !</body>" + CRLF
                + "</html>", result);

        // READ (GET) : ERROR UNKNOWN KEEP
        Map<String, String> map5 = this.createParm("FILENAME", filename);
        map5.put("KEEP", "NO");
        result = sendHttpGet(obj, "/sts/READ", map5);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : KEEP value has to be TRUE or FALSE !</body>"
                + CRLF + "</html>", result);

        // LENGTH (GET)
        result = sendHttpGet(obj, "/sts/LENGTH", this.createParm("FILENAME", filename));
        assertEquals("<html><title>OK</title>" + CRLF + "<body>1</body>" + CRLF
                + "</html>", result);

        // LENGTH (POST)
        result = sendHttpPost(obj, "/sts/LENGTH", this.createParm("FILENAME", filename));
        assertEquals("<html><title>OK</title>" + CRLF + "<body>1</body>" + CRLF
                + "</html>", result);

        // LENGTH (GET) ERROR FILE NOT FOUND
        result = sendHttpGet(obj, "/sts/LENGTH", this.createParm("FILENAME", "unknown.txt"));
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : unknown.txt not loaded yet !</body>" + CRLF
                + "</html>", result);

        // LENGTH (GET) ERROR MISSING FILENAME
        result = sendHttpGet(obj, "/sts/LENGTH", this.createParm("A", "unknown.txt"));
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // ADD (POST)
        Map<String, String> urlParameters = this.createParm("FILENAME", "unknown.txt");
        urlParameters.put("ADD_MODE", "LAST");
        urlParameters.put("FILENAME", "test-login.csv");
        urlParameters.put("LINE", "login3;password3");
        result = sendHttpPost(obj, "/sts/ADD", urlParameters);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        urlParameters.put("UNIQUE", "TRUE");
        result = sendHttpPost(obj, "/sts/ADD", urlParameters);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : ENTRY already exists !</body>"
                + CRLF + "</html>", result);

        urlParameters.put("UNIQUE", "FALSE");
        result = sendHttpPost(obj, "/sts/ADD", urlParameters);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>"
                + CRLF + "</html>", result);

        urlParameters.put("UNIQUE", "UNKNOWN");
        result = sendHttpPost(obj, "/sts/ADD", urlParameters);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : UNIQUE value has to be TRUE or FALSE !</body>"
                + CRLF + "</html>", result);

        // ADD (GET) : ERROR ADD SHOULD USE POST METHOD
        result = sendHttpGet(obj, ""
                + "/sts/ADD?LINE=login4;password4&FILENAME=" + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                        + "<body>Error : unknown command !</body>" + CRLF + "</html>",
                result);

        // ADD (POST) : ERROR MISSING LINE
        Map<String, String> urlParameters2 = this.createParm("FILENAME", "unknown.txt");
        urlParameters2.put("ADD_MODE", "LAST");
        urlParameters2.put("FILENAME", "test-login.csv");
        result = sendHttpPost(obj, "/sts/ADD", urlParameters2);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : LINE parameter was missing !</body>" + CRLF
                + "</html>", result);

        // ADD (POST) : MISSING ADD_MODE
        Map<String, String> urlParameters3 = this.createParm("FILENAME", "unknown.txt");
        urlParameters3.put("FILENAME", "test-login.csv");
        urlParameters3.put("LINE", "login3;password3");
        result = sendHttpPost(obj, "/sts/ADD", urlParameters3);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        // ADD (POST) : ERROR WRONG ADD MODE
        Map<String, String> urlParameters4 = this.createParm("FILENAME", "unknown.txt");
        urlParameters4.put("ADD_MODE", "RANDOM");
        urlParameters4.put("FILENAME", "test-login.csv");
        urlParameters4.put("LINE", "login3;password3");

        result = sendHttpPost(obj, "/sts/ADD", urlParameters4);
        assertEquals(
                "<html><title>KO</title>"
                        + CRLF
                        + "<body>Error : ADD_MODE value has to be FIRST or LAST !</body>"
                        + CRLF + "</html>", result);

        // READ RANDOM KEEP=TRUE (GET)
        Map<String, String> urlParameters5 = this.createParm("FILENAME", filename);
        urlParameters4.put("READ_MODE", "RANDOM");
        result = sendHttpGet(obj, "/sts/READ", urlParameters5);
        assertTrue(result.startsWith("<html><title>OK</title>"));

        // SAVE (GET)
        result = sendHttpGet(obj, "/sts/SAVE", this.createParm("FILENAME", filename));
        assertEquals("<html><title>OK</title>" + CRLF + "<body>4</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR MAX SIZE REACHED

        result = sendHttpGet(obj, "/sts/SAVE", this.createParm("FILENAME", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeeffffffffffgggggggggghhhhhhhhhhiiiiiiiiiijjjjjjjjjjkkkkkkkkkkllllllllllmmmmmmmmmm.txt"
                + filename));
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Maximum size reached (128) !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL CHAR
        result = sendHttpGet(obj, "/sts/SAVE", this.createParm("FILENAME", "logins:passwords.csv"));
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL FILENAME .
        result = sendHttpGet(obj, "/sts/SAVE", this.createParm("FILENAME", "."));
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL FILENAME ..
        result = sendHttpGet(obj, "/sts/SAVE", this.createParm("FILENAME", ".."));
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // Delete the newly saved file test-login.csv
        dataset = new File(DATA_DIR, filename);
        dataset.delete();

        // RESET (GET)
        result = sendHttpGet(obj, "/sts/RESET", this.createParm("FILENAME", filename));
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        // RESET (GET) ERROR MISSING FILENAME
        result = sendHttpGet(obj, "/sts/RESET", this.createParm("A", filename));
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // READ (GET) : ERROR LIST IS EMPTY
        result = sendHttpGet(obj, "/sts/READ", this.createParm("FILENAME", filename));
        assertEquals("<html><title>KO</title>" + CRLF
                        + "<body>Error : No more line !</body>" + CRLF + "</html>",
                result);

        // STATUS (GET)
        result = sendHttpGet(obj, "/sts/STATUS");
        assertEquals("<html><title>OK</title>" + CRLF + "<body>" + CRLF
                + filename + " = 0<br />" + CRLF + "</body></html>", result);
    }


    private Map<String, String> createParm(String filename, String filename1) {
        Map<String, String> res = new HashMap<String, String>();
        res.put(filename, filename1);
        return res;
    }

    private String sendHttpGet(HttpSimpleTableServer obj, String s, Map<String, String> params) throws IOException {
        SessionEmulator sess = new SessionEmulator(s);

        if (params != null) {
            sess.setParms(params);
        }
        NanoHTTPD.Response resp = obj.serve(sess);
        InputStream inputStream = resp.getData();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        return writer.toString();
    }

    private String sendHttpGet(HttpSimpleTableServer obj, String url) throws Exception {
        return sendHttpGet(obj, url, null);
    }

    private String sendHttpPost(HttpSimpleTableServer obj, String url, Map<String, String> parms)
            throws Exception {
        SessionEmulator sess = new SessionEmulator(url);
        sess.setMethod(NanoHTTPD.Method.POST);
        sess.setBody((parms));
        NanoHTTPD.Response resp = obj.serve(sess);
        InputStream inputStream = resp.getData();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        return writer.toString();
    }

    private class SessionEmulator implements NanoHTTPD.IHTTPSession {
        private final String url;
        private Map<String, String> parms;
        private NanoHTTPD.Method method;
        private Map<String, String> body;

        public SessionEmulator(String url) {
            this.url = url;
        }

        @Override
        public void execute() throws IOException {

        }

        @Override
        public Map<String, String> getParms() {
            return this.parms;
        }

        @Override
        public Map<String, String> getHeaders() {
            return null;
        }

        @Override
        public String getUri() {
            return this.url;
        }

        @Override
        public String getQueryParameterString() {
            return null;
        }

        @Override
        public NanoHTTPD.Method getMethod() {
            return this.method;
        }

        @Override
        public InputStream getInputStream() {
            return null;
        }

        @Override
        public NanoHTTPD.CookieHandler getCookies() {
            return null;
        }

        @Override
        public void parseBody(Map<String, String> files) throws IOException, NanoHTTPD.ResponseException {
            files.putAll(this.body);
            parms = body;
        }

        public void setParms(Map<String, String> parms) {
            this.parms = parms;
        }

        public void setMethod(NanoHTTPD.Method method) {
            this.method = method;
        }

        public void setBody(Map<String, String> body) {
            this.body = body;
        }
    }
}
