package com.jeeex.objregex.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * An immutable datatype to identify the transition between the states.
 * <p>
 * {@link TransitionIdentifier} is either {@link #isSpecial() special} or not .
 * Special TransitionIdentifiers are predefined, and they're available as the
 * static constants in this class. Currently, {@link #EPSILON}, {@link #EOF},
 * and {@link #BOF} are the only special identifiers. Users should use the
 * method {@link #makeTid(String)} to create {@link TransitionIdentifier}, or
 * {@link #makeSpecialTid(String)} to obtain special
 * {@link TransitionIdentifier}
 * <p>
 * {@link TransitionIdentifier} is immutable.
 * 
 * @author jeekim
 * 
 */
public class TransitionIdentifier {
	/**
	 * Represents a transition that does not consume any input.
	 */
	public static final TransitionIdentifier EPSILON;
	/**
	 * Represents a transition that can only be consumed by end-of-file.
	 */
	public static final TransitionIdentifier EOF;
	/**
	 * Represents a transition that can only be consumed by beginning-of-file.
	 */
	public static final TransitionIdentifier BOF;

	/**
	 * Map from id(String) to Special {@link TransitionIdentifier}s with given
	 * {@link #getId()}.
	 */
	private static final ImmutableMap<String, TransitionIdentifier> SPECIAL_IDS;

	static {
		// initialize STATIC FINAL members.
		Builder<String, TransitionIdentifier> builder = ImmutableMap.builder();

		EOF = premakeSpecial("$", builder);
		BOF = premakeSpecial("^", builder);
		EPSILON = premakeSpecial("EPSILON", builder);

		SPECIAL_IDS = builder.build();
	}

	/**
	 * Returns a special {@link TransitionIdentifier} with given id.
	 * 
	 * @throws IllegalArgumentException
	 *             If no such special {@link TransitionIdentifier} exists.
	 */
	public static TransitionIdentifier makeSpecialTid(String id)
			throws IllegalArgumentException {
		if (!SPECIAL_IDS.containsKey(id)) {
			throw new IllegalArgumentException("No such special Tid exists.");
		}
		return SPECIAL_IDS.get(id);
	}

	/**
	 * Initializes a nonspecial {@link TransitionIdentifier} with given {@code
	 * id}.
	 * 
	 * @throws NullPointerException
	 *             If id is null.
	 */
	public static TransitionIdentifier makeTid(String id)
			throws NullPointerException {
		return makeTid(id, false);
	}

	public static TransitionIdentifier makeTid(String id, boolean negation)
			throws NullPointerException {
		return new TransitionIdentifier(false, negation, id);
	}

	/**
	 * Create a special {@link TransitionIdentifier} with given {@code id}, and
	 * add it to the {@code builder}.
	 */
	private static TransitionIdentifier premakeSpecial(String id,
			Builder<String, TransitionIdentifier> builder) {
		TransitionIdentifier tid = new TransitionIdentifier(true, false, id);
		builder.put(id, tid);
		return tid;
	}

	private final boolean special;

	private final boolean negation;

	private final String id;

	private TransitionIdentifier(boolean special, boolean negation, String id)
			throws NullPointerException {
		checkNotNull(id);
		this.special = special;
		this.negation = negation;
		this.id = id;
	}

	public boolean equals(Object other) {
		if (other instanceof TransitionIdentifier) {
			TransitionIdentifier that = (TransitionIdentifier) other;
			return Objects.equal(this.id, that.id)
					&& Objects.equal(this.special, that.special);
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public int hashCode() {
		return Objects.hashCode(id, special);
	}

	public boolean isSpecial() {
		return special;
	}

	public boolean isNegation() {
		return negation;
	}

	@Override
	public String toString() {
		return (negation ? "!" : "") + id;
	}
}
