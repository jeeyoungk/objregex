package com.jeeex.objregex.impl;

import com.google.common.collect.Multimap;

/**
 * A class representing a group of NFA states.
 * <p>
 * Each {@link State} is either a single state, or a group of states.
 * {@link State} objects are special in a sense that, they have exactly one each
 * {@link #getHead() Head} and {@link #getTail() tail} states, which are the
 * states with transitions to / from other states that are not part of this
 * {@link State}. It is possible that the {@code head} and {@code tail} refer to
 * the same state.
 * 
 * @author jeekim
 * @since 2010-02-11
 */
public interface State {

	public State getHead();

	public State getTail();

	/**
	 * Retrieves all the {@link State}s that are reachable from this
	 * {@link State}. Note {@code a.getTransitions()} is equivalent to {@code
	 * a.getHead().getTransitions()}.
	 * 
	 * @return
	 */
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
