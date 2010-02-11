package com.jeeex.objregex.impl;

/**
 * An enum that represents the postfix regular expression operators, *, ?, and
 * +.
 * 
 * @author jeekim
 * 
 */
public enum RegexOperator {
	STAR("*"), QUESTION("?"), PLUS("+");

	private String literal;

	RegexOperator(String literal) {
		this.literal = literal;
	}

	/**
	 * From the string "*", "?", or "+", obtain a {@link RegexOperator}.
	 * 
	 * @throws IllegalArgumentException
	 *             If {@code literal} is not mentioned above.
	 */
	public static RegexOperator get(String literal)
			throws IllegalArgumentException {
		for (RegexOperator op : RegexOperator.values()) {
			if (op.literal.equals(literal)) {
				return op;
			}

		}
		throw new IllegalArgumentException("No matching Operator for literal.");
	}

	/**
	 * Returns the string form of the operator.
	 */
	@Override
	public String toString() {
		return this.literal;
	}
}