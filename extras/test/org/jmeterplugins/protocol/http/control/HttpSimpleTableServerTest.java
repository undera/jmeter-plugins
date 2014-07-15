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

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Felix Henry
 * @author Vincent Daburon
 */
public class HttpSimpleTableServerTest extends TestCase {
    private final String USER_AGENT = "Mozilla/5.0";
    private static final int HTTP_SERVER_PORT = -1;
    private static final String DATA_DIR = System.getProperty("user.dir");
    private static final String CRLF = HttpSimpleTableServer.lineSeparator;


    public void testGetRequest() throws Exception {
        // create a file to test the STS
        BufferedWriter out = null;
        String filename = "test-login.csv";
        out = new BufferedWriter(new FileWriter(new File(DATA_DIR, filename)));
        out.write("login1;password1");
        out.write(CRLF);
        out.write("login2;password2");
        out.write(CRLF);
        if (null != out) {
            out.close();
        }

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
        result = sendHttpGet(obj, ""
                + "/sts/INITFILE?FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF + "<body>2</body>" + CRLF
                + "</html>", result);

        // INITFILE (GET) : ERROR FILE NOT FOUND
        result = sendHttpGet(obj, ""
                + "/sts/INITFILE?FILENAME=unknown.txt");
        assertEquals("<html><title>KO</title>" + CRLF
                        + "<body>Error : file not found !</body>" + CRLF + "</html>",
                result);

        // INITFILE (GET) : ERROR MISSING FILENAME
        result = sendHttpGet(obj, ""
                + "/sts/INITFILE");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // Delete the file test-login.csv
        File dataset = new File(DATA_DIR, filename);
        dataset.delete();

        // READ LAST KEEP=TRUE (GET)
        result = sendHttpGet(obj, ""
                + "/sts/READ?READ_MODE=LAST&FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF
                + "<body>login2;password2</body>" + CRLF + "</html>", result);

        // READ FIRST KEEP=FALSE (GET)
        result = sendHttpGet(obj, ""
                + "/sts/READ?READ_MODE=FIRST&KEEP=FALSE&FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF
                + "<body>login1;password1</body>" + CRLF + "</html>", result);

        // READ (GET) : ERROR UNKNOWN READ_MODE
        result = sendHttpGet(obj, ""
                + "/sts/READ?READ_MODE=SECOND&FILENAME=" + filename);
        assertEquals(
                "<html><title>KO</title>"
                        + CRLF
                        + "<body>Error : READ_MODE value has to be FIRST, LAST or RANDOM !</body>"
                        + CRLF + "</html>", result);

        // READ (GET) : ERROR MISSING FILENAME
        result = sendHttpGet(obj, ""
                + "/sts/READ?READ_MODE=LAST");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // READ (GET) : ERROR UNKNOWN FILENAME
        result = sendHttpGet(obj, ""
                + "/sts/READ?FILENAME=unexpected.txt");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : unexpected.txt not loaded yet !</body>" + CRLF
                + "</html>", result);

        // READ (GET) : ERROR UNKNOWN KEEP
        result = sendHttpGet(obj, ""
                + "/sts/READ?KEEP=NO&FILENAME=" + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : KEEP value has to be TRUE or FALSE !</body>"
                + CRLF + "</html>", result);

        // LENGTH (GET)
        result = sendHttpGet(obj, ""
                + "/sts/LENGTH?FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF + "<body>1</body>" + CRLF
                + "</html>", result);

