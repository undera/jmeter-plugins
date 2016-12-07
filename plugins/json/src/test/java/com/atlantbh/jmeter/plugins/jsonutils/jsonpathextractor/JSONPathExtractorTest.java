/*
 * Copyright 2013 undera.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atlantbh.jmeter.plugins.jsonutils.jsonpathextractor;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONPathExtractorTest {
    private static final String json = "{ \"store\": {\n" +
            "    \"book\": [ \n" +
            "      { \"category\": \"reference\",\n" +
            "        \"author\": \"Nigel Rees\",\n" +
            "        \"title\": \"Sayings of the Century\",\n" +
            "        \"price\": 8.95\n" +
            "      },\n" +
            "      { \"category\": \"fiction\",\n" +
            "        \"author\": \"Evelyn Waugh\",\n" +
            "        \"title\": \"Sword of Honour\",\n" +
            "        \"price\": 12.99\n" +
            "      },\n" +
            "      { \"category\": \"fiction\",\n" +
            "        \"author\": \"Herman Melville\",\n" +
            "        \"title\": \"Moby Dick\",\n" +
            "        \"isbn\": \"0-553-21311-3\",\n" +
            "        \"price\": 8.99\n" +
            "      },\n" +
            "      { \"category\": \"fiction\",\n" +
            "        \"author\": \"J. R. R. Tolkien\",\n" +
            "        \"title\": \"The Lord of the Rings\",\n" +
            "        \"isbn\": \"0-395-19395-8\",\n" +
            "        \"price\": 22.99\n" +
            "      }\n" +
            "    ],\n" +
            "    \"bicycle\": {\n" +
            "      \"color\": \"red\",\n" +
            "      \"price\": 19.95\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private static final String json2 = "{\n" +
            "  \"status\": \"success\",\n" +
            "  \"data\": {\n" +
            "    \"groups\": [\n" +
            "      {\n" +
            "        \"id\": \"e02991f4-a95d-43dd-8eb0-fbc44349e238\",\n" +
            "        \"name\": \"Uber\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"71bc2d86-b023-44ca-b358-52531bd57ab3\",\n" +
            "        \"name\": \"Hooey\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"378e9b20-99bb-4d1f-bf2c-6a4a6c69a8ed\",\n" +
            "        \"name\": \"Zaz\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"296453c7-379b-4694-8cc2-5ca44afcb0a4\",\n" +
            "        \"name\": \"Zompek\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"46834f01-6b5f-4b35-bd34-e96aa9cbe315\",\n" +
            "        \"name\": \"Asguard\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    private static final String json3 = "{\n" +
            " \"data\":[\n" +
            "   {\"attr\":{\"value\":0}},\n" +
            "   {\"attr\":{\"value\":1}},\n" +
            "   {\"attr\":{\"value\":2}},\n" +
            "   {\"attr\":{\"value\":0}},\n" +
            " ]\n" +
            "}";

    @Before
    public void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void testGetJsonPath() {
        System.out.println("getJsonPath");
        JSONPathExtractor instance = new JSONPathExtractor();
        String expResult = "";
        String result = instance.getJsonPath();
        assertEquals(expResult, result);

    }

    @Test
    public void testSetJsonPath() {
        System.out.println("setJsonPath");
        String jsonPath = "";
        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setJsonPath(jsonPath);
    }

    @Test
    public void testGetVar() {
        System.out.println("getVar");
        JSONPathExtractor instance = new JSONPathExtractor();
        String expResult = "";
        String result = instance.getVar();
        assertEquals(expResult, result);
    }

    @Test
    public void testProcess_default() {
        System.out.println("process def");
        JMeterContext context = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        context.setPreviousResult(res);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setDefaultValue("DEFAULT");
        instance.setVar("test");
        instance.process();
        JMeterVariables vars = context.getVariables();
        assertEquals("DEFAULT", vars.get("test"));
    }

    @Test
    public void testProcess() {
        System.out.println("process");
        JMeterContext context = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData(json.getBytes());
        context.setPreviousResult(res);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setDefaultValue("DEFAULT");
        instance.setVar("test");
        instance.setJsonPath("$.store.book[*].author");
        instance.process();
        JMeterVariables vars = context.getVariables();
        assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\",\"Herman Melville\",\"J. R. R. Tolkien\"]", vars.get("test"));
    }

    @Test
    public void testProcess_chinese() {
        JMeterContext context = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        String chinese = "{\"carBrandName\":\"大众\"}";

        res.setResponseData(chinese.getBytes());
        context.setPreviousResult(res);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setDefaultValue("DEFAULT");
        instance.setVar("test");
        instance.setJsonPath("$.carBrandName");
        instance.process();
        JMeterVariables vars = context.getVariables();
        // freaking "static final" DEFAULT_ENCODING field in SampleResult does not allow us to assert this
        // assertEquals("大众", vars.get("test"));
    }

    @Test
    public void testProcess_from_var() {
        System.out.println("process fromvar");
        JMeterContext context = JMeterContextService.getContext();
        JMeterVariables vars = context.getVariables();

        SampleResult res = new SampleResult();
        res.setResponseData("".getBytes());
        context.setPreviousResult(res);

        vars.put("SVAR", json);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setDefaultValue("DEFAULT");
        instance.setVar("test");
        instance.setJsonPath("$.store.book[*].author");
        instance.setSubject(JSONPathExtractor.SUBJECT_VARIABLE);
        instance.setSrcVariableName("SVAR");
        instance.process();
        assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\",\"Herman Melville\",\"J. R. R. Tolkien\"]", vars.get("test"));
    }

    @Test
    public void testProcess_list() {
        System.out.println("process list");
        JMeterContext context = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData("{\"myval\": [{\"test\":1},{\"test\":{\"dict\":1}},{\"test\":null}]}".getBytes());
        context.setPreviousResult(res);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setDefaultValue("DEFAULT");
        instance.setVar("test");
        instance.setJsonPath("$.myval[*].test");
        instance.process();
        JMeterVariables vars = context.getVariables();
        assertEquals("[1,{\"dict\":1},null]", vars.get("test"));
        assertEquals("1", vars.get("test_1"));
        assertEquals("{\"dict\":1}", vars.get("test_2"));
        assertEquals("null", vars.get("test_3"));

        // test for cleaning prev vars
        res.setResponseData("{\"myval\": [{\"test\":1},{\"test\":2}]}".getBytes());
        instance.process();
        assertEquals("[1,2]", vars.get("test"));
        assertEquals("1", vars.get("test_1"));
        assertEquals("2", vars.get("test_2"));
        assertEquals(null, vars.get("test_3"));
    }

    @Test
    public void testReported1() {
        System.out.println("process reported");
        JMeterContext context = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData(json2.getBytes());
        context.setPreviousResult(res);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setVar("GroupID");
        instance.setJsonPath("$.data.groups[?(@.name=='Zaz')].id");
        instance.setDefaultValue("NOTFOUND");
        instance.process();
        JMeterVariables vars = context.getVariables();
        assertNotEquals("NOTFOUND", vars.get("GroupID"));
        assertEquals("378e9b20-99bb-4d1f-bf2c-6a4a6c69a8ed", vars.get("GroupID_1"));
    }

    @Test
    public void testReported1_1() {
        System.out.println("process reported");
        JMeterContext context = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData(json2.getBytes());
        context.setPreviousResult(res);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setVar("GroupID");
        instance.setJsonPath("$.data.groups[*].id");
        instance.setDefaultValue("NOTFOUND");
        instance.process();
        JMeterVariables vars = context.getVariables();
        assertNotEquals("NOTFOUND", vars.get("GroupID"));
        assertEquals("e02991f4-a95d-43dd-8eb0-fbc44349e238", vars.get("GroupID_1"));
    }

    @Test
    public void testReported1_3() {
        System.out.println("process reported");
        JMeterContext context = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData(json2.getBytes());
        context.setPreviousResult(res);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setVar("GroupID");
        instance.setJsonPath("$.data.groups[?(@.name==Avtovaz)].id");
        instance.setDefaultValue("NOTFOUND");
        instance.process();
        JMeterVariables vars = context.getVariables();
        assertEquals("NOTFOUND", vars.get("GroupID"));
    }

    @Ignore
    @Test // FIXME: we need to solve this one day
    public void testReported2() {
        System.out.println("process reported");
        JMeterContext context = JMeterContextService.getContext();
        SampleResult res = new SampleResult();
        res.setResponseData(json3.getBytes());
        context.setPreviousResult(res);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setVar("var");
        instance.setJsonPath("$.data[?(@.attr.value>0)][0].attr");
        instance.setDefaultValue("NOTFOUND");
        instance.process();
        JMeterVariables vars = context.getVariables();
        assertNotEquals("NOTFOUND", vars.get("var"));
        assertEquals("{value=1}", vars.get("var"));
    }

    @Test
    public void testProcess_from_var_2() {
        System.out.println("process fromvar");
        JMeterContext context = JMeterContextService.getContext();
        JMeterVariables vars = context.getVariables();

        SampleResult res = new SampleResult();
        res.setResponseData("".getBytes());
        context.setPreviousResult(res);

        vars.put("SVAR", json);

        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setDefaultValue("DEFAULT");
        instance.setVar("test");
        instance.setJsonPath("$.store.bicycle");
        instance.setSubject(JSONPathExtractor.SUBJECT_VARIABLE);
        instance.setSrcVariableName("SVAR");
        instance.process();
        String test = vars.get("test");
        boolean thiis = "{\"color\":\"red\",\"price\":19.95}".equals(test);
        boolean thaat = "{\"price\":19.95,\"color\":\"red\"}".equals(test);
        assertTrue(thiis || thaat);
    }
}