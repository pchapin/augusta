grammar Augusta;

/* ======= */
/* Grammar */
/* ======= */


// High Level Grammar
// ------------------

compilation_unit
    :   procedure_definition;

procedure_definition
    :   PROCEDURE IDENTIFIER IS declarations block IDENTIFIER? ';';


// Declaration Grammar
// -------------------

declarations
    :   declaration*;

declaration
    :   object_declaration;

object_declaration
    :   IDENTIFIER ':' IDENTIFIER (':=' expression)? ';';


// Statement Grammar
// -----------------

block
    :   BEGIN statement+ END;

statement
    :   assignment_statement
    |   conditional_statement
    |   iteration_statement
    |   null_statement;

assignment_statement
    :   left_expression ASSIGNMENT expression ';';

conditional_statement
    :   IF expression THEN thenStatements+=statement+
       (elsif_fragment)*
       (ELSE elseStatements+=statement+)?
        END IF ';';

// The problem with including elsif_fragment in the rule above is that I don't know how to
// distinguish the batches of statements in the various ELSIF parts. Unfortunately when building
// the CFG this rule seems to require two different exit points: one leading to the next ELSE if
// the expression is False, and one leading to the overall end when the statement list completes.
elsif_fragment
    :   ELSIF expression THEN statement+;

iteration_statement
    :   WHILE expression LOOP statement+ END LOOP ';';

null_statement
    :   NULL ';';


// Expression Grammar
// ------------------

primary_expression
    locals [String expressionType]
    :   IDENTIFIER
    |   NUMERIC_LITERAL
    |   BOOLEAN_LITERAL
    |   '(' expression ')';

multiplicative_expression
    locals [String expressionType]
    :   multiplicative_expression (MULTIPLY | DIVIDE | REM) primary_expression
    |   primary_expression;

unary_expression
    locals [String expressionType]
    :   (PLUS | MINUS) multiplicative_expression
    |   multiplicative_expression;

additive_expression
    locals [String expressionType]
    :   additive_expression (PLUS | MINUS)  unary_expression
    |   unary_expression;

relational_expression
    locals [String expressionType]
    :   relational_expression
          (EQUAL | NOT_EQUAL | LESS | LESS_EQUAL | GREATER | GREATER_EQUAL)
            additive_expression
    |   additive_expression;

expression
    locals [String expressionType]
    :   relational_expression;

left_expression
    locals [String expressionType]
    :   IDENTIFIER;


/* =========== */
/* Lexer rules */
/* =========== */

// --------------
// Reserved Words
// --------------

// Augusta reserves all the words of Ada to prevent programmers from accidentally creating
// identifiers like these. This avoids forward incompatibilities. Unlike Ada, Augusta is case
// sensitive. This simplifies the specification of the reserved words (among other things). See
// the language reference manual for a more detailed rationale.

ABORT        : 'abort';
ABS          : 'abs';
ABSTRACT     : 'abstract';
ACCEPT       : 'accept';
ACCESS       : 'access';
ALIASED      : 'aliased';
ALL          : 'all';
AND          : 'and';
ARRAY        : 'array';
AT           : 'at';
BEGIN        : 'begin';
BODY         : 'body';
CASE         : 'case';
CONSTANT     : 'constant';
DECLARE      : 'declare';
DELAY        : 'delay';
DELTA        : 'delta';
DIGITS       : 'digits';
DO           : 'do';
ELSE         : 'else';
ELSIF        : 'elsif';
END          : 'end';
ENTRY        : 'entry';
EXCEPTION    : 'exception';
EXIT         : 'exit';
FOR          : 'for';
FUNCTION     : 'function';
GENERIC      : 'generic';
GOTO         : 'goto';
IF           : 'if';
IN           : 'in';
INTERFACE    : 'interface';
IS           : 'is';
LIMITED      : 'limited';
LOOP         : 'loop';
MOD          : 'mod';
NEW          : 'new';
NOT          : 'not';
NULL         : 'null';
OF           : 'of';
OR           : 'or';
OTHERS       : 'others';
OUT          : 'out';
OVERRIDING   : 'overriding';
PACKAGE      : 'package';
PARALLEL     : 'parallel';
PRAGMA       : 'pragma';
PRIVATE      : 'private';
PROCEDURE    : 'procedure';
PROTECTED    : 'protected';
RAISE        : 'raise';
RANGE        : 'range';
RECORD       : 'record';
REM          : 'rem';
RENAMES      : 'renames';
REQUEUE      : 'requeue';
RETURN       : 'return';
REVERSE      : 'reverse';
SELECT       : 'select';
SEPARATE     : 'separate';
SOME         : 'some';
SUBTYPE      : 'subtype';
SYNCHRONIZED : 'synchronized';
TAGGED       : 'tagged';
TASK         : 'task';
TERMINATE    : 'terminate';
THEN         : 'then';
TYPE         : 'type';
UNTIL        : 'until';
USE          : 'use';
WHEN         : 'when';
WHILE        : 'while';
WITH         : 'with';
XOR          : 'xor';

// Various operator symbols.
ASSIGNMENT    : ':=';
DIVIDE        : '/';
DOTDOT        : '..';
EQUAL         : '=';
GREATER       : '>';
GREATER_EQUAL : '>=';
LESS          : '<';
LESS_EQUAL    : '<=';
MINUS         : '-';
MULTIPLY      : '*';
NOT_EQUAL     : '/=';
PLUS          : '+';

BOOLEAN_LITERAL
    :   'True' | 'False';

IDENTIFIER
    :   [a-zA-Z][a-zA-Z0-9_]*;

WHITESPACE
    :   [ \t\f\r\n]+  -> skip;

COMMENT
    :   '--' .*? [\r\n] -> skip;

// Just integer literals for now.
// Note that Ada allows underscores to appear in the exponent.
NUMERIC_LITERAL
    :   (DECIMAL | BASED) ( ('E' | 'e') '+'? DIGIT+ )?;

fragment DECIMAL
    :   DIGIT ('_'? DIGIT)*;

// Note that Ada allows underscores to appear in the base.
fragment BASED
    :   DIGIT+ '#' HEX_DIGIT ('_'? HEX_DIGIT)* '#';

fragment DIGIT
    :   [0-9];

fragment HEX_DIGIT
    :   [0-9a-fA-F];
