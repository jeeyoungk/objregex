/* Generated By:JJTree: Do not edit this line. ASTExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.jeeex.objregex.javacc;

public
class ASTExpression extends com.jeeex.objregex.javacc.EnhancedNode {
  public ASTExpression(int id) {
    super(id);
  }

  public ASTExpression(RegexParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public com.jeeex.objregex.impl.State jjtAccept(RegexParserVisitor visitor, com.jeeex.objregex.impl.SingleTransitionFactory data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=09e153ca210a800c7df7a36b7da157dc (do not edit this line) */
