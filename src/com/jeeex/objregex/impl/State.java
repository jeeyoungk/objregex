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

	/**
	 * Add a transition from the head of this state to the tail of the other
	 * state. (This implies {@code a.addTransition(id, b)} is equivalent to
	 * {@code a.getHead().addTransition(id, b.getTail())}.
	 * 
	 * @param id
	 *            The identifier for the newly created transition.
	 * @param The
	 *            target of the state transition.
	 */
	public void addTransition(TransitionIdentifier id, State other);
}
