package com.jeeex.objregex.impl;

/**
 * This class serves two purposes
 * <ol>
 * <li>Factory for lazy single identifier transition states, via
 * {@link #singleTransition(TransitionIdentifier)}.
 * <li>Lazily initialize the produced states via
 * {@link #initializeLazySingle(LazyState, TransitionIdentifier, LeafState)}.
 * </ol>
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-20
 * 
 */
public abstract class SingleLazyStateManager implements SingleTransitionFactory {

	/**
	 * {@link LazyState} that represents a single transition.
	 */
	class SingleLazyState extends LazyState {

		/**
		 * Projected end of the transition.
		 */
		private final LeafState lazyHead;

		/**
		 * {@link TransitionIdentifier} which identifies this single transition.
		 */
		private final TransitionIdentifier tid;

		public SingleLazyState(TransitionIdentifier tid, LeafState head) {
			this.lazyHead = head;
			this.tid = tid;
		}

		/**
		 * Delegate to {@link SingleLazyStateManager}.
		 */
		@Override
		protected void lazyInit() {
			SingleLazyStateManager.this.initializeLazySingle(this.getTail(),
					tid, lazyHead);
		}

	}

	/**
	 * Initializes the given lazy single transition. Subclasses should override
	 * this method to specify lazy initialization behaviors.
	 * <p>
	 * This code must <b>not</b> call {@link State#getTransitions()} on {@code
	 * tail}, since it will cause infinite recursion.
	 * 
	 * @param tail
	 *            tail of the single transition NFA.
	 * @param identifier
	 *            {@link TransitionIdentifier} for the given lazily-initialized
	 *            transition.
	 * @param head
	 *            Projected head of the single transition NFA.
	 */
	public abstract void initializeLazySingle(LazyState tail,
			TransitionIdentifier identifier, LeafState head);

	/**
	 * Similar to {@link StateUtil#single(TransitionIdentifier)}, but creates
	 * lazily initialized transition.
	 */
	public State singleTransition(TransitionIdentifier id) {
		LeafState head = new LeafState();
		State tail = new SingleLazyState(id, head);
		return new CompositeState(head, tail);
	}
}
