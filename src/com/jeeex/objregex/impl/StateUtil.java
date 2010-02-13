package com.jeeex.objregex.impl;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Utility methods needed to translate regular expression into NFA. Each atomic
 * regular expression can be translated NFA with two states via
 * {@link #single(TransitionIdentifier)} method. The three major operations on
 * regular expressions - concatenation (A B), Alternation (A | B), and Kleine
 * Closure (A*) generates another State from the input. By recursively applying
 * those operations, any regular expression can be transformed into an NFA.
 * <p>
 * I've got the idea of translating regex into NFA from the book <a
 * href="http://www.cs.princeton.edu/~appel/modern/java/">Modern Compiler
 * Implementation in Java</a>, chapter 3.
 * 
 * @author jeekim
 * 
 */
public class StateUtil {

	private StateUtil() {
		// prevent initialization.
	}

	/**
	 * Returns the NFA for the regular expression "A B"
	 * 
	 * @param first
	 *            NFA state for regex A.
	 * @param second
	 *            NFA state for regex B.
	 */
	public static State concat(State first, State second) {
		State newState = new CompositeState(first, second);
		first.addTransition(TransitionIdentifier.EPSILON, second);

		return newState;
	}

	/**
	 * Returns the NFA for the regular expression "A*"
	 * 
	 * @param state
	 *            NFA state for regex A.
	 */
	public static State kleineClosure(State state) {
		State newState = new LeafState();
		newState.addTransition(TransitionIdentifier.EPSILON, state);
		state.addTransition(TransitionIdentifier.EPSILON, newState);
		return newState;
	}

	/**
	 * Returns the NFA for the regular expression "A | B"
	 * 
	 * @param first
	 *            NFA state for regex A.
	 * @param second
	 *            NFA state for regex B.
	 */
	public static State or(State first, State second) {
		State tail = new LeafState();
		State head = new LeafState();

		tail.addTransition(TransitionIdentifier.EPSILON, second);
		tail.addTransition(TransitionIdentifier.EPSILON, second);
		second.addTransition(TransitionIdentifier.EPSILON, head);
		second.addTransition(TransitionIdentifier.EPSILON, head);

		return new CompositeState(head, tail);
	}

	/**
	 * Returns the NFA for regular expression "a", where {@code a} is a single
	 * character.
	 * <p>
	 * Internally, it creates a {@link State} containing exactly two
	 * {@link State}s, {@code head} and {@code tail}, and one transition from
	 * the head to tail.
	 * 
	 * @param id
	 *            The Transition identifier between the two states.
	 */
	public static State single(TransitionIdentifier id) {
		State tail = new LeafState();
		State head = new LeafState();
		tail.addTransition(id, head);
		return new CompositeState(head, tail);
	}

	/**
	 * Calculate the transitive closure of the given set of states.
	 * <p>
	 * A transitive closure of a set of states is the maximal set of states
	 * reachable from the set just by traversion
	 * {@link TransitionIdentifier#EPSILON epsilon} edges. By definition,
	 * transitive closure of a set is superset of the original.
	 * 
	 * @param states
	 * @return
	 */
	public static Set<State> transitiveClosure(Set<State> states) {
		Set<State> closure = Sets.newHashSet(states);
		int oldSize;
		int newSize;

		// try to expand the closure each iteration. If the size stays the same
		// for the iteration, then we've hit the limit - terminate.
		do {
			oldSize = closure.size();
			closure.addAll(traverse(closure, TransitionIdentifier.EPSILON));
			newSize = closure.size();
		} while (oldSize != newSize);

		return closure;
	}

	/**
	 * Make a transition from the given set of states to another set of states,
	 * specified by the provided {@link TransitionIdentifier}. The original set
	 * is unmodified by this method.
	 * 
	 * @param original
	 *            Starting set of states.
	 * @param identifier
	 *            The identifier for the transition.
	 * @return Set of all states reachable from {@code original} by a single
	 *         transition of {@code identifier}.
	 */
	public static Set<State> traverse(Set<State> original,
			TransitionIdentifier identifier) {
		Set<State> tempSet = Sets.newHashSet();
		for (State state : original) {
			tempSet.addAll(state.getTransitions().get(identifier));
		}
		return tempSet;
	}

	/**
	 * Generate a {@link State} for empty string.
	 */
	public static State emptyState() {
		return StateUtil.single(TransitionIdentifier.EPSILON);
	}
}
