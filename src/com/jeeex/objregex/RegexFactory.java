package com.jeeex.objregex;

import java.util.regex.PatternSyntaxException;

/**
 * Defines an interface that generates compiled regex patterns from string.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-05
 */
public interface RegexFactory {
	/**
	 * 
	 * @param <T>
	 *            Type of list that the compiled pattern can match against with.
	 * @param pattern
	 *            The regex pattern string.
	 * @return {@link ObjectPattern} object, underlying
	 *         {@link ObjectPattern#getRegex() regex} backed up by {@code
	 *         pattern}.
	 * @throws PatternSyntaxException
	 *             If the string {@code pattern} is an invalid regex string.
	 *             <b>Accepted regular expression patterns differ between the
	 *             implementations. </b>
	 */
	public <T> ObjectPattern<T> compile(String pattern)
			throws PatternSyntaxException;
}
