grammar Augusta;

/* ======= */
/* Grammar */
/* ======= */


// High Level Grammar
// ------------------

// Augusta only allows compilation units that are a sequence of subprogram definitions.
// Ada-style packages are not currently supported.
compilation_unit
    :   subprogram_definition+;

subprogram_definition
    :   procedure_definition | function_definition;

procedure_definition
    :   PROCEDURE IDENTIFIER parameter_list? IS declarations block IDENTIFIER? ';';

function_definition
    :   FUNCTION IDENTIFIER parameter_list? RETURN IDENTIFIER IS declarations block IDENTIFIER? ';';

parameter_list
    :   '(' parameter_declaration+ ')';

// In Ada the mode is optional and defaults to IN. Augusta requires the mode to be specified.
// In Ada parameter declarations can have default values. Augusta does not support this feature.
parameter_declaration
    :   IDENTIFIER ':' (IN | OUT | IN OUT) IDENTIFIER;

// Declaration Grammar
// -------------------

// There is no mechanism for declaring types in Augusta. All types are built-in.
// There is no mechanism for declaring subprograms in Augusta.
// There are no arrays in Augusta (yet).
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

// Augusta only supports the IF statement. The CASE statement is not currently supported.
conditional_statement
    :   IF expression THEN thenStatements+=statement+
       (elsif_fragment)*
       (ELSE elseStatements+=statement+)?
        END IF ';';

elsif_fragment
    :   ELSIF expression THEN statement+;

// Augusta only supports the WHILE loop. The FOR loop is not currently supported.
iteration_statement
    :   WHILE expression LOOP statement+ END LOOP ';';

null_statement
    :   NULL ';';


// Expression Grammar
// ------------------

primary_expression
    locals [String expressionType]
    :   IDENTIFIER
    |   INTEGER_LITERAL
    |   REAL_LITERAL
    |   BOOLEAN_LITERAL
    |   '(' expression ')';

unary_expression
    locals [String expressionType]
    :   (PLUS | MINUS) primary_expression
    |   primary_expression;

multiplicative_expression
    locals [String expressionType]
    :   multiplicative_expression (MULTIPLY | DIVIDE | REM) unary_expression
    |   unary_expression;

additive_expression
    locals [String expressionType]
    :   additive_expression (PLUS | MINUS)  multiplicative_expression
    |   multiplicative_expression;

// This does not deal with Ada's rules about parenthesizing potentially ambiguous expressions.
relational_expression
    locals [String expressionType]
    :   relational_expression
          (EQUAL | NOT_EQUAL | LESS | LESS_EQUAL | GREATER | GREATER_EQUAL)
            additive_expression
    |   additive_expression;

expression
    locals [String expressionType]
    :   relational_expression;

// Augusta currently only supports simple left-hand side expressions.
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

// Note that Ada allows underscores to appear in the exponent.
INTEGER_LITERAL
    :   (DECIMAL | BASED) ( ('E' | 'e') '+'? DIGIT+ )?;

// Note that Ada allows underscores to appear in the exponent.
REAL_LITERAL
    :   (DECIMAL_REAL | BASED_REAL) ( ('E' | 'e') ('+' | '-')? DIGIT+ )?;

fragment DECIMAL
    :   DIGIT ('_'? DIGIT)*;

fragment DECIMAL_REAL
    :   DIGIT ('_'? DIGIT)* '.' DIGIT ('_'? DIGIT)*;

// Note that Ada allows underscores to appear in the base.
fragment BASED
    :   DIGIT+ '#' HEX_DIGIT ('_'? HEX_DIGIT)* '#';

// Note that Ada allows underscores to appear in the base.
fragment BASED_REAL
    :   DIGIT+ '#' HEX_DIGIT ('_'? HEX_DIGIT)* '.' HEX_DIGIT ('_'? HEX_DIGIT)* '#';

fragment DIGIT
    :   [0-9];

fragment HEX_DIGIT
    :   [0-9a-fA-F];
