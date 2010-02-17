package com.jeeex.objregex.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

/**
 * An immutable datatype to identify the transition between the states.
 * <p>
 * {@link TransitionIdentifier} is either {@link #isSpecial() special} or not .
 * Special TransitionIdentifiers are predefined, and they're available as the
 * static constants in this class. Currently, {@link #EPSILON}, {@link #EOF},
 * and {@link #BOF} are the only special identifiers. Users should use the
 * method {@link #makeTid(String)} to create {@link TransitionIdentifier}.
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
	public static final TransitionIdentifier EPSILON = new TransitionIdentifier(
			true, "EPSILON");

	/**
	 * Represents a transition that can only be consumed by end-of-file.
	 */
	public static final TransitionIdentifier EOF = new TransitionIdentifier(
			true, "$");
	/**
	 * Represents a transition that can only be consumed by beginning-of-file.
	 */
	public static final TransitionIdentifier BOF = new TransitionIdentifier(
			true, "^");

	/**
	 * Initializes a {@link TransitionIdentifier}. If {@code} id is "$" or "^",
	 * then the special {@link TransitionIdentifier} {@link #BOF} or
	 * {@link #EOF} is returned. Otherwise, a nonspecial
	 * {@link TransitionIdentifier} for the given {@code id} is returned.
	 * 
	 * @throws NullPointerException
	 *             If id is null.
	 */
	public static TransitionIdentifier makeTid(String id)
			throws NullPointerException {
		if (id.equals("$")) {
			return EOF;
		} else if (id.equals("^")) {
			return BOF;
		}
		return new TransitionIdentifier(false, id);
	}

	private final boolean special;

	private final String id;

	private TransitionIdentifier(boolean special, String id)
			throws NullPointerException {
		checkNotNull(id);
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

	@Override
	public String toString() {
		return id;
	}

}
