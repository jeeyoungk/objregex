package com.jeeex.objregex.impl;

import com.google.common.base.Objects;

/**
 * An immutable datatype to identify the transition between the states.
 * <p>
 * {@link TransitionIdentifier} is either {@link #isSpecial() special} or not .
 * Special TransitionIdentifiers are predefined, and they're visible as static
 * members in this class. Currently, {@link #EPSILON} is the only special
 * identifier. Users should use the method {@link #make(String)} to create
 * {@link TransitionIdentifier}. {@link #make(String)} method returns nonspecial
 * identifier.
 * 
 * @author jeekim
 * 
 */
public class TransitionIdentifier {
	/**
	 * Represents a transition that does not consume any input.
	 */
	public static final TransitionIdentifier EPSILON = new TransitionIdentifier(
			true, "EPSILON");

	/**
	 * Initializes a non-special special id, with given string identifier.
	 */
	public static TransitionIdentifier make(String id) {
		return new TransitionIdentifier(false, id);
	}

	private final boolean special;

	private final String id;

	private TransitionIdentifier(boolean special, String id) {
		this.special = special;
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

}
