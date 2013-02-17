/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.jsonutils.jsonpathassertion;

import java.io.Serializable;
import java.text.ParseException;
import org.apache.jmeter.assertions.*;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import com.jayway.jsonpath.JsonPath;

/**
 * This is main class for JSONPath Assertion which verifies assertion on previous sample result
 * using JSON path expression
 * 
 * @author Semir Sabic / AtlantBH
 */
public class JSONPathAssertion extends AbstractTestElement implements Serializable, Assertion {

	private static final long serialVersionUID = 1L;
	
	private static final String JSONPATH = "JSON_PATH";
	private static final String EXPECTEDVALUE = "EXPECTED_VALUE";
	private static final String JSONVALIDATION = "JSONVALIDATION";

	public String getJsonPath() {
		return getPropertyAsString(JSONPATH);
	}
	
	public void setJsonPath(String jsonPath) {
		setProperty(JSONPATH, jsonPath);
	}
	
	public String getExpectedValue() {
		return getPropertyAsString(EXPECTEDVALUE);
	}
	
	public void setExpectedValue(String expectedValue) {
		setProperty(EXPECTEDVALUE, expectedValue);
	}
	
	public void setJsonValidationBool(boolean jsonValidation) {
		setProperty(JSONVALIDATION, jsonValidation);
	}
	
	public boolean isJsonValidationBool() {
		return getPropertyAsBoolean(JSONVALIDATION);
	}
	
	public boolean checkJSONPathWithoutValidation(String jsonString, String jsonPath) throws Exception {
		String jsonPathResult = "";
		
		jsonPathResult = JsonPath.read(jsonString, jsonPath).toString();
		
		if("".equalsIgnoreCase(jsonPath)){
			throw new Exception("JSON path is is empty!");
		} else if("".equalsIgnoreCase(jsonPathResult)){
			throw new Exception("Incorrect JSON path");
		} else {
			return true;
		}
	}
	
	public boolean checkJSONPathWithValidation(String jsonString, String jsonPath, String expectedValue) throws Exception {
		if("".equalsIgnoreCase(jsonPath) || "".equalsIgnoreCase(expectedValue)){
			throw new Exception("JSON path or expected value is empty!");
		}
		
		if (expectedValue.equalsIgnoreCase(JsonPath.read(jsonString, jsonPath).toString())) {
			return true;
		} else {
			throw new Exception("Response doesn't contain expected value.");
		}
	}
	
	@Override
	public AssertionResult getResult(SampleResult samplerResult) 
	{
		AssertionResult result = new AssertionResult(getName());
		byte[] responseData = samplerResult.getResponseData();
		if (responseData.length == 0) {
			return result.setResultForNull();
		}
	
		if (isJsonValidationBool())
		{
			try 
			{
				if (checkJSONPathWithValidation(new String(responseData), getJsonPath(), getExpectedValue())) {
					result.setFailure(false);
					result.setFailureMessage("");
				}
			} 
			catch (ParseException e)
			{
				result.setFailure(true);
				result.setFailureMessage(e.getClass().toString() + " - " + e.getMessage());
			}
			catch (Exception e) 
			{
				result.setFailure(true);
				result.setFailureMessage(e.getMessage());
			}
		}
		
		if (!isJsonValidationBool())
		{
			try 
			{
				if (checkJSONPathWithoutValidation(new String(responseData), getJsonPath())) {
					result.setFailure(false);
					result.setFailureMessage("");
				}		
			} 
			catch (ParseException e) 
			{
				result.setFailure(true);
				result.setFailureMessage(e.getClass().toString() + " - " + e.getMessage());
			} 
			catch (Exception e) 
			{
				result.setFailure(true);
				result.setFailureMessage(e.getMessage());
			}
		}	
		
		return result;
	}	
}