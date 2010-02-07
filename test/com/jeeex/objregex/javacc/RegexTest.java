package com.jeeex.objregex.javacc;

import java.io.StringReader;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class RegexTest {

	/**
	 * Test whether the list of given patterns are [not] valid regular
	 * expressions. This method tests all the inputs.
	 * 
	 * @param testForPass
	 *            if true, then it marks invalid regexes as bad. if false, it
	 *            marks valid regexes as bad.
	 * @param input
	 *            List of regex strings to be tested.
	 * @return List of all the patterns that failed the test.
	 */
	public List<String> test(boolean testForPass, String... input) {
		List<String> testInput = ImmutableList.of(input);

		List<String> bad = Lists.newArrayList();
		for (String test : testInput) {
			boolean failed = false;
			try {
				RegexParser parser = new RegexParser(new StringReader(test));
				parser.Start();
			} catch (ParseException ex) {
				failed = true;
			}
			if (failed == testForPass) {
				bad.add(test);
			}
		}
		return bad;
	}

	public void testDump(String input) {
		SimpleNode node = null;
		try {
			node = new RegexParser(new StringReader(input)).Start();
		} catch (ParseException e) {
			Assert.fail();
		}
		dump(node, " ");
	}

	public void dump(SimpleNode node, String prefix) {
		Token token = node.firstToken;
		List<Token> tokens = Lists.newArrayList();
		while (token != null) {
			tokens.add(token);
			if (token == node.lastToken)
				break;
			token = token.next;
		}
		String tokenStr = "{" + Joiner.on(" ").join(tokens) + "}";

		System.out.println(node.toString(prefix) + tokenStr);

		if (node.children != null) {
			for (int i = 0; i < node.children.length; ++i) {
				SimpleNode n = (SimpleNode) node.children[i];
				if (n != null) {
					dump(n, prefix + " ");
				}
			}
		}
	}

	public void testGood(String... input) {
		List<String> bad = test(true, input);
		Assert.assertEquals("The following patterns are supposed to be valid.",
				ImmutableList.of(), bad);
	}

	public void testBad(String... input) {
		List<String> bad = test(false, input);
		Assert.assertEquals(
				"The following patterns are supposed to be invalid.",
				ImmutableList.of(), bad);
	}

	@Test
	public void grammar_Basic() {
		testGood("A", "A B", "A B C", "A B C D");

		testGood("(A)", "((A))", "(A)(B)", "((A)(B))");

		testGood("$", "^", ".", "^.*$");
	}

	@Test
	public void grammar_Empty() {
		testGood("", "()", "()()", "(())");
	}

	@Test
	public void grammar_Bad() throws Exception {
		testBad("(", ")", "*", "+", "?", "(()", "())");

		testBad("|*", "*|", "|*|");
	}

	@Test
	public void grammar_Or() {
		testGood("A|B", "A|", "|A", "A||A", "|||");
	}

	@Test
	public void grammar_Operator() {
		testGood("A+", "A?", "A*", "AB++", "A+?", "A**", "(A)*", "(A+)*");
	}

	@Test
	public void grammar_Compound() {
		testGood("A+ B? C*", "((A B+ C)*(D? E F)+)?", "(A?|B?|C?|D?)");
	}
}
