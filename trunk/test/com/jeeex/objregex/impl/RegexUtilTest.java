package com.jeeex.objregex.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.jeeex.objregex.javacc.ASTOperatorExpr;
import com.jeeex.objregex.javacc.EnhancedNode;

public class RegexUtilTest {

	@Test
	public void testExtractOperators() {

		List<RegexOperator> empty = extract("A");
		assertEquals(ImmutableList.of(), empty);

		List<RegexOperator> singleOp = extract("B?");
		assertEquals(ImmutableList.of(RegexOperator.QUESTION), singleOp);

		List<RegexOperator> doubleOp = extract("C*+");
		assertEquals(ImmutableList.of(RegexOperator.STAR, RegexOperator.PLUS),
				doubleOp);
	}

	static List<RegexOperator> extract(String input) {
		EnhancedNode node = RegexUtil.getRootNode(input);

		ASTOperatorExpr opExpr = (ASTOperatorExpr) node.getFirstChild()
				.getFirstChild().getFirstChild();

		return RegexUtil.extractOperators(opExpr);
	}
}
