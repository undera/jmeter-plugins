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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author Felix Henry
 * @author Vincent Daburon
 */
public class TestHttpSimpleTableServer extends TestCase {
    private final String USER_AGENT = "Mozilla/5.0";
    private static final int HTTP_SERVER_PORT = 9191;
    private static final String DATA_DIR = System.getProperty("user.dir");
    private static final String CRLF = HttpSimpleTableServer.lineSeparator;

    public TestHttpSimpleTableServer(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(
                TestHttpSimpleTableServer.class)) {
            private HttpSimpleTableServer httpServer;

            @Override
            protected void setUp() throws Exception {
                httpServer = startHttpSimpleTableServer(HTTP_SERVER_PORT);
            }

            @Override
            protected void tearDown() throws Exception {
                // Shutdown the http server
                httpServer.stopServer();
                httpServer = null;
            }
        };
        return setup;
    }

    /**
     * Utility method to handle starting the HttpSimpleTableServer for testing.
     */
    public static HttpSimpleTableServer startHttpSimpleTableServer(int port)
            throws Exception {
        HttpSimpleTableServer server = null;
        server = new HttpSimpleTableServer(port, false, DATA_DIR);
        Exception except = null;
        try {
            server.start();
        } catch (IOException e) {
            except = e;
        }
        for (int i = 0; i < 10; i++) {// Wait up to 1 second
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            if (except != null) {// Already failed
                throw new Exception("Could not start sts on port: " + port
                        + ". " + except);
            }
            if (server.isAlive()) {
                break; // succeeded
            }
        }

        if (!server.isAlive()) {
            throw new Exception("Could not start sts on port: " + port);
        }
        return server;
    }

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

        // HELP (GET)
        String result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts");
        assertTrue(0 < result.length()
                && result
                        .startsWith("<html><head><title>URL for the dataset</title><head>"));

        // HELP (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT + "/sts/");
        assertTrue(0 < result.length()
                && result
                        .startsWith("<html><head><title>URL for the dataset</title><head>"));

        // STATUS (GET) : ERROR EMPTY DATABASE
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/STATUS");
        assertEquals("<html><title>KO</title>" + CRLF + "<body>"
                + "Error : Database was empty !</body>" + CRLF + "</html>",
                result);

        // INITFILE (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/INITFILE?FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF + "<body>2</body>" + CRLF
                + "</html>", result);

        // INITFILE (GET) : ERROR FILE NOT FOUND
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/INITFILE?FILENAME=unknown.txt");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : file not found !</body>" + CRLF + "</html>",
                result);

        // INITFILE (GET) : ERROR MISSING FILENAME
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/INITFILE");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // Delete the file test-login.csv
        File dataset = new File(DATA_DIR, filename);
        dataset.delete();

        // READ LAST KEEP=TRUE (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/READ?READ_MODE=LAST&FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF
                + "<body>login2;password2</body>" + CRLF + "</html>", result);

        // READ FIRST KEEP=FALSE (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/READ?READ_MODE=FIRST&KEEP=FALSE&FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF
                + "<body>login1;password1</body>" + CRLF + "</html>", result);

        // READ (GET) : ERROR UNKNOWN READ_MODE
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/READ?READ_MODE=SECOND&FILENAME=" + filename);
        assertEquals(
                "<html><title>KO</title>"
                        + CRLF
                        + "<body>Error : READ_MODE value has to be FIRST, LAST or RANDOM !</body>"
                        + CRLF + "</html>", result);

        // READ (GET) : ERROR MISSING FILENAME
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/READ?READ_MODE=LAST");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // READ (GET) : ERROR UNKNOWN FILENAME
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/READ?FILENAME=unexpected.txt");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : unexpected.txt not loaded yet !</body>" + CRLF
                + "</html>", result);

        // READ (GET) : ERROR UNKNOWN KEEP
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/READ?KEEP=NO&FILENAME=" + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : KEEP value has to be TRUE or FALSE !</body>"
                + CRLF + "</html>", result);

        // LENGTH (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/LENGTH?FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF + "<body>1</body>" + CRLF
                + "</html>", result);

        // LENGTH (POST)
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("FILENAME", filename));
        result = sendHttpPost("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/LENGTH", urlParameters);
        assertEquals("<html><title>OK</title>" + CRLF + "<body>1</body>" + CRLF
                + "</html>", result);

        // LENGTH (GET) ERROR FILE NOT FOUND
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/LENGTH?FILENAME=unknown.txt");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : unknown.txt not loaded yet !</body>" + CRLF
                + "</html>", result);

        // LENGTH (GET) ERROR MISSING FILENAME
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/LENGTH");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // ADD (POST)
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("ADD_MODE", "LAST"));
        urlParameters.add(new BasicNameValuePair("FILENAME", "test-login.csv"));
        urlParameters.add(new BasicNameValuePair("LINE", "login3;password3"));
        result = sendHttpPost("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/ADD", urlParameters);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        // ADD (GET) : ERROR ADD SHOULD USE POST METHOD
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/ADD?LINE=login4;password4&FILENAME=" + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : unknown command !</body>" + CRLF + "</html>",
                result);

        // ADD (POST) : ERROR MISSING LINE
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("ADD_MODE", "LAST"));
        urlParameters.add(new BasicNameValuePair("FILENAME", "test-login.csv"));
        result = sendHttpPost("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/ADD", urlParameters);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : LINE parameter was missing !</body>" + CRLF
                + "</html>", result);

        // ADD (POST) : MISSING ADD_MODE
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("FILENAME", "test-login.csv"));
        urlParameters.add(new BasicNameValuePair("LINE", "login4;password4"));
        result = sendHttpPost("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/ADD", urlParameters);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        // ADD (POST) : ERROR WRONG ADD MODE
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("ADD_MODE", "RANDOM"));
        urlParameters.add(new BasicNameValuePair("FILENAME", "test-login.csv"));
        urlParameters.add(new BasicNameValuePair("LINE", "login3;password3"));
        result = sendHttpPost("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/ADD", urlParameters);
        assertEquals(
                "<html><title>KO</title>"
                        + CRLF
                        + "<body>Error : ADD_MODE value has to be FIRST or LAST !</body>"
                        + CRLF + "</html>", result);

        // READ RANDOM KEEP=TRUE (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/READ?READ_MODE=RANDOM&FILENAME=" + filename);
        assertTrue(result.startsWith("<html><title>OK</title>"));

        // SAVE (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/SAVE?FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF + "<body>3</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR MAX SIZE REACHED
        result = sendHttpGet("http://localhost:"
                + HTTP_SERVER_PORT
                + "/sts/SAVE?FILENAME=aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeeffffffffffgggggggggghhhhhhhhhhiiiiiiiiiijjjjjjjjjjkkkkkkkkkkllllllllllmmmmmmmmmm.txt"
                + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Maximum size reached (128) !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL CHAR
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/SAVE?FILENAME=logins:passwords.csv");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL FILENAME .
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/SAVE?FILENAME=.");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // SAVE (GET) : ERROR ILLEGAL FILENAME ..
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/SAVE?FILENAME=..");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : Illegal character found !</body>" + CRLF
                + "</html>", result);

        // Delete the newly saved file test-login.csv
        dataset = new File(DATA_DIR, filename);
        dataset.delete();

        // RESET (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/RESET?FILENAME=" + filename);
        assertEquals("<html><title>OK</title>" + CRLF + "<body></body>" + CRLF
                + "</html>", result);

        // RESET (GET) ERROR MISSING FILENAME
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/RESET");
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : FILENAME parameter was missing !</body>"
                + CRLF + "</html>", result);

        // READ (GET) : ERROR LIST IS EMPTY
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/READ?FILENAME=" + filename);
        assertEquals("<html><title>KO</title>" + CRLF
                + "<body>Error : No more line !</body>" + CRLF + "</html>",
                result);

        // STATUS (GET)
        result = sendHttpGet("http://localhost:" + HTTP_SERVER_PORT
                + "/sts/STATUS");
        assertEquals("<html><title>OK</title>" + CRLF + "<body>" + CRLF
                + filename + " = 0<br />" + CRLF + "</body></html>", result);
    }

    private String sendHttpGet(String url) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse resp = client.execute(request);
        HttpEntity resp_entity = resp.getEntity();
        String result = EntityUtils.toString(resp_entity);
        return result;
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
}
