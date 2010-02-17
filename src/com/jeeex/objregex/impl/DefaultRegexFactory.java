package com.jeeex.objregex.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.PatternSyntaxException;

import com.google.common.base.Predicates;
import com.jeeex.objregex.ObjectPattern;
import com.jeeex.objregex.RegexFactory;

/**
 * Default {@link RegexFactory}.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-14
 * 
 */
public class DefaultRegexFactory implements RegexFactory {

	public <T> ObjectPattern<T> compile(String pattern)
			throws PatternSyntaxException {
		checkNotNull(pattern);

		ObjectPatternImpl<T> patternImpl = new ObjectPatternImpl<T>(pattern);

		patternImpl.compile();

		addDefaultPredicates(patternImpl);

		return patternImpl;
	}

	private <T> void addDefaultPredicates(ObjectPattern<T> pattern) {
		pattern.set("null", Predicates.<T> isNull());
	}
}
