/* Generated By:JJTree: Do not edit this line. ASTOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.jeeex.objregex.javacc;

public
class ASTOperator extends com.jeeex.objregex.javacc.EnhancedNode {
  public ASTOperator(int id) {
    super(id);
  }

  public ASTOperator(RegexParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public com.jeeex.objregex.impl.State jjtAccept(RegexParserVisitor visitor, com.jeeex.objregex.impl.SingleTransitionFactory data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=98bdeef2fd8e4e951b12c036909647ad (do not edit this line) */
