package com.jeeex.objregex.impl;

import static com.jeeex.objregex.impl.TransitionIdentifier.makeTid;

import java.util.List;

import com.jeeex.objregex.javacc.ASTConcatExpr;
import com.jeeex.objregex.javacc.ASTExpression;
import com.jeeex.objregex.javacc.ASTIdentifier;
import com.jeeex.objregex.javacc.ASTNegativeIdentifier;
import com.jeeex.objregex.javacc.ASTOperator;
import com.jeeex.objregex.javacc.ASTOperatorExpr;
import com.jeeex.objregex.javacc.ASTSpecialIdentifier;
import com.jeeex.objregex.javacc.ASTStart;
import com.jeeex.objregex.javacc.ASTTerm;
import com.jeeex.objregex.javacc.EnhancedNode;
import com.jeeex.objregex.javacc.SimpleNode;

/**
 * A Visitor that generates {@link State} from the Abstract Syntax Tree (AST) of
 * a regular expression. The second argument of the visitor is
 * {@link SingleTransitionFactory}, which is used to create single lazy
 * transitions.
 * <p>
 * The entry point for this class is {@link #start(ASTStart)}.
 * 
 * @author Jeeyoung Kim
 * @since 2010-02-12
 * 
 */
class ASTVisitor implements com.jeeex.objregex.javacc.RegexParserVisitor {

	/**
	 * Calls jjtAccept.
	 */
	private State accept(EnhancedNode node, SingleTransitionFactory manager) {
		return node.jjtAccept(this, manager);
	}

	private State firstChildAccept(EnhancedNode node,
			SingleTransitionFactory manager) {
		return accept(node.getFirstChild(), manager);
	}

	public State start(ASTStart start, SingleTransitionFactory manager) {
		State state = this.visit(start, manager);

		return state;
	}

	/**
	 * {@link ASTConcatExpr} may be empty.
	 */
	public State visit(ASTConcatExpr node, SingleTransitionFactory manager) {
		if (node.getNodeList().isEmpty()) {
			return StateUtil.emptyState();
		}
		State state = firstChildAccept(node, manager);

		for (EnhancedNode n : node.getNodeList(1)) {
			State concatState = accept(n, manager);
			state = StateUtil.concat(state, concatState);
		}
		return state;
	}

	public State visit(ASTExpression node, SingleTransitionFactory manager) {

		State state = firstChildAccept(node, manager);

		for (EnhancedNode n : node.getNodeList(1)) {
			State alternateState = accept(n, manager);
			state = StateUtil.or(state, alternateState);
		}

		return state;
	}

	/**
	 * Identifier is converted into a state with single node.
	 * <p>
	 * Identifier names are collected in the set {@link #ids}.
	 */
	public State visit(ASTIdentifier node, SingleTransitionFactory manager) {
		String id = node.jjtGetFirstToken().image;
		return manager.singleTransition(makeTid(id));
	}

	public State visit(ASTNegativeIdentifier node,
			SingleTransitionFactory manager) {
		String id = node.getFirstChild().jjtGetFirstToken().image;
		return manager.singleTransition(makeTid(id, true));
	}

	public State visit(ASTOperator node, SingleTransitionFactory manager) {
		throw new UnsupportedOperationException("Should never visit Operator.");
	}

	public State visit(ASTOperatorExpr node, SingleTransitionFactory manager) {
		State state = firstChildAccept(node, manager);

		List<RegexOperator> operators = RegexUtil.extractOperators(node);
		for (RegexOperator operator : operators) {
			switch (operator) {
			case STAR:
				state = StateUtil.kleineClosure(state);
				break;

			case PLUS:
				// A+ is defined as A A*.
				State head = firstChildAccept(node, manager);
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

	/**
	 * Create a transition for a special identifier.
	 */
	public State visit(ASTSpecialIdentifier node,
			SingleTransitionFactory manager) {
		String id = node.jjtGetFirstToken().image;
		return StateUtil.single(TransitionIdentifier.makeSpecialTid(id));
	}

	public State visit(ASTStart node, SingleTransitionFactory manager) {
		return firstChildAccept(node, manager);
	}

	/**
	 * A term is either Identifier, or Expression. either case, the state
	 * delegation logic is delegated to the first child.
	 */
	public State visit(ASTTerm node, SingleTransitionFactory manager) {
		return firstChildAccept(node, manager);
	}

	public State visit(SimpleNode node, SingleTransitionFactory manager) {
		throw new UnsupportedOperationException(
				"Generic SimpleNode should not be visited.");
	}
}
