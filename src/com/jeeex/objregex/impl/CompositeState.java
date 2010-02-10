package com.jeeex.objregex.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Multimap;

/**
 * Represents a group of NFA states bound together. A {@link CompositeState}
 * contains ³ 1 states. There are at least 2 groups of {@link State}s.
 * {@link #head} and {@link #tail}. The CompositeState is trivial if head =
 * tail. All the methods defined in the interface {@link State} are delegated to
 * either {@link #head} or {@link #tail}.
 * 
 * @author jeekim
 * 
 */
public class CompositeState implements State {
	private final State tail;
	private final State head;

	/**
	 * Create a double state State.
	 * 
	 * @throws NullPointerException
	 *             If any of the arguments are null.
	 */
	public CompositeState(State head, State tail) throws NullPointerException {
		checkNotNull(head);
		checkNotNull(tail);
		this.head = head;
		this.tail = tail;
	}

	public void addTransition(TransitionIdentifier id, State other) {
		// delegate to the head.
		head.addTransition(id, other);
	}

	public State getHead() {
		// delegate to the head.
		return head.getHead();
	}

	public State getTail() {
		// delegate to the tail.
		return tail.getTail();
	}

	public Multimap<TransitionIdentifier, State> getTransitions() {
		// delegate to the head.
		return head.getTransitions();
	}
}
