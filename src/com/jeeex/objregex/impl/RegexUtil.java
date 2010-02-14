package com.jeeex.objregex.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jeeex.objregex.ObjectPattern;
import com.jeeex.objregex.javacc.ASTOperatorExpr;
import com.jeeex.objregex.javacc.ASTStart;
import com.jeeex.objregex.javacc.EnhancedNode;
import com.jeeex.objregex.javacc.ParseException;
import com.jeeex.objregex.javacc.RegexParser;

/**
 * Utility method for regular expression.
 * <p>
 * This class is declared as package private. Methods provided by
 * {@link RegexUtil} is solely to aid the implementation, and the users of
 * {@link ObjectPattern} shouldn't need to worry about this class.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-11
 */
public class RegexUtil {

	private RegexUtil() {
	}

	/**
	 * TODO(Jeeyoung Kim) {@link PatternSyntaxException} should include better
	 * error messages.
	 * <p>
	 * Returns the root of the AST of the provided regular expression.
	 * 
	 * @param pattern
	 *            Regular expression string pattern.
	 * @return {@link EnhancedNode} representing the root of the Abstract Syntax
	 *         Tree of the regex string.
	 * @throws PatternSyntaxException
	 *             If {@code pattern} is not a vaid pattern.
	 */
	public static ASTStart getRootNode(String pattern)
			throws PatternSyntaxException {
		Reader reader = new StringReader(pattern);
		RegexParser parser = new RegexParser(reader);
		try {
			return (ASTStart) parser.Start();
		} catch (ParseException e) {
			// TODO(Jeeyoung Kim) Exception should provide better message to the
			// users.
			throw new PatternSyntaxException("", pattern, 0);
		}
	}

	/**
	 * Extract the operators from the {@link ASTOperatorExpr} node.
	 * 
	 * @return {@link Lists} of {@link RegexOperator}s, as they appear in the
	 *         input node.
	 */
	public static List<RegexOperator> extractOperators(ASTOperatorExpr node) {
		return Lists.transform(node.getNodeList(1),
				new Function<EnhancedNode, RegexOperator>() {
					public RegexOperator apply(EnhancedNode arg) {
						return RegexOperator.get(arg.jjtGetFirstToken().image);
					}
				});
	}
}
