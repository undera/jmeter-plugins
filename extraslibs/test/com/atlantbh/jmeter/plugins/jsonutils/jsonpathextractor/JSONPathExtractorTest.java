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

import static org.junit.Assert.assertEquals;
import kg.apc.emulators.TestJMeterUtils;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.junit.Before;
import org.junit.Test;

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
             "        \"isbn\": \"0553213113\",\n" +
             "        \"price\": 8.99\n" +
             "      },\n" +
             "      { \"category\": \"fiction\",\n" +
             "        \"author\": \"J. R. R. Tolkien\",\n" +
             "        \"title\": \"The Lord of the Rings\",\n" +
             "        \"isbn\": \"0395193958\",\n" +
             "        \"price\": 22.99\n" +
             "      }\n" +
             "    ],\n" +
             "    \"bicycle\": {\n" +
             "      \"color\": \"red\",\n" +
             "      \"price\": 19.95\n" +
             "    }\n" +
             "  }\n" +
             "}";
	private static final String jsonArray = "[" + "{\"id\": \"123\", \"value\": 123}," + "{\"id\": \"456\", \"value\": \"String\"}," + "]";

	@Before
	public void setUpClass() throws Exception {
		TestJMeterUtils.createJmeterEnv();
	}

	@Test
	public void testJsonPathProperty() {
		JSONPathExtractor instance = new JSONPathExtractor();
		instance.setJsonPath("");
		assertEquals("", instance.getJsonPath());

		instance.setJsonPath("$.json.path");
		assertEquals("$.json.path", instance.getJsonPath());
	}

	@Test
	public void testVarProperty() {
		JSONPathExtractor instance = new JSONPathExtractor();
		assertEquals("", instance.getVar());

		instance.setVar("test");
		assertEquals("test", instance.getVar());
	}

	@Test
	public void testProcess_default() {
		assertEquals("DEFAULT", extractJson(json, null, "DEFAULT"));
	}

	@Test
	public void testProcess_ArrayResult() {
		assertEquals("[\"Nigel Rees\",\"Evelyn Waugh\",\"Herman Melville\",\"J. R. R. Tolkien\"]",
				extractJson(json, "$.store.book[*].author"));
	}

	@Test
	public void testProcess_SingleResult() {
		assertEquals("Nigel Rees", extractJson(json, "$.store.book[0].author"));
	}

	@Test
	public void testSingleResultFromArray() {
		assertEquals("123", extractJson(jsonArray, "$[?(@.id == '123')].value"));
		assertEquals("String", extractJson(jsonArray, "$[?(@.id == '456')].value"));
	}

	@Test
	public void testMultipleResultsFromArray() {
		assertEquals("[123,\"String\"]", extractJson(jsonArray, "$[?(@.id <> '')].value"));
	}

	protected String extractJson(String json, String jsonPath) {
		return extractJson(json, jsonPath, null);
	}

	protected String extractJson(String json, String jsonPath, String defaultValue) {
		JMeterContext context = JMeterContextService.getContext();
		SampleResult res = new SampleResult();
		res.setResponseData(json.getBytes());
		context.setPreviousResult(res);

		JSONPathExtractor instance = new JSONPathExtractor();
		if (defaultValue != null) {
			instance.setDefaultValue(defaultValue);
		}
		instance.setVar("test");
		if (jsonPath != null) {
			instance.setJsonPath(jsonPath);
		}
		instance.process();

		return context.getVariables().get("test");
	}
}
