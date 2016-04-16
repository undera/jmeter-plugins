/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * This class provides a convenient way to store and manipulate named parameters.
 * It is used to manage the authorization header constructed by OAuthGenerator class.
 */
public class Parameter implements Comparable<Parameter>, Map.Entry<String, String> {
	private final String key;
	private String value;
	private String compareKey;
	
	private void calculateCompareKey()
	{
		compareKey = percentEncode(key) + ' ' + percentEncode(value);
	}

	/**
	 * @param key Property name
	 * @param value Property value
	 * 
	 * */
	public Parameter(String key, String value)
	{
		this.key = key;
		this.value = value;
		calculateCompareKey();
	}
	
	
	/**
	 * Property name getter.
	 * 
	 * @return Name of the current property instance.
	 * 
	 * */
	@Override
	public String getKey() {
		return key;
	}

	/**
	 * Property value getter.
	 * 
	 * @return Name of the current property instance.
	 * 
	 * */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * Property value setter.
	 * 
	 * @param value New value for the property
	 * 
	 * */
	@Override
	public String setValue(String value) {
		String result = this.value;
		this.value = value;
		calculateCompareKey();
		return result;
	}

	@Override
	public String toString() {
		return getKey() + '=' + getValue();
	}

	@Override
	public int compareTo(Parameter other) {
		return compareKey.compareTo(other.compareKey);
	}
	
	/**
	 * Encodes a string to a URL friendly alternative.
	 * 
	 * @param value The string to encode
	 * @return URL friendly version of the string
	 * 
	 * */
	public static String percentEncode(String value) {
		if (value == null) return "";
		try {
			return URLEncoder.encode(value, "UTF-8").replace("%7E", "~");
		} catch (UnsupportedEncodingException e) {
			//This will never happen
			return null;
		}
	}
}
