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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    public void testSetVar() {
        System.out.println("setVar");
        String var = "";
        JSONPathExtractor instance = new JSONPathExtractor();
        instance.setVar(var);
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
}
