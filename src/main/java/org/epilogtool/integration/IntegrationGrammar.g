grammar IntegrationGrammar;

@header {
package org.epilogtool.integration;
}


eval	returns [IntegrationFunctionSpecification.IntegrationExpression value]	
	: exp=expression { $value = $exp.value; }
	;

expression  	returns [IntegrationFunctionSpecification.IntegrationExpression value]
	: or=disjunction { $value = $or.value; } 
	;

disjunction	returns [IntegrationFunctionSpecification.IntegrationExpression value]
	: c1=conjunction (OR c2=conjunction)* {$value = IntegrationFunctionSpecification.createDisjunction($c1.value,$c2.value); }
	; 
	
conjunction    returns [IntegrationFunctionSpecification.IntegrationExpression value ]
	: a1=atom (AND a2=atom)* {$value = IntegrationFunctionSpecification.createConjunction($a1.value,$a2.value); }
	;
		
	

atom	 returns [ IntegrationFunctionSpecification.IntegrationExpression value]
	: id=ID '(' threshold=ENUMBER ',' min=ENUMBER ',' max=ENUMBER ')' 
	{ $value = IntegrationFunctionSpecification.createAtom($id.text,$threshold.text,$max.text,$min.text);}
	| id=ID '(' threshold=RANGE ',' min=ENUMBER ',' max=ENUMBER ')' 
	{ $value = IntegrationFunctionSpecification.createAtom($id.text,$threshold.text,$max.text,$min.text);}
	| id=ID '(' threshold=ENUMBER ',' min=ENUMBER ',' max=ENUMBER ',' dist=ENUMBER ')'
	{ $value = IntegrationFunctionSpecification.createAtom($id.text,$threshold.text,$min.text,$max.text,$dist.text);}
	| id=ID '(' threshold=RANGE ',' min=ENUMBER ',' max=ENUMBER ',' dist=ENUMBER ')'
	{ $value = IntegrationFunctionSpecification.createAtom($id.text,$threshold.text,$min.text,$max.text,$dist.text);}
	| id=ID '(' threshold=ENUMBER ',' min=ENUMBER ',' max=ENUMBER ',' dist=RANGE ')'
	{ $value = IntegrationFunctionSpecification.createAtom($id.text,$threshold.text,$min.text,$max.text,$dist.text);}
	| id=ID '(' threshold=RANGE ',' min=ENUMBER ',' max=ENUMBER ',' dist=RANGE ')'
	{ $value = IntegrationFunctionSpecification.createAtom($id.text,$threshold.text,$min.text,$max.text,$dist.text);}
	
	| '(' id1=ID ',' id2=ID ')' '(' threshold=ENUMBER ',' min=ENUMBER ',' max=ENUMBER ')' 
	{ $value = IntegrationFunctionSpecification.createAtom($id1.text,$id2.text,$threshold.text,$max.text,$min.text);}
	
	|'(' exp=expression ')' { $value = IntegrationFunctionSpecification.createAtom($exp.value);}
	| NOT a=atom { $value = IntegrationFunctionSpecification.createNegation($a.value);}
	;	
	
	

ENUMBER	:	('0' .. '9')+ | '_';
RANGE	:	('0' .. '9')+ ':' ('0' .. '9')+;
ID	:	('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')+;
OR	:	'|';
AND	:	'&';
NOT	:	'!';
	
	