        // LENGTH (POST)
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("FILENAME", filename));
        result = sendHttpPost(""
                + "/sts/LENGTH", urlParameters);
        assertEquals("<html><title>OK</title>" + CRLF + "<body>1</body>" + CRLF
                + "</html>", result);

        // LENGTH (GET) ERROR FILE NOT FOUND
        result = sendHttpGet(obj, ""
                + "/sts/LENGTH?FILENAME=unknown.txt");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : unknown.txt not loaded yet !</body>" + CRLF
                + "</html>", result);

        // LENGTH (GET) ERROR MISSING FILENAME
        result = sendHttpGet(obj, ""
                + "/sts/LENGTH");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // ADD (POST)
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("ADD_MODE", "LAST"));
        urlParameters.add(new BasicNameValuePair("FILENAME", "test-login.csv"));
        urlParameters.add(new BasicNameValuePair("LINE", "login3;password3"));
        result = sendHttpPost(""
                + "/sts/ADD", urlParameters);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        // ADD (GET) : ERROR ADD SHOULD USE POST METHOD
        result = sendHttpGet(obj, ""
                + "/sts/ADD?LINE=login4;password4&FILENAME=" + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                        + "<body>Error : unknown command !</body>" + CRLF + "</html>",
                result);

        // ADD (POST) : ERROR MISSING LINE
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("ADD_MODE", "LAST"));
        urlParameters.add(new BasicNameValuePair("FILENAME", "test-login.csv"));
        result = sendHttpPost(""
                + "/sts/ADD", urlParameters);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : LINE parameter was missing !</body>" + CRLF
                + "</html>", result);

        // ADD (POST) : MISSING ADD_MODE
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("FILENAME", "test-login.csv"));
        urlParameters.add(new BasicNameValuePair("LINE", "login4;password4"));
        result = sendHttpPost(""
                + "/sts/ADD", urlParameters);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        // ADD (POST) : ERROR WRONG ADD MODE
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("ADD_MODE", "RANDOM"));
        urlParameters.add(new BasicNameValuePair("FILENAME", "test-login.csv"));
        urlParameters.add(new BasicNameValuePair("LINE", "login3;password3"));
        result = sendHttpPost(""
                + "/sts/ADD", urlParameters);
        assertEquals(
                "<html><title>KO</title>"
                        + CRLF
                        + "<body>Error : ADD_MODE value has to be FIRST or LAST !</body>"
                        + CRLF + "</html>", result);

        // READ RANDOM KEEP=TRUE (GET)
        result = sendHttpGet(obj, ""
                + "/sts/READ?READ_MODE=RANDOM&FILENAME=" + filename);
        assertTrue(result.startsWith("<html><title>OK</title>"));

        // SAVE (GET)
        result = sendHttpGet(obj, ""
                + "/sts/SAVE?FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF + "<body>3</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR MAX SIZE REACHED
        result = sendHttpGet(obj, "http://localhost:"
                + HTTP_SERVER_PORT
                + "/sts/SAVE?FILENAME=aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeeffffffffffgggggggggghhhhhhhhhhiiiiiiiiiijjjjjjjjjjkkkkkkkkkkllllllllllmmmmmmmmmm.txt"
                + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Maximum size reached (128) !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL CHAR
        result = sendHttpGet(obj, ""
                + "/sts/SAVE?FILENAME=logins:passwords.csv");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL FILENAME .
        result = sendHttpGet(obj, ""
                + "/sts/SAVE?FILENAME=.");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL FILENAME ..
        result = sendHttpGet(obj, ""
                + "/sts/SAVE?FILENAME=..");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // Delete the newly saved file test-login.csv
        dataset = new File(DATA_DIR, filename);
        dataset.delete();

        // RESET (GET)
        result = sendHttpGet(obj, ""
                + "/sts/RESET?FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        // RESET (GET) ERROR MISSING FILENAME
        result = sendHttpGet(obj, ""
                + "/sts/RESET");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // READ (GET) : ERROR LIST IS EMPTY
        result = sendHttpGet(obj, ""
                + "/sts/READ?FILENAME=" + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                        + "<body>Error : No more line !</body>" + CRLF + "</html>",
                result);

        // STATUS (GET)
        result = sendHttpGet(obj, ""
                + "/sts/STATUS");
        assertEquals("<html><title>OK</title>" + CRLF + "<body>" + CRLF
                + filename + " = 0<br />" + CRLF + "</body></html>", result);
    }

    private String sendHttpGet(HttpSimpleTableServer obj, String url) throws Exception {

        NanoHTTPD.IHTTPSession sess = new SessionEmulator(url);
        NanoHTTPD.Response resp = obj.serve(sess);
        InputStream inputStream = resp.getData();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        String resp_entity = writer.toString();
        return resp_entity;
    }

    private String sendHttpPost(String url, List<NameValuePair> parms)
            throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        post.setEntity(new UrlEncodedFormEntity(parms, "UTF-8"));
        HttpResponse resp = client.execute(post);
        HttpEntity resp_entity = resp.getEntity();
        String result = EntityUtils.toString(resp_entity);
        return result;
    }

    private class SessionEmulator implements NanoHTTPD.IHTTPSession {
        private final String url;

        public SessionEmulator(String url) {
            this.url = url;
        }

        @Override
        public void execute() throws IOException {

        }

        @Override
        public Map<String, String> getParms() {
            return null;
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
            return null;
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

        }
    }
}
