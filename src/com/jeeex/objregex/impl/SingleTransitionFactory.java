package com.jeeex.objregex.impl;

/**
 * Interface that defines a method that creates a single transition NFA
 * {@link State} for the given {@link TransitionIdentifier}.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-21
 * 
 */
public interface SingleTransitionFactory {

	/**
	 * Create a single transition.
	 */
	public State singleTransition(TransitionIdentifier tid)
			throws NullPointerException;
}
