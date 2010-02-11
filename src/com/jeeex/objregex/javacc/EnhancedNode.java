package com.jeeex.objregex.javacc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * A wrapper around {@link SimpleNode} to provide more modern API.
 */
public class EnhancedNode extends SimpleNode {

	public EnhancedNode(int i) {
		super(i);
	}

	public EnhancedNode(RegexParser p, int i) {
		super(p, i);
	}

	/**
	 * Returns the unmodifiable list, containing all the children of this node.
	 */
	public List<EnhancedNode> getNodeList() {
		if (children == null) {
			return ImmutableList.of();
		}
		return Collections.unmodifiableList(Arrays
				.asList((EnhancedNode[]) children));
	}
}
