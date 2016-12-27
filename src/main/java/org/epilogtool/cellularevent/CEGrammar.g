grammar CEGrammar;

@header {
package org.epilogtool.cellularevent;
}

@parser::members {
  @Override
  public void reportError(RecognitionException e) {
    throw new RuntimeException("I quit!\n" + e.getMessage());
  }
}

@lexer::members {
  @Override
  public void reportError(RecognitionException e) {
    throw new RuntimeException("I quit!\n" + e.getMessage());
  }
}

/*****************************************************************************/
/* Rules */

eval	returns [CellularEventExpression value]
	: exp=expror EOF { $value = $exp.value; }
	;

expror returns [CellularEventExpression value]
	: o1=exprand (OR o2=exprand)*
		{ $value = CESpecification.cellularEventOperationOR($o1.value,$o2.value); }
	; 

exprand returns [CellularEventExpression value]
	: a1=exprnot (AND a2=exprnot)*
		{ $value = CESpecification.cellularEventOperationAND($a1.value,$a2.value); }
	;

exprnot returns [CellularEventExpression value]
	: expr=exprparen
		{ $value = $expr.value; }
	| NOT expr=exprparen
		{ $value = CESpecification.cellularEventNOT($expr.value); }
	;

exprparen returns [CellularEventExpression value]
	: '(' expr=expror ')'
		{ $value = $expr.value; }
	| expr=node
		{ $value = $expr.value; }
	;

node returns [CellularEventExpression value]
	: id=ID
		{ $value = CESpecification.cellularEventNode($id.text, "1"); }
	| id=ID ':' t=NUMBER
		{ $value = CESpecification.cellularEventNode($id.text, $t.text); }
	;

/*****************************************************************************/
/* Macros */
ID     : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
NUMBER : '0'..'9';
OR     : '|';
AND    : '&';
NOT    : '!';
WS     : SPACE+ {skip();};
fragment SPACE : '\t' | ' ' | '\r' | '\n'| '\u000C';