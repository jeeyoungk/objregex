package com.jeeex.objregex.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList.Builder;
import com.jeeex.objregex.ObjectPattern;

/**
 * An implementation of {@link ObjectPattern}.
 * <p>
 * The users of objregex should not initiate this class directly -
 * {@link DefaultRegexFactory} should be used instead.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-14
 * 
 */
class ObjectPatternImpl<T> implements ObjectPattern<T> {
	private final Map<String, Predicate<T>> idToPredicate = Maps.newHashMap();
	private ImmutableSet<String> identifiers;
	private final String regex;

	/**
	 * State graph generated from the string {@link #regex}.
	 */
	private State state;

	/**
	 * An object that represents the start of input in the augmented list.
	 */
	private static final Object AUGMENTED_START = new Object[0];

	/**
	 * An object that represents the end of input in the augmented list.
	 */
	private static final Object AUGMENTED_END = new Object[0];

	/**
	 * Augment the input list with {@link #AUGMENTED_START} at the beginning and
	 * {@link #AUGMENTED_END} at the end.
	 * 
	 * @return The augmented list.
	 */
	static ImmutableList<Object> buildAugmentedInput(List<?> input) {
		Builder<Object> builder = ImmutableList.builder();
		builder.add(AUGMENTED_START);
		builder.addAll(input);
		builder.add(AUGMENTED_END);
		return builder.build();
	}

	ObjectPatternImpl(String regex) {
		this.regex = regex;
	}

	public boolean apply(List<? extends T> input) {
		return match(input);
	}

	public void compile() {
		ASTVisitor visitor = new ASTVisitor();
		State state = visitor.start(RegexUtil.getRootNode(this.regex));
		// finished - set up stuff.
		this.state = state;
	}

	/**
	 * Consume a token of input
	 * 
	 * @param states
	 *            Starting set of the NFA states
	 * @param token
	 *            Token to be consumed.
	 * @return Set of NFA states after {@code token} has been consumed. That is,
	 *         set of all states that is connected by a
	 *         {@link TransitionIdentifier} that evaluates {@code token} to
	 *         True.
	 */
	Set<State> consume(final Set<State> states, T token) {
		// set of all transition id's going out from the states.
		final Set<TransitionIdentifier> outgoingTransitions = StateUtil
				.getOutgoingTransitions(states);
		final ImmutableSet.Builder<State> nextStatesBuilder = ImmutableSet
				.builder();

		for (TransitionIdentifier tid : outgoingTransitions) {
			if (tid.equals(TransitionIdentifier.EPSILON)) {
				// EPSILON is not mapped with any predicate.
				continue;
			} else if (idToPredicate.get(tid.getId()).apply(token)) {
				// if the predicate evaluates to true, than traverse the
				// sets.
				nextStatesBuilder.addAll(StateUtil.traverse(states, tid));
			}
		}
		return nextStatesBuilder.build();
	}

	public String getRegex() {
		return regex;
	}

	public boolean match(List<? extends T> input) throws NullPointerException {

		// temporary, current set of states reached by the regex engine.
		// starts from the transitive closure of state.getTail().
		Set<State> currentStates = StateUtil.transitiveClosure(ImmutableSet
				.of(state.getTail()));

		for (T curToken : input) {
			// consume the token, and take the transitive closure.
			currentStates = StateUtil.transitiveClosure(consume(currentStates,
					curToken));
			// states cannot grow if it's empty, so terminate the loop.
			if (currentStates.isEmpty()) {
				break;
			}
		}

		// 
		return currentStates.contains(state.getHead());
	}

	public void set(String identifier, ObjectPattern<T> pattern)
			throws NullPointerException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void set(String identifier, Predicate<T> predicate)
			throws NullPointerException {
		checkNotNull(identifier);
		checkNotNull(predicate);
		idToPredicate.put(identifier, predicate);
	}

}
