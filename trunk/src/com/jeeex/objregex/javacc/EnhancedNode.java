package com.jeeex.objregex.javacc;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

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
	 * Returns the unmodifiable list, containing all the children nodes of this
	 * node.
	 */
	public List<EnhancedNode> getNodeList() {
		if (children == null) {
			return ImmutableList.of();
		}
		EnhancedNode[] echildren = new EnhancedNode[children.length];
		for (int i = 0; i < children.length; i++) {
			echildren[i] = (EnhancedNode) children[i];
		}
		return Arrays.asList(echildren);
	}

	/**
	 * Returns the unmodifiable list, containing all the children nodes of this
	 * node, starting from {@code index}.
	 */
	public List<EnhancedNode> getNodeList(int index) {
		return getNodeList().subList(index, children.length);
	}

	/**
	 * Convenient way to get the first child of the node.
	 * 
	 * @return
	 */
	public EnhancedNode getFirstChild() {
		return (EnhancedNode) children[0];
	}

	/**
	 * Returns the list of tokens in this Node.
	 * 
	 * @return
	 */
	public List<Token> getTokenList() {
		Builder<Token> builder = ImmutableList.builder();
		Token t = jjtGetFirstToken();
		do {
			builder.add(t);
			t = t.next;
		} while (t != jjtGetLastToken());

		return builder.build();
	}
}
