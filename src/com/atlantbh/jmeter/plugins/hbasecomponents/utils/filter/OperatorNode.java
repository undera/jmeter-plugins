/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.utils.filter;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for filters.
 *
 */
public class OperatorNode extends Node {

	public static final String AND = " AND ";
	public static final String OR = " OR ";

	/** used for validation that on one level all nodes have same operator */
	private Set<String> operators;

	public Set<String> getOperators() {
		return operators;
	}

	public void setOperators(Set<String> operators) {
		this.operators = operators;
	}

	public void addOperator(String operator) throws Exception {
		if (operators == null) {
			operators = new HashSet<String>();
		}
		if (operator != null) {
			if (!AND.trim().equals(operator.trim()) && !OR.trim().equals(operator.trim())) {
				throw new Exception("Invalid operator " + operator);
			}
			operators.add(operator.trim());

			if (operators.size() > 1) {
				throw new Exception("Can't mix operators at same level");
			}
		}
	}

}
