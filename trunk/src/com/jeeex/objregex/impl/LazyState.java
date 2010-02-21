package com.jeeex.objregex.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * A lazily initialized state.
 * <p>
 * This class is similar to {@link LeafState}. {@code this} ==
 * {@link #getHead()} == {@link #getTail()}. The object maintains its own
 * {@link #transitions}. However, when {@link #getTransitions()} is called for
 * the first time, {@link #lazyInit()} method is invoked to perform required
 * lazy initializations.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-20
 */
public abstract class LazyState implements State {

	private boolean initialized = false;
	private final Multimap<TransitionIdentifier, State> transitions = HashMultimap
			.create();

	public LazyState() {
	}

	public void addTransition(TransitionIdentifier id, State other) {
		transitions.put(id, other.getTail());
	}

	public LazyState getHead() {
		return this;
	}

	public LazyState getTail() {
		return this;
	}

	public Multimap<TransitionIdentifier, State> getTransitions() {
		if (!initialized) {
			// initialize when required.
			lazyInit();
			initialized = true;
		}
		return this.transitions;

	}

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Lazily initializes this state. Subclasses should override this method.
	 */
	protected abstract void lazyInit();

}
