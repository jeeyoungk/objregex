package com.jeeex.objregex.javacc;

import java.io.StringReader;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

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
	public void testGrammar_Comprehensive() {
		testGood("FOO", "BAR", "FOO BAR", "FOO BAR BAZ", "FOO+ BAR? BAZ*",
				"_FOO_BAR_BAZ*", "A|B|C|D", "(A|B|C)*", "((A B C )+ (A|B|C)*)?", "A B C|");
	}

	@Test
	public void testGrammar_Empty() {
		testGood("", "()", "()()", "(())");
	}

	@Test
	public void testGrammar_Bad() throws Exception {
		testBad("(", ")", "*", "+", "?", "(()", "())");
	}

	@Test
	public void testGrammar_Or() {
		testGood("A|B", "A|", "|A", "A||A", "|||");
	}
}
