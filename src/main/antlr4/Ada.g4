grammar Ada;

/* ======= */
/* Grammar */
/* ======= */


// High Level Grammar
// ------------------

compilation_unit
    :   procedure_definition;

procedure_definition
    :   PROCEDURE IDENTIFIER IS declarations block ';';


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
    |   null_statement;

assignment_statement
    :   left_expression ':=' expression ';';

null_statement
    :   NULL ';';


// Expression Grammar
// ------------------

primary_expression
    :   IDENTIFIER
    |   NUMERIC_LITERAL
    |   '(' expression ')';

multiplicative_expression
    :   multiplicative_expression MULTIPLY primary_expression
    |   multiplicative_expression DIVIDE   primary_expression
    |   primary_expression;

unary_expression
    :   (PLUS | MINUS) multiplicative_expression
    |   multiplicative_expression;

additive_expression
    :   additive_expression PLUS  unary_expression
    |   additive_expression MINUS unary_expression
    |   unary_expression;

expression
    :   additive_expression;

left_expression
    :   IDENTIFIER;


/* =========== */
/* Lexer rules */
/* =========== */

// --------------
// Reserved Words
// --------------

// Augusta reserves all the words of Ada to prevent programmers from accidentally creating
// identifiers like these. This avoids forward incompatibilities.

ABORT        : [Aa][Bb][Oo][Rr][Tt];
ABS          : [Aa][Bb][Ss];
ABSTRACT     : [Aa][Bb][Ss][Tt][Rr][Aa][Cc][Tt];
ACCEPT       : [Aa][Cc][Cc][Ee][Pp][Tt];
ACCESS       : [Aa][Cc][Cc][Ee][Ss][Ss];
ALIASED      : [Aa][Ll][Ii][Aa][Ss][Ee][Dd];
ALL          : [Aa][Ll][Ll];
AND          : [Aa][Nn][Dd];
ARRAY        : [Aa][Rr][Rr][Aa][Yy];
AT           : [Aa][Tt];
BEGIN        : [Bb][Ee][Gg][Ii][Nn];
BODY         : [Bb][Oo][Dd][Yy];
CASE         : [Cc][Aa][Ss][Ee];
CONSTANT     : [Cc][Oo][Nn][Ss][Tt][Aa][Nn][Tt];
DECLARE      : [Dd][Ee][Cc][Ll][Aa][Rr][Ee];
DELAY        : [Dd][Ee][Ll][Aa][Yy];
DELTA        : [Dd][Ee][Ll][Tt][Aa];
DIGITS       : [Dd][Ii][Gg][Ii][Tt][Ss];
DO           : [Dd][Oo];
ELSE         : [Ee][Ll][Ss][Ee];
ELSIF        : [Ee][Ll][Ss][Ii][Ff];
END          : [Ee][Nn][Dd];
ENTRY        : [Ee][Nn][Tt][Rr][Yy];
EXCEPTION    : [Ee][Xx][Cc][Ee][Pp][Tt][Ii][Oo][Nn];
EXIT         : [Ee][Xx][Ii][Tt];
FOR          : [Ff][Oo][Rr];
FUNCTION     : [Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn];
GENERIC      : [Gg][Ee][Nn][Ee][Rr][Ii][Cc];
GOTO         : [Gg][Oo][Tt][Oo];
IF           : [Ii][Ff];
IN           : [Ii][Nn];
INTERFACE    : [Ii][Nn][Tt][Ee][Rr][Ff][Aa][Cc][Ee];
IS           : [Ii][Ss];
LIMITED      : [Ll][Ii][Mm][Ii][Tt][Ee][Dd];
LOOP         : [Ll][Oo][Oo][Pp];
MOD          : [Mm][Oo][Dd];
NEW          : [Nn][Ee][Ww];
NOT          : [Nn][Oo][Tt];
NULL         : [Nn][Uu][Ll][Ll];
OF           : [Oo][Ff];
OR           : [Oo][Rr];
OTHERS       : [Oo][Tt][Hh][Ee][Rr][Ss];
OUT          : [Oo][Uu][Tt];
OVERRIDING   : [Oo][Vv][Ee][Rr][Rr][Ii][Dd][Ii][Nn][Gg];
PACKAGE      : [Pp][Aa][Cc][Kk][Aa][Gg][Ee];
PRAGMA       : [Pp][Rr][Aa][Gg][Mm][Aa];
PRIVATE      : [Pp][Rr][Ii][Vv][Aa][Tt][Ee];
PROCEDURE    : [Pp][Rr][Oo][Cc][Ee][Dd][Uu][Rr][Ee];
PROTECTED    : [Pp][Rr][Oo][Tt][Ee][Cc][Tt][Ee][Dd];
RAISE        : [Rr][Aa][Ii][Ss][Ee];
RANGE        : [Rr][Aa][Nn][Gg][Ee];
RECORD       : [Rr][Ee][Cc][Oo][Rr][Dd];
REM          : [Rr][Ee][Mm];
RENAMES      : [Rr][Ee][Nn][Aa][Mm][Ee][Ss];
REQUEUE      : [Rr][Ee][Qq][Uu][Ee][Uu][Ee];
RETURN       : [Rr][Ee][Tt][Uu][Rr][Nn];
REVERSE      : [Rr][Ee][Vv][Ee][Rr][Ss][Ee];
SELECT       : [Ss][Ee][Ll][Ee][Cc][Tt];
SEPARATE     : [Ss][Ee][Pp][Aa][Rr][Aa][Tt][Ee];
SOME         : [Ss][Oo][Mm][Ee];
SUBTYPE      : [Ss][Uu][Bb][Tt][Yy][Pp][Ee];
SYNCHRONIZED : [Ss][Yy][Nn][Cc][Hh][Rr][Oo][Nn][Ii][Zz][Ee][Dd];
TAGGED       : [Tt][Aa][Gg][Gg][Ee][Dd];
TASK         : [Tt][Aa][Ss][Kk];
TERMINATE    : [Tt][Ee][Rr][Mm][Ii][Nn][Aa][Tt][Ee];
THEN         : [Tt][Hh][Ee][Nn];
TYPE         : [Tt][Yy][Pp][Ee];
UNTIL        : [Uu][Nn][Tt][Ii][Ll];
USE          : [Uu][Ss][Ee];
WHEN         : [Ww][Hh][Ee][Nn];
WHILE        : [Ww][Hh][Ii][Ll][Ee];
WITH         : [Ww][Ii][Tt][Hh];
XOR          : [Xx][Oo][Rr];

// Various operator symbols.
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

IDENTIFIER
    :   [a-zA-Z][a-zA-Z0-9_]*;

WHITESPACE
    :   [ \t\f\r\n]+  -> skip;

COMMENT
    :   '--' .*? [\r\n] -> skip;

NUMERIC_LITERAL
    :   (DECIMAL | BASED) ( ('E' | 'e') DIGIT+ )?;

fragment DECIMAL
    :   DIGIT ('_'? DIGIT)*;

fragment BASED
    :   DIGIT+ '#' HDIGIT ('_'? HDIGIT)* '#';

fragment DIGIT
    :   [0-9];

fragment HDIGIT
    :   [0-9a-fA-F];
