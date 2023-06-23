grammar PostgreSQL;

/* Lexer rules */

// Keywords
SELECT : 'SELECT';
FROM : 'FROM';
WHERE : 'WHERE';
AND : 'AND';
OR : 'OR';

// Identifiers
IDENTIFIER : [a-zA-Z_][a-zA-Z0-9_]*;

// Operators
EQUALS : '=';
GREATER_THAN : '>';
LESS_THAN : '<';
GREATER_THAN_EQUALS : '>=';
LESS_THAN_EQUALS : '<=';
NOT_EQUALS : '!=';

// Symbols
LPAREN : '(';
RPAREN : ')';
ASTERISK : '*';
DOT : '.';
COMMA : ',';
SEMICOLON : ';';
SINGLE_QUOTE : '\'';
DOUBLE_QUOTE : '"';
BACKSLASH : '\\';

// Whitespace and line breaks
WS : [ \t\r\n]+ -> skip;

/* Parser rules */

query : selectStatement;

selectStatement : SELECT selectList FROM tableExpression (WHERE expression)?;

selectList : ASTERISK | column (COMMA column)*;

tableExpression : IDENTIFIER;

expression : logicalExpression;

logicalExpression : logicalTerm (OR logicalTerm)*;

logicalTerm : logicalFactor (AND logicalFactor)*;

logicalFactor : expressionElement (EQUALS expressionElement
                                 | GREATER_THAN expressionElement
                                 | LESS_THAN expressionElement
                                 | GREATER_THAN_EQUALS expressionElement
                                 | LESS_THAN_EQUALS expressionElement
                                 | NOT_EQUALS expressionElement)?;

expressionElement : IDENTIFIER;

column : IDENTIFIER;
