grammar FunctionGrammar;

@header {
package org.epilogtool.function;
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

eval	returns [FunctionExpression value]
	: exp=functionexpror EOF { $value = $exp.value; }
	;

functionexpror returns [FunctionExpression value]
	: o1=functionexprand (OR o2=functionexprand)*
		{ $value = FSpecification.functionOperationOR($o1.value,$o2.value); }
	; 

functionexprand returns [FunctionExpression value]
	: a1=functionexprnot (AND a2=functionexprnot)*
		{ $value = FSpecification.functionOperationAND($a1.value,$a2.value); }
	;

functionexprnot returns [FunctionExpression value]
	: expr=functionparen
		{ $value = $expr.value; }
	| NOT expr=functionparen
		{ $value = FSpecification.functionNOT($expr.value); }
	;

functionparen returns [FunctionExpression value]
	: '(' expr=functionexpror ')'
		{ $value = $expr.value; }
	| expr=componentexpr
		{ $value = $expr.value; }
	| 'TRUE'
		{ $value = FSpecification.functionTRUE(); }
	| 'FALSE'
		{ $value = FSpecification.functionFALSE(); }
	;

componentexpr returns [FunctionExpression value ]
	: id=ID
		{ $value = FSpecification.functionComponent($id.text, "1"); }
	| id=ID ':' t=NUMBER
		{ $value = FSpecification.functionComponent($id.text, $t.text); }
	;

/*****************************************************************************/
/* Macros */
ID     : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
NUMBER : ('0'..'9')+;
OR     : '|';
AND    : '&';
NOT    : '!';
WS     : SPACE+ {skip();};
fragment SPACE : '\t' | ' ' | '\r' | '\n'| '\u000C';
