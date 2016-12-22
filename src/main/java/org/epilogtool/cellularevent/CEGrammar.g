grammar CEGrammar;

@header {
package org.epilogtool.cellularevent;
}

/*****************************************************************************/
/* Rules */

eval	returns [CellularEventExpression value]
	: exp=expror { $value = $exp.value; }
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
	: expr=node
		{ $value = $expr.value; }
	| NOT expr=node
		{ $value = CESpecification.cellularEventNOT($expr.value); }
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
