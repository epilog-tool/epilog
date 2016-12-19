grammar IntegrationGrammar;

@header {
package org.epilogtool.integration;
}

/*****************************************************************************/
/* Rules */

eval	returns [IntegrationFunctionExpression value]
	: exp=functionexpror { $value = $exp.value; }
	;
/*
functionexpr returns [IntegrationFunctionExpression value]
	: '(' expr=functionexpr ')'
		{ $value = $expr.value; }
	| NOT expr=functionexpr
		{ $value = IFSpecification.integrationFunctionNOT($expr.value); }
	| functionexpror
		{ $value = $expr.value; }
	| cardconst
		{ $value = $expr.value; }
	;
*/
functionexpror returns [IntegrationFunctionExpression value]
	: o1=functionexprand (OR o2=functionexprand)*
		{ $value = IFSpecification.integrationFunctionOperationOR($o1.value,$o2.value); }
	; 

functionexprand returns [IntegrationFunctionExpression value]
	: a1=functionexprnot (AND a2=functionexprnot)*
		{ $value = IFSpecification.integrationFunctionOperationAND($a1.value,$a2.value); }
	;

functionexprnot returns [IntegrationFunctionExpression value]
	: expr=cardconst
		{ $value = $expr.value; }
	| NOT expr=cardconst
		{ $value = IFSpecification.integrationFunctionNOT($expr.value); }
	;

cardconst returns [IntegrationFunctionExpression value ]
	: '{' expr=signalexpror ',' 'min=' min=NUMBER '}'
		{ $value = IFSpecification.cardinalityConstraint($expr.value, $min.text, "-1"); }
	| '{' expr=signalexpror ',' 'max=' max=NUMBER '}'
		{ $value = IFSpecification.cardinalityConstraint($expr.value, "-1", $max.text); }
	| '{' expr=signalexpror ',' 'min=' min=NUMBER ',' 'max=' max=NUMBER '}'
		{ $value = IFSpecification.cardinalityConstraint($expr.value, $min.text, $max.text); }
	;

signalexpror returns [IntegrationSignalExpression value]
	: o1=signal (OR o2=signal)*
		{ $value = IFSpecification.integrationSignalOperationOR($o1.value,$o2.value); }
	; 

signal returns [IntegrationSignal value]
	: id=ID
		{ $value = IFSpecification.integrationSignal($id.text, "1", new IntegrationDistance("1", "1")); }
	| id=ID '[' min=NUMBER ']'
		{ $value = IFSpecification.integrationSignal($id.text, "1", new IntegrationDistance($min.text, $min.text)); }
	| id=ID '[' min=NUMBER ':' ']'
		{ $value = IFSpecification.integrationSignal($id.text, "1", new IntegrationDistance($min.text, "-1")); }
	| id=ID '[' min=NUMBER ':' max=NUMBER ']'
		{ $value = IFSpecification.integrationSignal($id.text, "1", new IntegrationDistance($min.text, $max.text)); }
	| id=ID '[' ':' max=NUMBER ']'
		{ $value = IFSpecification.integrationSignal($id.text, "1", new IntegrationDistance("-1", $max.text)); }
	| id=ID ':' t=NUMBER
		{ $value = IFSpecification.integrationSignal($id.text, $t.text, new IntegrationDistance("1", "1")); }
	| id=ID ':' t=NUMBER '[' min=NUMBER ']'
		{ $value = IFSpecification.integrationSignal($id.text, $t.text, new IntegrationDistance($min.text, $min.text)); }
	| id=ID ':' t=NUMBER '[' min=NUMBER ':' ']'
		{ $value = IFSpecification.integrationSignal($id.text, $t.text, new IntegrationDistance($min.text, "-1")); }
	| id=ID ':' t=NUMBER '[' min=NUMBER ':' max=NUMBER ']'
		{ $value = IFSpecification.integrationSignal($id.text, $t.text, new IntegrationDistance($min.text, $max.text)); }
	| id=ID ':' t=NUMBER '[' ':' max=NUMBER ']'
		{ $value = IFSpecification.integrationSignal($id.text, $t.text, new IntegrationDistance("-1", $max.text)); }
	;

/*****************************************************************************/
/* Macros */
ID     : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
NUMBER : '0'..'9';
OR     : '|';
AND    : '&';
NOT    : '!';
