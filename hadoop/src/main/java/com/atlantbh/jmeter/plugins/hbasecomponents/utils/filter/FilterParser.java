/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.utils.filter;

import com.atlantbh.jmeter.plugins.hbasecomponents.samplers.HBaseScanSampler;

/**
 * Filter parser used in {@link HBaseScanSampler}
 *
 */
public class FilterParser {

	public static final String[] COMPARE_OPERATORS = { "=", "!=", "<", ">", "<=", ">=" };

	private static final String OPERATOR_SPLIT_REG_EXP = "(!=|<=|>=|=|<|>)";
	private static final String ENCLOSED_TOKEN = "[\\(][A-Za-z0-9$#~_-]+:[A-Za-z0-9$#~_-]+[!=|<=|>=|=|>|<]+[\\S\\s]+[\\)]"; //"[\\(][A-Za-z0-9$#~_-]+:[A-Za-z0-9$#~_-]+[!=|<=|>=|=|>|<]+[A-Za-z0-9$#~_-]+[\\)]";
	private static final String TOKEN = "[A-Za-z0-9$#~_-]+:[A-Za-z0-9$#~_-]+[!=|<=|>=|=|>|<]+[\\S\\s]+";//"[A-Za-z0-9$#~_-]+:[A-Za-z0-9$#~_-]+[!=|<=|>=|=|>|<]+[A-Za-z0-9$#~_-\\{\\}:,]+";

	/** parse complex expression */
	public static Node parse(String expression) throws Exception {

		String token = expression.trim();

		// support for simple enclosed expressions
		if (token.matches(ENCLOSED_TOKEN)) {
			token = token.replace("(", "").replace(")", "");
			ExpressionNode simpleNode = new ExpressionNode();
			parseToken(simpleNode, token);
			return simpleNode;
		}

		OperatorNode node = new OperatorNode();
		int i = 0;
		while (i < token.length()) {
			String char_ = token.substring(i, i + 1);
			String operator = null;
			if (char_.equals("(")) {
				// complex -- loop until closed )
				int[] resultInfo = findComplexEndIndex(token.substring(i));

				operator = resultInfo[1] == 0 ? OperatorNode.OR : resultInfo[1] == 1 ? OperatorNode.AND : null;

				String complex = token.substring(i + 1, i + resultInfo[0] - 1);
				Node complexNode = parse(complex);
				node.addChild(complexNode);
				node.addOperator(operator);
				if (operator == null) {
					break;
				}
				i = i + resultInfo[0] + operator.length();
			} else {
				ExpressionNode child = new ExpressionNode();
				// simple -- loop until operator

				int[] operatorInfo = findEndIndex(token.substring(i));
				int operatorIndex = operatorInfo[0];
				operator = operatorInfo[1] == 0 ? OperatorNode.OR : operatorInfo[1] == 1 ? OperatorNode.AND : null;

				if (operatorIndex == -1) {
					parseToken(child, token.substring(i));
					i = token.length();
				} else {
					parseToken(child, token.substring(i, i + operatorIndex));
					i = i + operatorIndex + operator.length();
				}
				node.addOperator(operator);
				node.addChild(child);
			}
		}

		return node;
	}

	/** find end index of complex expression */
	private static int[] findComplexEndIndex(String token) {
		int[] returnInfo = new int[2];

		int i = 0;
		int bracks = 0;

		// find end of complex expression
		while (true) {
			String char_ = token.substring(i, i + 1);
			if ("(".equals(char_)) {
				bracks++;
			}
			if (")".equals(char_)) {
				bracks--;
			}
			i++;

			if (bracks == 0) {
				break;
			}
		}
		returnInfo[0] = i;
		returnInfo[1] = -1;
		// find operator value at the end of complex expression
		if (token.substring(i).startsWith(OperatorNode.AND)) {
			returnInfo[1] = 1;
		}
		if (token.substring(i).startsWith(OperatorNode.OR)) {
			returnInfo[1] = 0;
		}

		return returnInfo;
	}

	/**
	 * return index of next operator in simple token case
	 * 
	 * first element in array is index of operator second element is operator
	 * (-1 == null 0 = or 1 = and)
	 * */
	private static int[] findEndIndex(String token) {
		int andOperatorIndex = token.indexOf(OperatorNode.AND);
		int orOperatorIndex = token.indexOf(OperatorNode.OR);
		int[] returnInfo = new int[2];

		if (andOperatorIndex == -1 && orOperatorIndex == -1) {
			// operator is null
			returnInfo[0] = -1;
			returnInfo[1] = -1;
			return returnInfo;
		}
		if (andOperatorIndex == -1) {
			// operator is OR
			returnInfo[0] = orOperatorIndex;
			returnInfo[1] = 0;
			return returnInfo;
		}
		if (orOperatorIndex == -1) {
			// operator is AND
			returnInfo[0] = andOperatorIndex;
			returnInfo[1] = 1;
			return returnInfo;
		}
		if (andOperatorIndex > orOperatorIndex) {
			// operator is OR
			returnInfo[0] = orOperatorIndex;
			returnInfo[1] = 0;
			return returnInfo;
		} else {
			// operator is AND
			returnInfo[0] = andOperatorIndex;
			returnInfo[1] = 1;
			return returnInfo;
		}
	}

	/** parse one simple token */
	private static void parseToken(ExpressionNode node, String token) throws Exception {
		if (!token.matches(TOKEN)) {
			throw new Exception("Invalid token " + token);
		}
		String[] colval = token.split(OPERATOR_SPLIT_REG_EXP);
		String[] columns = colval[0].split("\\:");
		node.setColumnFamily(columns[0].trim());
		node.setQualifier(columns[1].trim());
		node.setValue("NULL".equals(colval[1].trim()) ? null : colval[1].trim());
		node.setOper(getOperator(token).trim());
	}

	/** extract operator from filter string and convert it to HBase operator */
	private static String getOperator(String filterToken) {
		for (int i = COMPARE_OPERATORS.length - 1; i >= 0; i--) {
			if (filterToken.contains(COMPARE_OPERATORS[i])) {
				int index = filterToken.indexOf(COMPARE_OPERATORS[i]);
				String operatorString = filterToken.substring(index, index + COMPARE_OPERATORS[i].length());
				return operatorString;
			}
		}
		return null;
	}

}
