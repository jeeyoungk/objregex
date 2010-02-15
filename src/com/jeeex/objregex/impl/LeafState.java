package com.jeeex.objregex.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Represents a single NFA state. Both {@link #getHead() head} and
 * {@link #getTail() tail} returns the {@code this} State. This class maintains
 * its own map of transitions.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-11
 * 
 */
public class LeafState implements State {
	final Multimap<TransitionIdentifier, State> transitions = HashMultimap
			.create();

	/**
	 * Default constructor.
	 */
	public LeafState() {
	}

	public void addTransition(TransitionIdentifier id, State other) {
		transitions.put(id, other.getTail());
	}

	/**
	 * @return this
	 */
	public State getHead() {
		return this;
	}

	/**
	 * @return this
	 */
	public State getTail() {
		return this;
	}

	public Multimap<TransitionIdentifier, State> getTransitions() {
		return transitions;
	}
}
