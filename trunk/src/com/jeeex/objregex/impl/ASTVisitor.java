package com.jeeex.objregex.impl;

import java.util.List;

import com.jeeex.objregex.javacc.ASTConcatExpr;
import com.jeeex.objregex.javacc.ASTExpression;
import com.jeeex.objregex.javacc.ASTIdentifier;
import com.jeeex.objregex.javacc.ASTOperator;
import com.jeeex.objregex.javacc.ASTOperatorExpr;
import com.jeeex.objregex.javacc.ASTStart;
import com.jeeex.objregex.javacc.ASTTerm;
import com.jeeex.objregex.javacc.EnhancedNode;
import com.jeeex.objregex.javacc.SimpleNode;

/**
 * A Visitor that generates {@link State} from the Abstract Syntax Tree (AST) of
 * a regular expression. The second argument of the visit method is unused.
 * <p>
 * The entry point for this class is {@link #start(ASTStart)}.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-12
 * 
 */
class ASTVisitor implements com.jeeex.objregex.javacc.RegexParserVisitor {

	/**
	 * Shortcut for jjtAccept, with null passed as data.
	 */
	private State accept(EnhancedNode node) {
		return node.jjtAccept(this, null);
	}

	private State firstChildAccept(EnhancedNode node) {
		return accept(node.getFirstChild());
	}

	/**
	 * {@link ASTConcatExpr} may be empty.
	 */
	public State visit(ASTConcatExpr node, Void v) {
		if (node.getNodeList().isEmpty()) {
			return StateUtil.emptyState();
		}
		State state = firstChildAccept(node);

		for (EnhancedNode n : node.getNodeList(1)) {
			State concatState = accept(n);
			state = StateUtil.concat(state, concatState);
		}
		return state;
	}

	public State visit(ASTExpression node, Void v) {

		State state = firstChildAccept(node);

		for (EnhancedNode n : node.getNodeList(1)) {
			State alternateState = accept(n);
			state = StateUtil.or(state, alternateState);
		}

		return state;
	}

	/**
	 * Identifier is converted into a state with single node.
	 * <p>
	 * Identifier names are collected in the set {@link #ids}.
	 */
	public State visit(ASTIdentifier node, Void v) {
		String id = node.jjtGetFirstToken().image;
		return StateUtil.single(TransitionIdentifier.makeTid(id));
	}

	public State visit(ASTOperator node, Void v) {
		throw new UnsupportedOperationException("Should never visit Operator.");
	}

	public State visit(ASTOperatorExpr node, Void v) {
		State state = firstChildAccept(node);

		List<RegexOperator> operators = RegexUtil.extractOperators(node);
		for (RegexOperator operator : operators) {
			switch (operator) {
			case STAR:
				state = StateUtil.kleineClosure(state);
				break;

			case PLUS:
				// A+ is defined as A A*.
				State head = firstChildAccept(node);
				State kleineClosure = StateUtil.kleineClosure(state);
				state = StateUtil.concat(head, kleineClosure);
				break;

			case QUESTION:
				state = StateUtil.or(state, StateUtil.emptyState());
				break;
			default:
				throw new UnsupportedOperationException(
						"Implement something else.");
			}
		}
		return state;
	}

	public State visit(ASTStart node, Void v) {
		return firstChildAccept(node);
	}

	/**
	 * A term is either Identifier, or Expression. either case, the state
	 * delegation logic is delegated to the first child.
	 */
	public State visit(ASTTerm node, Void v) {
		return firstChildAccept(node);
	}

	public State visit(SimpleNode node, Void v) {
		throw new UnsupportedOperationException(
				"Generic SimpleNode should not be visited.");
	}

	public State start(ASTStart start) {
		State state = this.visit(start, null);

		return state;
	}
}
