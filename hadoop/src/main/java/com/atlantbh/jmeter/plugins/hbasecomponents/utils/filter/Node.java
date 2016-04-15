/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.utils.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for filters.
 *
 */
public abstract class Node {

	private List<Node> childs;

	public void setChilds(List<Node> childs) {
		this.childs = childs;
	}

	public List<Node> getChilds() {
		return childs;
	}

	public void addChild(Node child) {
		if (childs == null) {
			childs = new ArrayList<Node>();
		}
		childs.add(child);
	}
}
