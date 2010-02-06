package com.jeeex.objregex;

import java.util.List;

import com.google.common.base.Predicate;

/**
 * Defines the interface for ObjectPattern, which determines whether a given
 * {@link List} matches against the underlying regular expression.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-25
 * 
 * @param <T>
 * @see #match(List), #set
 */
public interface ObjectPattern<T> {
	/**
	 * Determines whether the given list matches against the underlying regular
	 * expression.
	 * 
	 * @param input
	 *            the input list.
	 * @return {@code true} if the input satisfies the underlying regular
	 *         expression, {@code false} otherwise.
	 * @throws NullPointerException
	 *             if the input is {@code null}.
	 */
	public boolean match(List<T> input) throws NullPointerException;

	/**
	 * Pairs up the given identifier and predicate in this pattern.
	 * 
	 * @throws NullPointerException
	 *             If any of the arguments are null.
	 */
	public void set(String identifier, Predicate<T> predicate)
			throws NullPointerException;

	/**
	 * Returns the underlying regular expression string.
	 */
	public String getRegex();
}
