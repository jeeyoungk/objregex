package com.jeeex.objregex.impl;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.jeeex.objregex.ObjectPattern;
import com.jeeex.objregex.RegexFactory;

/**
 * Sample set of tests
 * 
 * @author Jeeyoung Kim
 * @since 2010-05-13
 * 
 */
public class SampleTest {

	Predicate<Integer> NEGATIVE = new Predicate<Integer>() {
		public boolean apply(Integer input) {
			return input < 0;
		}
	};

	Predicate<Integer> POSITIVE = new Predicate<Integer>() {
		public boolean apply(Integer input) {
			return input > 0;
		}
	};

	List<String> input;

	List<Integer> ints;

	ObjectPattern<String> ptrn;

	ObjectPattern<Integer> intPtrn;

	RegexFactory factory = new DefaultRegexFactory();

	/** Assert that the input list only contains words that starts with "A" */
	@Test
	public void simpleTest() {
		input = newArrayList("Apple", "America", "Alice");
		ptrn = new DefaultRegexFactory().compile("startWithA+");

		ptrn.set("startWithA", new Predicate<String>() {
			public boolean apply(String input) {
				return input.startsWith("A");
			}
		});

		assertTrue(ptrn.match(input));
	}

	/** Check that the given list contains only non-null elements. */
	@Test
	public void nullListTest() {
		input = newArrayList("Apple", "America", "Alice", null);

		ptrn = factory.compile("!null+");

		assertTrue(ptrn.match(input.subList(0, 3)));

		assertFalse(ptrn.match(input));
	}

	/**
	 * Tests for balenced parenthesis, which is impossible to do so using the
	 * "real" regular expressions. (I think Perl can do this too)
	 */
	@Test
	public void balencedParenthesisTest() {
		input = newArrayList("(", "(", ")", "(", ")", ")");

		ptrn = factory.compile("L this* R");

		ptrn.set("L", Predicates.equalTo("("));
		ptrn.set("R", Predicates.equalTo(")"));

		assertTrue(ptrn.match(input));

		assertFalse(ptrn.match(input.subList(1, 5)));
	}

	/** Alternating sequence of positive / negative numbers. */
	@Test
	public void alternatingSeqTest() {
		intPtrn = factory.compile("POS? (NEG POS)* NEG?");

		intPtrn.set("NEG", NEGATIVE);
		intPtrn.set("POS", POSITIVE);

		assertTrue(intPtrn.match(newArrayList(1, -3)));
		assertTrue(intPtrn.match(newArrayList(-2, 2)));
		assertTrue(intPtrn.match(newArrayList(-3, 5, -4, 7, -6, 9, -10, 200)));

		assertFalse(intPtrn.match(newArrayList(1, 1)));
		assertFalse(intPtrn.match(newArrayList(1, -3, 2, -4, 5, -2, -6)));
	}

	/** Determine whether the list contains non-null element or not. */
	@Test
	public void hasNotNullTest() {
		input = newArrayList(null, null, null, "notnull", null, null);

		ptrn = factory.compile(".* !null .*");

		assertTrue(ptrn.match(input));
	}
}
