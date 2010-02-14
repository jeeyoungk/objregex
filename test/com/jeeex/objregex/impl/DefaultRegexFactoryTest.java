package com.jeeex.objregex.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.jeeex.objregex.ObjectPattern;
import com.jeeex.objregex.RegexFactory;

public class DefaultRegexFactoryTest {

	private RegexFactory factory;

	private void doesNotMatch(ObjectPattern<String> ptrn, String... input) {
		assertFalse(ptrn.match(Arrays.asList(input)));
	}

	private ObjectPattern<String> generatePattern(String pattern) {
		ObjectPattern<String> compiledPattern = factory.compile(pattern);

		compiledPattern.set("A", Predicates.equalTo("A"));
		compiledPattern.set("B", Predicates.equalTo("B"));
		compiledPattern.set("C", Predicates.equalTo("C"));
		compiledPattern.set("ABC", Predicates
				.in(ImmutableSet.of("A", "B", "C")));

		return compiledPattern;
	}

	private void match(ObjectPattern<String> ptrn, String... input) {
		assertTrue(ptrn.match(Arrays.asList(input)));
	}

	@Before
	public void setup() {
		factory = new DefaultRegexFactory();
	}

	@Test
	public void testConcat() {
		ObjectPattern<String> ptrn = generatePattern("A B");

		doesNotMatch(ptrn);
		doesNotMatch(ptrn, "A");
		doesNotMatch(ptrn, "B");
		doesNotMatch(ptrn, "B", "A");
		match(ptrn, "A", "B");
	}

	@Test
	public void testGroup() {
		ObjectPattern<String> ptrn = generatePattern("(A)");

		doesNotMatch(ptrn);
		match(ptrn, "A");
		doesNotMatch(ptrn, "A", "A");
	}

	@Test
	public void testGroupStar() {
		ObjectPattern<String> ptrn = generatePattern("(A|B)*");

		match(ptrn);
		match(ptrn, "A");
		match(ptrn, "B");
		match(ptrn, "A", "A");
		match(ptrn, "A", "B");
		match(ptrn, "B", "A");
		match(ptrn, "B", "B");

		doesNotMatch(ptrn, "C");
	}

	@Test
	public void testOperatorPlus() {
		ObjectPattern<String> ptrn = generatePattern("A+");

		doesNotMatch(ptrn);
		match(ptrn, "A");
		match(ptrn, "A", "A");
		match(ptrn, "A", "A", "A");

		doesNotMatch(ptrn, "B");
		doesNotMatch(ptrn, "A", "B", "A");
		doesNotMatch(ptrn, "A", "A", "A", "B");
	}

	@Test
	public void testOperatorQuestion() {
		ObjectPattern<String> ptrn = generatePattern("A?");

		match(ptrn);
		match(ptrn, "A");

		doesNotMatch(ptrn, "B");
		doesNotMatch(ptrn, "A", "B");
	}

	@Test
	public void testOperatorStar() {
		ObjectPattern<String> ptrn = generatePattern("A*");

		match(ptrn);
		match(ptrn, "A");
		match(ptrn, "A", "A");
		match(ptrn, "A", "A", "A");

		doesNotMatch(ptrn, "B");
		doesNotMatch(ptrn, "A", "B", "A");
		doesNotMatch(ptrn, "A", "A", "A", "B");
	}

	@Test
	public void testOr() {
		ObjectPattern<String> ptrn = generatePattern("A|B");

		doesNotMatch(ptrn);
		doesNotMatch(ptrn, "A", "B");
		doesNotMatch(ptrn, "B", "A");

		match(ptrn, "A");
		match(ptrn, "B");
	}

	@Test
	public void testSimple() {
		ObjectPattern<String> ptrn = generatePattern("A");

		doesNotMatch(ptrn);
		match(ptrn, "A");
		doesNotMatch(ptrn, "A", "A");
	}
}
