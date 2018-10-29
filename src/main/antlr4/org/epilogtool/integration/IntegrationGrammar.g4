grammar IntegrationGrammar;

/*****************************************************************************/
/* Rules */

eval	returns [IntegrationFunctionExpression value]
	: exp=functionexpror EOF 
	    { $value = $exp.value; }
	;

functionexpror returns [IntegrationFunctionExpression value]
	: o1=functionexpror OR o2=functionexprand
		{ $value = IFSpecification.integrationFunctionBinOpOR($o1.value,$o2.value); }
	| expr=functionexprand
	    { $value = $expr.value; }
	; 

functionexprand returns [IntegrationFunctionExpression value]
	: a1=functionexprand AND a2=functionexprnot
		{ $value = IFSpecification.integrationFunctionBinOpAND($a1.value,$a2.value); }
	| expr=functionexprnot
	    { $value = $expr.value; }
	;

functionexprnot returns [IntegrationFunctionExpression value]
	: expr=functionparen
		{ $value = $expr.value; }
	| NOT expr=functionparen
		{ $value = IFSpecification.integrationFunctionNOT($expr.value); }
	;

functionparen returns [IntegrationFunctionExpression value]
	: '(' e1=functionexpror ')'
		{ $value = $e1.value; }
	| e2=cardconst
		{ $value = $e2.value; }
	| 'TRUE'
	    { $value = IFSpecification.integrationFunctionTRUE(); }
	| 'FALSE'
	    { $value = IFSpecification.integrationFunctionFALSE(); }
	;

cardconst returns [IntegrationFunctionExpression value ]
	: '{' expr=signalexpror '}'
		{ $value = IFSpecification.cardinalityConstraint($expr.value, "1", "-1"); }
	| '{' expr=signalexpror ',' 'min' '=' min=NUMBER '}'
		{ $value = IFSpecification.cardinalityConstraint($expr.value, $min.text, "-1"); }
	| '{' expr=signalexpror ',' 'max' '=' max=NUMBER '}'
		{ $value = IFSpecification.cardinalityConstraint($expr.value, "-1", $max.text); }
	| '{' expr=signalexpror ',' 'min' '=' min=NUMBER ',' 'max' '=' max=NUMBER '}'
		{ $value = IFSpecification.cardinalityConstraint($expr.value, $min.text, $max.text); }
	;

signalexpror returns [IntegrationSignalExpression value]
	: o1=signalexpror OR o2=signalexprand
		{ $value = IFSpecification.integrationSignalBinOpOR($o1.value,$o2.value); }
	| expr=signalexprand
	    { $value = $expr.value; }
	;

signalexprand returns [IntegrationSignalExpression value]
	: o1=signalexprand OR o2=signal
		{ $value = IFSpecification.integrationSignalBinOpAND($o1.value,$o2.value); }
	| expr=signal
	    { $value = $expr.value; }
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
NUMBER : ('0'..'9')+;
OR     : '|';
AND    : '&';
NOT    : '!';
WS     : SPACE+ {skip();};
fragment SPACE : '\t' | ' ' | '\r' | '\n'| '\u000C';
