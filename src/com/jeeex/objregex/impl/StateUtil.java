package com.jeeex.objregex.impl;

import static com.jeeex.objregex.impl.TransitionIdentifier.EPSILON;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet.Builder;

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
		State newState = new CompositeState(second, first);
		first.addTransition(EPSILON, second);

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
		newState.addTransition(EPSILON, state);
		state.addTransition(EPSILON, newState);
		return newState;
	}

	/**
	 * Returns the NFA for the regular expression "A | B".
	 * <p>
	 * Namely, it is set of states A, B, HEAD, TAIL, with epsilon-transitions
	 * from TAIL to A and B, from A and B to HEAD.
	 * 
	 * @param first
	 *            NFA state for regex A.
	 * @param second
	 *            NFA state for regex B.
	 */
	public static State or(State first, State second) {
		State tail = first.getTail();
		State head = first.getHead();

		tail.addTransition(EPSILON, second);
		second.addTransition(EPSILON, head);

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
		return transitiveClosure(states, ImmutableSet.of(EPSILON));
	}

	/**
	 * 
	 * @param states
	 * @param freeTransitions
	 * @return
	 */
	public static Set<State> transitiveClosure(Set<State> states,
			Set<TransitionIdentifier> freeTransitions) {
		Set<State> closure = Sets.newHashSet(states);
		int oldSize;
		int newSize;

		// try to expand the closure each iteration. If the size stays the same
		// for the iteration, then we've hit the limit - terminate.
		do {
			oldSize = closure.size();
			for (TransitionIdentifier id : freeTransitions) {
				closure.addAll(traverse(closure, id));
			}
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
		return StateUtil.single(EPSILON);
	}

	public static Set<TransitionIdentifier> getOutgoingTransitions(
			Set<State> states) {
		Builder<TransitionIdentifier> builder = ImmutableSet.builder();
		for (State state : states) {
			builder.addAll(state.getTransitions().keySet());
		}
		return builder.build();
	}
}
