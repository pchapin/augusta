grammar Ada;

@parser::header {
    package edu.vtc.augusta;
}

@lexer::header {
    package edu.vtc.augusta;
}

@members {

// The following material was from an earlier ANTLR3 grammar. It may not be applicable with
// ANTLR4. Aborting after the first syntax error is not acceptable in a production parser
// anyway. However, I'm keeping this material here for now in case it proves to be a useful
// reference.

//  // The following two magic methods, together with the @rulecatch section below cause the
//  // parser to exit immediately with an exception when an error is encountered.
//  //
//  protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
//      throws RecognitionException
//  {
//    throw new MismatchedTokenException(ttype, input);
//  }
//
//  public Object recoverFromMismatchedSet(
//      IntStream input, RecognitionException e, BitSet follow) throws RecognitionException
//  {
//    throw e;
//  }
}

//@parser::rulecatch {
//  catch (RecognitionException e) {
//    throw e;
//  }
//}

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
    :   declaration+;

declaration
    :   object_declaration
    |   type_declaration
    |   subtype_declaration
    |   subprogram_declaration;

object_declaration
    :   IDENTIFIER ':' IDENTIFIER (':=' expression)? ';';

type_declaration
    :   TYPE IDENTIFIER IS type_definition ';';

type_definition
    :   integer_type_definition
    |   array_type_definition;

integer_type_definition
    locals [int lowerBound, int upperBound]
    :   RANGE NUMERIC_LITERAL DOTDOT NUMERIC_LITERAL;

array_type_definition
    locals [String indexTypeName, String elementTypeName]
    :   ARRAY '(' IDENTIFIER ')' OF IDENTIFIER;

subtype_declaration
    :   SUBTYPE IDENTIFIER IS IDENTIFIER RANGE NUMERIC_LITERAL DOTDOT NUMERIC_LITERAL ';';

subprogram_declaration
    :   subprogram_specification IS declarations block ';';

subprogram_specification
    :   procedure_specification
    |   function_specification;

procedure_specification
    :   PROCEDURE IDENTIFIER parameter_profile?;

function_specification
    :   FUNCTION IDENTIFIER parameter_profile? RETURN IDENTIFIER;

parameter_profile
    :   '(' parameter_specification (';' parameter_specification)* ')';

parameter_specification
    :   IDENTIFIER ':' IDENTIFIER;


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
    :   left_expression ':=' expression ';';

conditional_statement
    :   IF expression THEN
           statement+
       (ELSIF expression THEN
           statement+)*
       (ELSE
           statement+)?
        END IF ';';

iteration_statement
    :   WHILE expression LOOP
           statement+
        END LOOP ';';

null_statement
    :   NULL ';';


// Expression Grammar
// ------------------

array_access_expression
    locals [String expressionType, int value = 0]
    :   IDENTIFIER '(' expression ')';

primary_expression
    locals [String expressionType, int value = 0]
    :   IDENTIFIER
    |   NUMERIC_LITERAL
    |   array_access_expression
    |   '(' expression ')';

multiplicative_expression
    locals [String expressionType, int value = 0]
    :   multiplicative_expression MULTIPLY primary_expression
    |   multiplicative_expression DIVIDE   primary_expression
    |   primary_expression;

unary_expression
    locals [String expressionType, int value = 0]
    :   (PLUS | MINUS) multiplicative_expression
    |   multiplicative_expression;

additive_expression
    locals [String expressionType, int value = 0]
    :   additive_expression PLUS  unary_expression
    |   additive_expression MINUS unary_expression
    |   unary_expression;

relational_expression
    locals [String expressionType, int value = 0]
    :   relational_expression (EQUAL   | NOT_EQUAL  |
                               LESS    | LESS_EQUAL |
                               GREATER | GREATER_EQUAL) additive_expression
    |   additive_expression;

expression
    locals [String expressionType, int value = 0]
    :   relational_expression;

left_expression
    locals [String expressionType]
    :   array_access_expression
    |   IDENTIFIER;


/* =========== */
/* Lexer rules */
/* =========== */

// --------------
// Reserved Words
// --------------

// Rabbit2 reserves all the words of Ada to prevent programmers from accidentally creating
// identifiers like these. Although Rabbit is case sensitive normally, reserved words must be
// case insensitive for compatibility with Ada. For example, we don't want an Rabbit programmer
// creating a variable named 'Access' or similar.

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
