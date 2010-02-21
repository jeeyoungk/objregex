package com.jeeex.objregex.impl;

import static com.jeeex.objregex.impl.TransitionIdentifier.makeTid;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;

public class LazyStateTest {
	public class SimpleLazyState extends LazyState {

		@Override
		protected void lazyInit() {

		}
	}

	@Test
	public void testAddTransition() {
		SimpleLazyState state = new SimpleLazyState();
		State comp = StateUtil.single(makeTid("foo"));
		state.addTransition(makeTid("bar"), comp);

		assertEquals(HashMultimap.create(ImmutableMultimap.of(makeTid("bar"),
				comp.getTail())), state.getTransitions());
	}
}
