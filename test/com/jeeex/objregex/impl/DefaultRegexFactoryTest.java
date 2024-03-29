package com.jeeex.objregex.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
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
		compiledPattern.set("D", Predicates.equalTo("D"));
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
		ObjectPattern<String> ptrn = generatePattern("^A B$");

		doesNotMatch(ptrn);
		doesNotMatch(ptrn, "A");
		doesNotMatch(ptrn, "B");
		doesNotMatch(ptrn, "B", "A");
		match(ptrn, "A", "B");
	}

	@Test
	public void testGroup() {
		ObjectPattern<String> ptrn = generatePattern("^(A)$");

		doesNotMatch(ptrn);
		match(ptrn, "A");
		doesNotMatch(ptrn, "A", "A");
	}

	@Test
	public void testGroupStar() {
		ObjectPattern<String> ptrn = generatePattern("^(A|B)*$");

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
		ObjectPattern<String> ptrn = generatePattern("^(A+)$");

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
		ObjectPattern<String> ptrn = generatePattern("^(A?)$");

		match(ptrn);
		match(ptrn, "A");

		doesNotMatch(ptrn, "B");
		doesNotMatch(ptrn, "A", "B");
	}

	@Test
	public void testOperatorStar() {
		ObjectPattern<String> ptrn = generatePattern("^(A*)$");

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
		ObjectPattern<String> ptrn = generatePattern("^(A|B)$");

		doesNotMatch(ptrn);
		doesNotMatch(ptrn, "A", "B");
		doesNotMatch(ptrn, "B", "A");

		match(ptrn, "A");
		match(ptrn, "B");
	}

	@Test
	public void testSimple() {
		ObjectPattern<String> ptrn = generatePattern("^A$");

		doesNotMatch(ptrn);
		match(ptrn, "A");
		doesNotMatch(ptrn, "A", "A");
	}

	@Test
	public void testEmpty() {
		// All different types of empty strings.
		List<String> ptrnStrings = ImmutableList.of("", "^", "$", "^$", "^^$$");

		ImmutableList.Builder<ObjectPattern<String>> builder = ImmutableList
				.builder();
		for (String ptrnString : ptrnStrings) {
			builder.add(generatePattern(ptrnString));
		}

		List<ObjectPattern<String>> emptyPatterns = builder.build();

		// test all the patterns.
		for (ObjectPattern<String> ptrn : emptyPatterns) {
			match(ptrn);
			doesNotMatch(ptrn, "A");
		}
	}

	@Test
	public void testNegation() {
		ObjectPattern<String> ptrn = generatePattern("!A");

		doesNotMatch(ptrn);
		doesNotMatch(ptrn, "A");
		match(ptrn, "B");
		match(ptrn, "C");
		doesNotMatch(ptrn, "B", "B");
		doesNotMatch(ptrn, "A", "A");
	}

	@Test
	public void testNull() {
		ObjectPattern<String> ptrn = generatePattern("null");

		doesNotMatch(ptrn);
		match(ptrn, (String) null);
		doesNotMatch(ptrn, "A");
	}

	@Test
	public void testDot() {
		ObjectPattern<String> ptrn = generatePattern(".");

		match(ptrn, "Foo");
		doesNotMatch(ptrn);
		match(ptrn, (String) null);
		doesNotMatch(ptrn, "Foo", "bar");
	}

	@Test
	public void testSetPattern() {
		ObjectPattern<String> ptrn = generatePattern("Foo");
		ptrn.set("Foo", "A");

		doesNotMatch(ptrn);
		match(ptrn, "A");
		doesNotMatch(ptrn, "A", "A");
	}

	@Test
	public void testRecursivePattern() {
		ObjectPattern<String> ptrn = generatePattern("LPAREN (this)? RPAREN");

		ptrn.set("LPAREN", Predicates.equalTo("("));
		ptrn.set("RPAREN", Predicates.equalTo(")"));

		doesNotMatch(ptrn);

		match(ptrn, "(", ")");
		match(ptrn, "(", "(", ")", ")");
		match(ptrn, "(", "(", "(", ")", ")", ")");
		match(ptrn, "(", "(", "(", "(", ")", ")", ")", ")");
		doesNotMatch(ptrn, "(", "(", ")");
		doesNotMatch(ptrn, "(", ")", ")");
	}

	@Test
	public void testConcatOr() {
		ObjectPattern<String> ptrn = generatePattern("(A B)|(C D)");

		match(ptrn, "A", "B");
		match(ptrn, "C", "D");
	}
}
