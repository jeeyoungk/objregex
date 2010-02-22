package com.jeeex.objregex.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;

public class ObjectPatternImplTest {

	@Test
	public void testSetDuplicate() {
		ObjectPatternImpl<Object> ptrn = new ObjectPatternImpl<Object>("");
		assertEquals(ImmutableSet.of(), ptrn.assignedIds);
		assertEquals(ImmutableSet.of(), ptrn.idToPredicate.keySet());
		assertEquals(ImmutableSet.of(), ptrn.idToPattern.keySet());

		ptrn.set("FOO", Predicates.alwaysTrue());
		assertEquals(ImmutableSet.of(), ptrn.idToPattern.keySet());
		assertEquals(ImmutableSet.of("FOO"), ptrn.idToPredicate.keySet());
		assertEquals(ImmutableSet.of("FOO"), ptrn.assignedIds);

		ptrn.set("FOO", Predicates.alwaysFalse());
		assertEquals(ImmutableSet.of(), ptrn.idToPattern.keySet());
		assertEquals(ImmutableSet.of("FOO"), ptrn.idToPredicate.keySet());
		assertEquals(ImmutableSet.of("FOO"), ptrn.assignedIds);

		ptrn.set("BAR", "some random pattern");
		assertEquals(ImmutableSet.of("BAR"), ptrn.idToPattern.keySet());
		assertEquals(ImmutableSet.of("FOO"), ptrn.idToPredicate.keySet());
		assertEquals(ImmutableSet.of("FOO", "BAR"), ptrn.assignedIds);

		ptrn.set("FOO", "some other pattern");
		assertEquals(ImmutableSet.of("FOO", "BAR"), ptrn.idToPattern.keySet());
		assertEquals(ImmutableSet.of(), ptrn.idToPredicate.keySet());
		assertEquals(ImmutableSet.of("FOO", "BAR"), ptrn.assignedIds);
	}
}
