/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.utils.filter;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Class used for conversion of custom written filters into HBase filter.
 *
 */
public class HBaseFilterParser {

	private static final Map<String, CompareOp> OPERATOR_MAPINGS = new HashMap<String, CompareOp>();
	static {
		OPERATOR_MAPINGS.put(FilterParser.COMPARE_OPERATORS[0], CompareOp.EQUAL);
		OPERATOR_MAPINGS.put(FilterParser.COMPARE_OPERATORS[1], CompareOp.NOT_EQUAL);
		OPERATOR_MAPINGS.put(FilterParser.COMPARE_OPERATORS[2], CompareOp.LESS);
		OPERATOR_MAPINGS.put(FilterParser.COMPARE_OPERATORS[3], CompareOp.GREATER);
		OPERATOR_MAPINGS.put(FilterParser.COMPARE_OPERATORS[4], CompareOp.LESS_OR_EQUAL);
		OPERATOR_MAPINGS.put(FilterParser.COMPARE_OPERATORS[5], CompareOp.GREATER_OR_EQUAL);
	}

	public static Filter parse(Node node) throws Exception {

		if (node instanceof OperatorNode) {
			OperatorNode operator = (OperatorNode) node;
			FilterList filter = null;
			String op = null;

			if (operator.getOperators().isEmpty()) {
				op = OperatorNode.AND.trim();
			} else {
				op = operator.getOperators().iterator().next();
			}

			if (OperatorNode.AND.trim().equals(op)) {
				filter = new FilterList(Operator.MUST_PASS_ALL);
			} else {
				filter = new FilterList(Operator.MUST_PASS_ONE);
			}
			for (Node child : operator.getChilds()) {
				filter.addFilter(parse(child));
			}
			return filter;
		}
		if (node instanceof ExpressionNode) {
			ExpressionNode expressionNode = (ExpressionNode) node;

			byte[] value = null;
			if (expressionNode.getValue() == null) {
				value = new byte[0];
			} else {
				value = Bytes.toBytes(expressionNode.getValue());
			}
			SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(Bytes.toBytes(expressionNode.getColumnFamily()), Bytes.toBytes(expressionNode.getQualifier()),
					OPERATOR_MAPINGS.get(expressionNode.getOper()), value);
			singleColumnValueFilter.setFilterIfMissing(true);

			return singleColumnValueFilter;
		}
		return null;
	}
}
