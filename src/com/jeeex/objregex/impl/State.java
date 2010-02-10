package com.jeeex.objregex.impl;

import com.google.common.collect.Multimap;

/**
 * A class representing a group of NFA states.
 * <p>
 * Each {@link State} is either a single state, or a group of states.
 * {@link #getHead() Head} and {@link Tail} represent the entry and exit point
 * in this group of state.
 * 
 * @author jeekim
 * 
 */
public interface State {
	public State getHead();

	public State getTail();

	public Multimap<TransitionIdentifier, State> getTransitions();

	public void addTransition(TransitionIdentifier id, State other);
}
