package com.jeeex.objregex.impl;

import static com.jeeex.objregex.impl.TransitionIdentifier.makeTid;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class StateUtilTest {

	private State stateA;
	private State stateB;
	private State stateC;
	private State stateD;

	private Set<State> fromSet;
	private Set<State> toSet;

	@Before
	public void setup() {
		stateA = new LeafState();
		stateB = new LeafState();
		stateC = new LeafState();
		stateD = new LeafState();

		stateA.addTransition(makeTid("FOO"), stateB);
		stateA.addTransition(makeTid("BAR"), stateC);

		stateB.addTransition(makeTid("TEST"), stateC);
		stateB.addTransition(makeTid("TEST"), stateD);

		stateC.addTransition(makeTid("FOO"), stateD);

		stateA.addTransition(TransitionIdentifier.EPSILON, stateB);
		stateB.addTransition(TransitionIdentifier.EPSILON, stateC);
	}

	@Test
	public void testEmptyState() {
		State state = StateUtil.emptyState();

		State head = state.getHead();
		State tail = state.getTail();

		assertNotSame(head, tail);

		assertEquals(ImmutableSet.of(TransitionIdentifier.EPSILON), tail
				.getTransitions().keySet());
		assertEquals(ImmutableSet.of(head), tail.getTransitions().get(
				TransitionIdentifier.EPSILON));
	}

	@Test
	public void testTraverse_Empty() {
		fromSet = ImmutableSet.of(stateB);
		toSet = StateUtil.traverse(fromSet, makeTid("DOES_NOT_EXIST"));
		assertEquals(ImmutableSet.of(), toSet);
	}

	@Test
	public void testTraverse_Multi() {
		fromSet = ImmutableSet.of(stateB);
		toSet = StateUtil.traverse(fromSet, makeTid("TEST"));
		assertEquals(ImmutableSet.of(stateC, stateD), toSet);
	}

	@Test
	public void testTraverse_MultipleStartingStates() {
		fromSet = ImmutableSet.of(stateA, stateC);
		toSet = StateUtil.traverse(fromSet, makeTid("FOO"));
		assertEquals(ImmutableSet.of(stateB, stateD), toSet);
	}

	@Test
	public void testTraverse_Simple() {
		fromSet = ImmutableSet.of(stateA);
		toSet = StateUtil.traverse(fromSet, makeTid("FOO"));
		assertEquals(ImmutableSet.of(stateB), toSet);
	}

	@Test
	public void testTransitiveClosure() {
		fromSet = ImmutableSet.of(stateA);
		toSet = StateUtil.transitiveClosure(fromSet);
		assertEquals(ImmutableSet.of(stateA, stateB, stateC), toSet);
	}
}
