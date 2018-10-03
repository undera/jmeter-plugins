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
package com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion;

import org.apache.commons.io.FileUtils;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSONPathAssertionTest {

    @Test
    public void testGetJsonPath() {
        System.out.println("getJsonPath");
        JSONPathAssertion instance = new JSONPathAssertion();
        String expResult = "";
        String result = instance.getJsonPath();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetJsonPath() {
        System.out.println("setJsonPath");
        String jsonPath = "";
        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath(jsonPath);
    }

    @Test
    public void testGetExpectedValue() {
        System.out.println("getExpectedValue");
        JSONPathAssertion instance = new JSONPathAssertion();
        String expResult = "";
        String result = instance.getExpectedValue();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetExpectedValue() {
        System.out.println("setExpectedValue");
        String expectedValue = "";
        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setExpectedValue(expectedValue);
    }

    @Test
    public void testSetJsonValidationBool() {
        System.out.println("setJsonValidationBool");
        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonValidationBool(false);
    }

    @Test
    public void testIsJsonValidationBool() {
        System.out.println("isJsonValidationBool");
        JSONPathAssertion instance = new JSONPathAssertion();
        boolean result = instance.isJsonValidationBool();
        assertFalse(result);
    }

    @Test
    public void testGetResult_positive() {
        System.out.println("getResult simple");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": 123}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("123");
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testGetResult_positive_regexp() {
        System.out.println("getResult simple");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": 123}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("(123|456)");
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());

        samplerResult.setResponseData("{\"myval\": 456}".getBytes());
        AssertionResult result2 = instance.getResult(samplerResult);
        assertFalse(result2.isFailure());
    }

    @Test
    public void testGetResult_positive_invert() {
        System.out.println("getResult simple");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": 123}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("123");
        instance.setInvert(true);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertTrue(result.isFailure());
        assertEquals(expResult.getName(), result.getName());
    }

    @Test
    public void testGetResult_not_regexp() {
        System.out.println("not regexp");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": \"some complicated value\"}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("some.+");
        AssertionResult result = instance.getResult(samplerResult);
        assertFalse(result.isFailure());

        instance.setIsRegex(false);
        AssertionResult result2 = instance.getResult(samplerResult);
        assertTrue(result2.isFailure());
    }

    @Test
    public void testGetResult_negative() {
        System.out.println("getResult simple");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": 123}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("1234");
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertTrue(result.isFailure());
    }

    @Test
    public void testGetResult_negative_invert() {
        System.out.println("getResult simple");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": 123}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("1234");
        instance.setInvert(true);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertFalse(result.isFailure());
        assertEquals(expResult.getName(), result.getName());
    }

    @Test
    public void testGetResult_null() {
        System.out.println("getResult null");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": null}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setExpectNull(true);
        instance.setJsonValidationBool(true);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testGetResult_null_not_found() {
        System.out.println("getResult null");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": 123}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setExpectNull(true);
        instance.setJsonValidationBool(true);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertTrue(result.isFailure());
    }

    @Test
    public void testGetResult_null_novalidate() {
        System.out.println("getResult null novalidate");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": null}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(false);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testGetResult_no_such_path() {
        System.out.println("getResult notexist");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": null}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.notexist");
        instance.setJsonValidationBool(false);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertTrue(result.isFailure());
    }

    @Test
    public void testGetResult_list_val() {
        System.out.println("getResult list-val");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": [{\"test\":1},{\"test\":2},{\"test\":3}]}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval[*].test");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("2");
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testGetResult_list_negative() {
        System.out.println("getResult list-neg");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": [{\"test\":1},{\"test\":2},{\"test\":3}]}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval[*].test");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("5");
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertTrue(result.isFailure());
    }

    @Test
    public void testGetResult_list_empty_novalidate() {
        System.out.println("getResult list-empty");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": []}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval[*]");
        instance.setJsonValidationBool(false);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testGetResult_list_empty_validate() {
        System.out.println("getResult list-empty");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": []}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("[]");
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testGetResult_dict() {
        System.out.println("getResult notexist");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": {\"key\": \"val\"}}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval");
        instance.setJsonValidationBool(true);
        instance.setExpectedValue("{\"key\":\"val\"}");
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertTrue(result.isFailure());
    }

    @Test
    public void testGetResult_inverted_null() {
        System.out.println("getResult notexist");
        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData("{\"myval\": [{\"key\": null}]}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval[*].key");
        instance.setJsonValidationBool(true);
        instance.setExpectNull(true);
        instance.setInvert(true);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertTrue(result.isFailure());
    }

    @Test
    public void testGetResult_match_msg_problem() {
        System.out.println("getResult notexist");
        SampleResult samplerResult = new SampleResult();
        String str = "{\"execution\":[{\"scenario\":{\"requests\":[{\"headers\":{\"headerkey\":\"header value\"}}]}}]}";
        samplerResult.setResponseData(str.getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.execution[0].scenario.requests[0].headers");
        instance.setJsonValidationBool(true);
        instance.setExpectNull(false);
        instance.setExpectedValue("{headerkey=header value}");
        instance.setInvert(false);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertTrue(result.isFailure());
        assertEquals("Value expected to match regexp '{headerkey=header value}', but it did not match: '{\"headerkey\":\"header value\"}'", result.getFailureMessage());
    }

    @Test
    public void testGetResult_match_msg_problem2() {
        SampleResult samplerResult = new SampleResult();
        String str = "{\n" +
                " \"code\":200,\n" +
                " \"contact\":{\n" +
                "   \"id\":28071,\n" +
                "   \"description\":\"test description\",\n" +
                "   \"info\":{\n" +
                "       \"ngn_number\":[\n" +
                "           \"2003\",\n" +
                "           \"2004\"\n" +
                "       ]\n" +
                "   }\n" +
                " }\n" +
                "}";
        samplerResult.setResponseData(str.getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.contact.info.ngn_number");
        instance.setJsonValidationBool(true);
        instance.setExpectNull(false);
        instance.setExpectedValue("[\"2003\",\"2004\"]");
        instance.setInvert(false);
        instance.setIsRegex(false);
        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testGetResultFloat() {
        System.out.println("testGetResultFloat");
        SampleResult samplerResult = new SampleResult();

        samplerResult.setResponseData("{\"myval\": [{\"test\":0.0000123456789}]}".getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setJsonPath("$.myval[*].test");
        instance.setJsonValidationBool(true);
        instance.setIsRegex(false);
        instance.setExpectedValue("0.0000123456789");

        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testYamlAssertion() throws IOException {
        File test = new File(getClass().getResource("/test1.yml").getPath());
        String response = FileUtils.readFileToString(test);

        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData(response.getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setInputFormat(JSONPathAssertion.INPUT_YAML);
        instance.setJsonPath("$.execution[*].concurrency");
        instance.setJsonValidationBool(true);
        instance.setIsRegex(false);
        instance.setExpectedValue("100");

        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testYamlAssertion2() throws IOException {
        File test = new File(getClass().getResource("/test1.yml").getPath());
        String response = FileUtils.readFileToString(test);

        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData(response.getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setInputFormat(JSONPathAssertion.INPUT_YAML);
        instance.setJsonPath("$.scenarios.quick-test.requests[0]");
        instance.setJsonValidationBool(true);
        instance.setIsRegex(false);
        instance.setExpectedValue("http://blazedemo.com");

        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testYamlAssertion3() throws IOException {
        File test = new File(getClass().getResource("/test1.yml").getPath());
        String response = FileUtils.readFileToString(test);

        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData(response.getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setInputFormat(JSONPathAssertion.INPUT_YAML);
        instance.setJsonPath("$.name");
        instance.setJsonValidationBool(true);
        instance.setIsRegex(false);
        instance.setExpectedValue("camel");

        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }

    @Test
    public void testYamlAssertion4() throws IOException {
        File test = new File(getClass().getResource("/test.yml").getPath());
        String response = FileUtils.readFileToString(test);

        SampleResult samplerResult = new SampleResult();
        samplerResult.setResponseData(response.getBytes());

        JSONPathAssertion instance = new JSONPathAssertion();
        instance.setInputFormat(JSONPathAssertion.INPUT_YAML);
        instance.setJsonPath("$.developers");
        instance.setJsonValidationBool(true);
        instance.setIsRegex(false);
        instance.setExpectedValue("[\"rultor\",\"salikjan\",\"sherif\"]");

        AssertionResult expResult = new AssertionResult("");
        AssertionResult result = instance.getResult(samplerResult);
        assertEquals(expResult.getName(), result.getName());
        assertFalse(result.isFailure());
    }
}
