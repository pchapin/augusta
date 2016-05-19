grammar AdaFull;

// This is pretty much a copy of the grammar in the Ada Reference Manual, written with an
// ANTLR syntax. I retain it as a reference. This grammar is quite ambiguous as written, but
// many parts of it are probably usable for Augusta.

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

// Section 3
// ---------

basic_declaration
    :    type_declaration
    |    subtype_declaration
    |    object_declaration
    |    number_declaration
    |    subprogram_declaration
    |    abstract_subprogram_declaration
    |    null_procedure_declaration
    |    expression_function_declaration
    |    package_declaration
    |    renaming_declaration
    |    exception_declaration
    |    generic_declaration
    |    generic_instantiation;

defining_identifier
    :    IDENTIFIER;

type_declaration
    :    full_type_declaration
    |    incomplete_type_declaration
    |    private_type_declaration
    |    private_extension_declaration;

full_type_declaration
    :    TYPE defining_identifier known_discriminant_part? IS
             type_definition aspect_specification? ';'
    |    task_type_declaration
    |    protected_type_declaration;

type_definition
    :    enumeration_type_definition
    |    integer_type_definition
    |    real_type_definition
    |    array_type_definition
    |    record_type_definition
    |    access_type_definition
    |    derived_type_definition
    |    interface_type_definition;

subtype_declaration
    :    SUBTYPE defining_identifier IS subtype_indication aspect_specification? ';';

subtype_indication
    :    null_exclusion? subtype_mark constraint?;

// The reference manual shows subtype_mark expanding to name. However, I find it hard to believe
// that it really needs the full generality of a name. Does it really?
//
subtype_mark
    :    IDENTIFIER /* name */;

constraint
    :    scalar_constraint | composite_constraint;

scalar_constraint
    :    range_constraint | digits_constraint | delta_constraint;

composite_constraint
    :    index_constraint | discriminant_constraint;

object_declaration
    :    defining_identifier_list ':' ALIASED? CONSTANT? subtype_indication
             (':=' expression)? aspect_specification? ';'
    |    defining_identifier_list ':' ALIASED? CONSTANT? access_definition
             (':=' expression)? aspect_specification? ';'
    |    defining_identifier_list ':' ALIASED? CONSTANT? array_type_definition
             (':=' expression)? aspect_specification? ';'
    |    single_task_declaration
    |    single_protected_declaration;

defining_identifier_list
    :    defining_identifier (',' defining_identifier)*;

number_declaration
    :    defining_identifier_list ':' CONSTANT ':=' expression ';';

derived_type_definition
    :    ABSTRACT? LIMITED? NEW subtype_indication ((AND interface_list)? record_extension_part)?;

range_constraint
    :    RANGE range;

range
    :    range_attribute_reference
    |    simple_expression '..' simple_expression;

enumeration_type_definition
    :    '(' enumeration_literal_specification (',' enumeration_literal_specification)* ')';

enumeration_literal_specification
    :    defining_identifier | defining_character_literal;

defining_character_literal
    :    CHARACTER_LITERAL;

integer_type_definition
    :    signed_integer_type_definition
    |    modular_type_definition;

signed_integer_type_definition
    :    RANGE simple_expression '..' simple_expression;

modular_type_definition
    :    MOD expression;

real_type_definition
    :    floating_point_definition
    |    fixed_point_definition;

floating_point_definition
    :    DIGITS expression real_range_specification?;

real_range_specification
    :    RANGE simple_expression '..' simple_expression;

fixed_point_definition
    :    ordinary_fixed_point_definition
    |    decimal_fixed_point_definition;

ordinary_fixed_point_definition
    :    DELTA expression real_range_specification;

decimal_fixed_point_definition
    :    DELTA expression DIGITS expression real_range_specification?;

digits_constraint
    :    DIGITS expression range_constraint?;

array_type_definition
    :    unconstrained_array_definition
    |    constrained_array_definition;

unconstrained_array_definition
    :    ARRAY '(' index_subtype_definition (',' index_subtype_definition)* ')'
             OF component_definition;

index_subtype_definition
    :    subtype_mark RANGE '<>';

constrained_array_definition
    :    ARRAY '(' discrete_subtype_definition (',' discrete_subtype_definition)* ')'
             OF component_definition;

discrete_subtype_definition
    :    subtype_indication
    |    range;

component_definition
    :    ALIASED? subtype_indication
    |    ALIASED? access_definition;

index_constraint
    :    '(' discrete_range (',' discrete_range)* ')';

discrete_range
    :    subtype_indication
    |    range;

discriminant_part
    :    unknown_discriminant_part
    |    known_discriminant_part;

unknown_discriminant_part
    :    '(' '<>' ')';

known_discriminant_part
    :    '(' discriminant_specification (';' discriminant_specification)* ')';

discriminant_specification
    :    defining_identifier_list ':' null_exclusion? subtype_mark (':=' default_expression)?
    |    defining_identifier_list ':' access_definition (':=' default_expression)?;

default_expression
    :    expression;

discriminant_constraint
    :    '(' discriminant_association (',' discriminant_association)* ')';

discriminant_association
    :    (selector_name ('|' selector_name)* '=>')? expression;

record_type_definition
    :    (ABSTRACT? TAGGED)? LIMITED? record_definition;

record_definition
    :    RECORD component_list END RECORD
    |    NULL RECORD;

component_list
    :    component_item+
    |    component_item* variant_part
    |    NULL;

component_item
    :    component_declaration
    |    aspect_clause;

component_declaration
    :    defining_identifier_list ':' component_definition
             (':=' default_expression)? aspect_specification? ';';

variant_part
    :    CASE direct_name IS variant+ END CASE ';';

variant
    :    WHEN discrete_choice_list '=>' component_list;

discrete_choice_list
    :    discrete_choice ('|' discrete_choice)*;

discrete_choice
    :    choice_expression | subtype_indication | range | OTHERS;

record_extension_part
    :    WITH record_definition;

abstract_subprogram_declaration
    :    overriding_indicator? subprogram_specification IS ABSTRACT aspect_specification? ';';

interface_type_definition
    :    (LIMITED | TASK | PROTECTED | SYNCHRONIZED)? INTERFACE (AND interface_list)?;

interface_list
    :    subtype_mark (AND subtype_mark)*;

access_type_definition
    :    null_exclusion? access_to_object_definition
    |    null_exclusion? access_to_subprogram_definition;

access_to_object_definition
    :    ACCESS general_access_modifier? subtype_indication;

general_access_modifier
    :    ALL | CONSTANT;

access_to_subprogram_definition
    :    ACCESS PROTECTED? PROCEDURE parameter_profile
    |    ACCESS PROTECTED? FUNCTION parameter_and_result_profile;

null_exclusion
    :    NOT NULL;

access_definition
    :    null_exclusion? ACCESS CONSTANT? subtype_mark
    |    null_exclusion? ACCESS PROTECTED? PROCEDURE parameter_profile
    |    null_exclusion? ACCESS PROTECTED? FUNCTION parameter_and_result_profile;

incomplete_type_declaration
    :    TYPE defining_identifier discriminant_part? (IS TAGGED)? ';' ;

declarative_part
    :    declarative_item*;

declarative_item
    :    basic_declarative_item | body;

basic_declarative_item
    :    basic_declaration
    |    aspect_clause
    |    use_clause;

body
    :    proper_body | body_stub;

proper_body
    :    subprogram_body
    |    package_body
    |    task_body
    |    protected_body;

// Section 4
// ---------

// In the reference manual 'name' is involved in several mutually left-recursive rules. In this
// grammar those rules are all collapsed into alternatives under 'name' making the left
// recursion direct. ANTLR v4 has automatic left factoring that can handle direct left
// recursion. Thus this transformation makes the grammar acceptable to ANTLR v4.
//
name
    :    direct_name
    |    name '.' ALL                               // explicit_dereference
    |    name '(' expression (',' expression)* ')'  // indexed_component
    |    name '(' discrete_range ')'                // slice
    |    name '.' selector_name                     // selected_component
    |    name TICK attribute_designator             // attribute_reference
    |    type_conversion // TODO: Ambiguous! Lexer needs to return type names as special tokens.
    |    CHARACTER_LITERAL
    |    qualified_expression
    |    name actual_parameter_part;                // generalized_indexing or function_call

direct_name
    :    IDENTIFIER | operator_symbol;

prefix
    :    name /* | implicit_dereference */;

// The reference manual breaks out implicit_dereference as a special kind of prefix. However
// since it expands to just 'name' there doesn't seem to be any point in representing it
// separately in the parser.
//
// implicit_dereference
//    :    name;

selector_name
    :    IDENTIFIER | CHARACTER_LITERAL | operator_symbol;

// Attribute designators need to handle ACCESS, DELTA, DIGITS, MOD, and RANGE in a special way.
attribute_designator
    :    IDENTIFIER ('(' expression ')')?
    |    ACCESS
    |    DELTA
    |    DIGITS
    |    MOD;

// It looks like RANGE is being handled in a special way here.
range_attribute_reference
    :    prefix TICK range_attribute_designator;

range_attribute_designator
    :    RANGE ('(' expression ')')?;

// The reference manual breaks out generalized_reference as a special kind of name. However
// since it expands to just 'name' there doesn't seem to be any point in representing it
// separately in the parser.
//
// generalized_reference
//     :    name;

aggregate
    :    record_aggregate
    |    extension_aggregate
    |    array_aggregate;

record_aggregate
    :    '(' record_component_association_list ')';

record_component_association_list
    :    record_component_association (',' record_component_association)*
    |    NULL RECORD;

record_component_association
    :    (component_choice_list '=>')? expression
    |    component_choice_list '=>' '<>';

component_choice_list
    :    selector_name ('|' selector_name)*
    |    OTHERS;

extension_aggregate
    :    '(' ancestor_part WITH record_component_association_list ')';

ancestor_part
    :    expression
    |    subtype_mark;

array_aggregate
    :    positional_array_aggregate
    |    named_array_aggregate;

positional_array_aggregate
    :    '(' expression ',' expression (',' expression)* ')'
    |    '(' expression (',' expression)* ',' OTHERS '=>' expression ')'
    |    '(' expression (',' expression)* ',' OTHERS '=>' '<>' ')';

named_array_aggregate
    :    '(' array_component_association (',' array_component_association)* ')';

array_component_association
    :    discrete_choice_list '=>' expression
    |    discrete_choice_list '=>' '<>';

expression
    :    relation (AND relation)*
    |    relation (AND THEN relation)*
    |    relation (OR relation)*
    |    relation (OR ELSE relation)*
    |    relation (XOR relation)*;

choice_expression
    :    choice_relation (AND choice_relation)*
    |    choice_relation (OR choice_relation)*
    |    choice_relation (XOR choice_relation)*
    |    choice_relation (AND THEN choice_relation)*
    |    choice_relation (OR ELSE choice_relation)*;

choice_relation
    :    simple_expression (relational_operator simple_expression)?;

relation
    :    simple_expression (relational_operator simple_expression)?
    |    simple_expression NOT? IN membership_choice_list;

membership_choice_list
    :    membership_choice ('|' membership_choice)*;

membership_choice
    :    choice_expression
    |    range
    |    subtype_mark;

simple_expression
    :    unary_adding_operator? term (binary_adding_operator term)*;

term
    :    factor (multiplying_operator factor)*;

factor
    :    primary ('**' primary)?
    |    ABS primary
    |    NOT primary;

primary
    :    NUMERIC_LITERAL
    |    NULL
    |    STRING_LITERAL
    |    aggregate
    |    name
    |    allocator
    |    '(' expression ')'
    |    '(' conditional_expression ')'
    |    '(' quantified_expression ')';

logical_operator
    :    AND | OR | XOR;

relational_operator
    :    '=' | '/=' | '<' | '<=' | '>' | '>=';

binary_adding_operator
    :    '+' | '-' | '&';

unary_adding_operator
    :    '+' | '-';

multiplying_operator
    :    '*' | '/' | MOD | REM;

highest_precedence_operator
    :    '**' | ABS | NOT;

conditional_expression
    :    if_expression
    |    case_expression;

if_expression
    :    IF condition THEN expression
           (ELSIF condition THEN expression)*
           (ELSE expression)?;

condition
    :    expression;

case_expression
    :    CASE expression IS case_expression_alternative (',' case_expression_alternative)*;

case_expression_alternative
    :    WHEN discrete_choice_list '=>' expression;

quantified_expression
    :    FOR quantifier loop_parameter_specification '=>' predicate
    |    FOR quantifier iterator_specification '=>' predicate;

quantifier
    :    ALL | SOME;

predicate
    :    expression;

type_conversion
    :    subtype_mark '(' expression ')'
    |    subtype_mark '(' name ')';

qualified_expression
    :    subtype_mark TICK '(' expression ')'
    |    subtype_mark TICK aggregate;

allocator
    :    NEW subpool_specification? subtype_indication
    |    NEW subpool_specification? qualified_expression;

subpool_specification
    :    name;

// Section 5
// ---------

sequence_of_statements
    :    statement+ label*;

statement
    :    label* simple_statement
    |    label* compound_statement;

simple_statement
    :    null_statement
    |    assignment_statement
    |    exit_statement
    |    goto_statement
    |    procedure_call_statement
    |    simple_return_statement
    |    entry_call_statement
    |    requeue_statement
    |    delay_statement
    |    abort_statement
    |    raise_statement
    |    code_statement;

compound_statement
    :    if_statement
    |    case_statement
    |    loop_statement
    |    block_statement
    |    extended_return_statement
    |    accept_statement
    |    select_statement;

null_statement
    :    NULL ';';

label
    :    '<<' statement_identifier '>>';

statement_identifier
    :    direct_name;

assignment_statement
    :    name ':=' expression ';';

if_statement
    :    IF condition THEN sequence_of_statements
           (ELSIF condition THEN sequence_of_statements)*
           (ELSE sequence_of_statements)?
            END IF ';';

case_statement
    :    CASE expression IS
           case_statement_alternative+
           END CASE ';';

case_statement_alternative
    :    WHEN discrete_choice_list '=>' sequence_of_statements;

loop_statement
    :    (statement_identifier ':')?
         (iteration_scheme)? LOOP
           sequence_of_statements
         END LOOP IDENTIFIER? ';';

iteration_scheme
    :    WHILE condition
    |    FOR loop_parameter_specification
    |    FOR iterator_specification;

loop_parameter_specification
    :    defining_identifier IN REVERSE? discrete_subtype_definition;

iterator_specification
    :    defining_identifier IN REVERSE? name
    |    defining_identifier (':' subtype_indication)? OF REVERSE? name;

block_statement
    :    (statement_identifier ':')?
         (DECLARE declarative_part)?
         BEGIN
           handled_sequence_of_statements
         END IDENTIFIER? ';';

exit_statement
    :    EXIT name? (WHEN condition)? ';';

goto_statement
    :    GOTO name ';';

// Section 6
// ---------

subprogram_declaration
    :    overriding_indicator? subprogram_specification aspect_specification? ';';

subprogram_specification
    :    procedure_specification
    |    function_specification;

procedure_specification
    :    PROCEDURE defining_program_unit_name parameter_profile;

function_specification
    :    FUNCTION defining_designator parameter_and_result_profile;

designator
    :    (parent_unit_name '.')? IDENTIFIER
    |    operator_symbol;

defining_designator
    :    defining_program_unit_name
    |    defining_operator_symbol;

defining_program_unit_name
    :    (parent_unit_name '.')? defining_identifier;

operator_symbol
    :    STRING_LITERAL;

defining_operator_symbol
    :    operator_symbol;

parameter_profile
    :    formal_part?;

parameter_and_result_profile
    :    formal_part? RETURN null_exclusion? subtype_mark
    |    formal_part? RETURN access_definition;

formal_part
    :    '(' parameter_specification (';' parameter_specification)* ')';

parameter_specification
    :    defining_identifier_list ':' ALIASED? parameter_mode null_exclusion? subtype_mark
             (':=' default_expression)?
    |    defining_identifier_list ':' access_definition (':=' default_expression)?;

parameter_mode
    :    IN? | IN OUT | OUT;

subprogram_body
    :    overriding_indicator? subprogram_specification aspect_specification? IS
           declarative_part
         BEGIN
           handled_sequence_of_statements
         END designator? ';';

procedure_call_statement
    :    name ';'
    |    prefix actual_parameter_part ';';

actual_parameter_part
    :    '(' parameter_association (',' parameter_association)* ')';

parameter_association
    :    (selector_name '=>')? explicit_actual_parameter;

explicit_actual_parameter
    :    expression
    |    name;

simple_return_statement
    :    RETURN expression? ';';

extended_return_statement
    :    RETURN defining_identifier ':' CONSTANT? return_subtype_indication
             (':=' expression)? (DO handled_sequence_of_statements END RETURN)? ';';

return_subtype_indication
    :    subtype_indication
    |    access_definition;

null_procedure_declaration
    :    overriding_indicator? procedure_specification IS NULL aspect_specification? ';';

expression_function_declaration
    :    overriding_indicator? function_specification IS
             '(' expression ')' aspect_specification? ';';


// Section 7
// ---------

package_declaration
    :    package_specification ';';

package_specification
    :    PACKAGE defining_program_unit_name aspect_specification? IS
           (basic_declarative_item)*
        (PRIVATE
           (basic_declarative_item)*)?
         END ((parent_unit_name '.')? IDENTIFIER)?;

package_body
    :    PACKAGE BODY defining_program_unit_name IS
           declarative_part
        (BEGIN
           handled_sequence_of_statements)?
         END ((parent_unit_name '.')? IDENTIFIER)? ';';

private_type_declaration
    :    TYPE defining_identifier discriminant_part? IS
             (ABSTRACT? TAGGED)? LIMITED? PRIVATE aspect_specification? ';';

private_extension_declaration
    :    TYPE defining_identifier discriminant_part? IS
           ABSTRACT? (LIMITED | SYNCHRONIZED)? NEW subtype_indication
           (AND interface_list)? WITH PRIVATE aspect_specification? ';';

// Section 8
// ---------

overriding_indicator
    :    NOT? OVERRIDING;

use_clause
    :    use_package_clause
    |    use_type_clause;

use_package_clause
    :    USE name (',' name)*;

use_type_clause
    :    USE ALL? TYPE subtype_mark (',' subtype_mark)*;

renaming_declaration
    :    object_renaming_declaration
    |    exception_renaming_declaration
    |    package_renaming_declaration
    |    subprogram_renaming_declaration
    |    generic_renaming_declaration;

object_renaming_declaration
    :    defining_identifier ':' null_exclusion? subtype_mark
             RENAMES name aspect_specification? ';'
    |    defining_identifier ':' access_definition RENAMES name aspect_specification? ';';

exception_renaming_declaration
    :    defining_identifier ':' EXCEPTION RENAMES name aspect_specification? ';';

package_renaming_declaration
    :    PACKAGE defining_program_unit_name RENAMES name aspect_specification? ';';

subprogram_renaming_declaration
    :    overriding_indicator? subprogram_specification RENAMES name aspect_specification? ';';

generic_renaming_declaration
    :    GENERIC PACKAGE defining_program_unit_name RENAMES name aspect_specification? ';'
    |    GENERIC PROCEDURE defining_program_unit_name RENAMES name aspect_specification? ';'
    |    GENERIC FUNCTION defining_program_unit_name RENAMES name aspect_specification? ';';

// Section 9
// ---------

task_type_declaration
    :    TASK TYPE defining_identifier known_discriminant_part? aspect_specification? (IS
           (NEW interface_list WITH)?
            task_definition)? ';';

single_task_declaration
    :    TASK defining_identifier aspect_specification? (IS
           (NEW interface_list WITH)?
            task_definition)? ';';

task_definition
    :    task_item*
        (PRIVATE
           task_item*)?
         END IDENTIFIER?;

task_item
    :    entry_declaration
    |    aspect_clause;

task_body
    :    TASK BODY defining_identifier IS
           declarative_part
         BEGIN
           handled_sequence_of_statements
         END IDENTIFIER? ';';

protected_type_declaration
    :    PROTECTED TYPE defining_identifier known_discriminant_part? aspect_specification? IS
           (NEW interface_list WITH)?
            protected_definition ';';

single_protected_declaration
    :    PROTECTED defining_identifier aspect_specification? IS
           (NEW interface_list WITH)?
            protected_definition ';';

protected_definition
    :    protected_operation_declaration*
        (PRIVATE
           protected_element_declaration*)?
         END IDENTIFIER?;

protected_operation_declaration
    :    subprogram_declaration
    |    entry_declaration
    |    aspect_clause;

protected_element_declaration
    :    protected_operation_declaration
    |    component_declaration;

protected_body
    :    PROTECTED BODY defining_identifier IS
           protected_operation_item*
         END IDENTIFIER? ';';

protected_operation_item
    :    subprogram_declaration
    |    subprogram_body
    |    entry_body
    |    aspect_clause;

// The reference manual speaks of By_Entry, By_Protected_Procedure and Optional. I believe
// these are not reserved words but should be treated syntactically as identifiers. Their
// validity needs to be checked during semantic analysis.
//
synchronization_kind
    :    IDENTIFIER;

entry_declaration
    :    overriding_indicator?
         ENTRY defining_identifier ('(' discrete_subtype_definition ')')?
             parameter_profile aspect_specification? ';';

accept_statement
    :    ACCEPT direct_name ('(' entry_index ')')? parameter_profile (DO
           handled_sequence_of_statements
         END IDENTIFIER?)? ';';

entry_index:    expression;

entry_body
    :    ENTRY defining_identifier entry_body_formal_part entry_barrier IS
           declarative_part
         BEGIN
           handled_sequence_of_statements
         END IDENTIFIER? ';';

entry_body_formal_part
    :    ('(' entry_index_specification ')')? parameter_profile;

entry_barrier
    :    WHEN condition;

entry_index_specification
    :    FOR defining_identifier IN discrete_subtype_definition;

entry_call_statement
    :    name actual_parameter_part? ';';

requeue_statement
    :    REQUEUE name (WITH ABORT)? ';';

delay_statement
    :    delay_until_statement
    |    delay_relative_statement;

delay_until_statement
    :    DELAY UNTIL expression ';';

delay_relative_statement
    :    DELAY expression ';';

select_statement
    :    selective_accept
    |    timed_entry_call
    |    conditional_entry_call
    |    asynchronous_select;

selective_accept
    :    SELECT guard?
           select_alternative
        (OR guard?
           select_alternative)*
        (ELSE
           sequence_of_statements)?
         END SELECT ';';

guard
    :    WHEN condition '=>';

select_alternative
    :    accept_alternative
    |    delay_alternative
    |    terminate_alternative;

accept_alternative
    :    accept_statement sequence_of_statements?;

delay_alternative
    :    delay_statement sequence_of_statements?;

terminate_alternative
    :    TERMINATE ';';

timed_entry_call
    :    SELECT
           entry_call_alternative
         OR
           delay_alternative
         END SELECT ';';

entry_call_alternative
    :    procedure_or_entry_call sequence_of_statements?;

procedure_or_entry_call
    :    procedure_call_statement
    |    entry_call_statement;

conditional_entry_call
    :    SELECT
           entry_call_alternative
         ELSE
           sequence_of_statements
         END SELECT ';';

asynchronous_select
    :    SELECT
           triggering_alternative
         THEN ABORT
           abortable_part
         END SELECT ';';

triggering_alternative
    :    triggering_statement sequence_of_statements?;

triggering_statement
    :    procedure_or_entry_call
    |    delay_statement;

abortable_part
    :    sequence_of_statements;

abort_statement
    :    ABORT name (',' name)* ';';

// Section 10
// ----------

compilation
    :    compilation_unit*;

compilation_unit
    :    context_clause library_item
    |    context_clause subunit;

library_item
    :    PRIVATE? library_unit_declaration
    |    library_unit_body
    |    PRIVATE? library_unit_renaming_declaration;

library_unit_declaration
    :    subprogram_declaration
    |    package_declaration
    |    generic_declaration
    |    generic_instantiation;

library_unit_renaming_declaration
    :    package_renaming_declaration
    |    generic_renaming_declaration
    |    subprogram_renaming_declaration;

library_unit_body
    :    subprogram_body
    |    package_body;

parent_unit_name
    :    IDENTIFIER;

context_clause
    :    context_item*;

context_item
    :    with_clause
    |    use_clause;

with_clause
    :    limited_with_clause
    |    nonlimited_with_clause;

// The reference manual uses name instead of IDENTIFIER in the following two productions. I it
// hard to believe the full generality of name is needed here. Is it really?
//
limited_with_clause
    :    LIMITED PRIVATE? WITH IDENTIFIER (',' IDENTIFIER)* ';';

nonlimited_with_clause
    :    PRIVATE? WITH IDENTIFIER (',' IDENTIFIER)* ';';

body_stub
    :    subprogram_body_stub
    |    package_body_stub
    |    task_body_stub
    |    protected_body_stub;

subprogram_body_stub
    :    overriding_indicator? subprogram_specification IS SEPARATE ';';

package_body_stub
    :    PACKAGE BODY defining_identifier IS SEPARATE ';';

task_body_stub
    :    TASK BODY defining_identifier IS SEPARATE ';';

protected_body_stub
    :    PROTECTED BODY defining_identifier IS SEPARATE ';';

subunit
    :    SEPARATE '(' parent_unit_name ')' proper_body;

// Section 11
// ----------

exception_declaration
    :    defining_identifier_list ';' EXCEPTION aspect_specification? ';';

handled_sequence_of_statements
    :    sequence_of_statements
        (EXCEPTION
           exception_handler+)?;

exception_handler
    :    WHEN (choice_parameter_specification ':')? exception_choice ('|' exception_choice)* '=>'
	       sequence_of_statements;

choice_parameter_specification
    :    defining_identifier;

exception_choice
    :    name
    |    OTHERS;

// Should the use of name below really be IDENTIFIER?
raise_statement
    :    RAISE ';'
    |    RAISE name (WITH expression)? ';';

// Section 12
// ----------

generic_declaration
    :    generic_subprogram_declaration
    |    generic_package_declaration;

generic_subprogram_declaration
    :    generic_formal_part subprogram_specification aspect_specification? ';';

generic_package_declaration
    :    generic_formal_part package_specification ';';

generic_formal_part
    :    GENERIC (generic_formal_parameter_declaration | use_clause)*;

generic_formal_parameter_declaration
    :    formal_object_declaration
    |    formal_type_declaration
    |    formal_subprogram_declaration
    |    formal_package_declaration;

generic_instantiation
    :    PACKAGE defining_program_unit_name IS
           NEW name generic_actual_part? aspect_specification? ';'
    |    overriding_indicator?
         PROCEDURE defining_program_unit_name IS
           NEW name generic_actual_part? aspect_specification? ';'
    |    overriding_indicator?
         FUNCTION defining_designator IS
           NEW name generic_actual_part? aspect_specification? ';';

generic_actual_part
    :    generic_association (',' generic_association)*;

generic_association
    :    (selector_name '=>')? explicit_generic_actual_parameter;

explicit_generic_actual_parameter
    :    expression
    |    name
    |    subtype_mark;

formal_object_declaration
    :    defining_identifier_list ':' parameter_mode null_exclusion? subtype_mark
             (':=' default_expression)? aspect_specification? ';'
    |    defining_identifier_list ':' parameter_mode access_definition
             (':=' default_expression)? aspect_specification? ';';

formal_type_declaration
    :    formal_complete_type_declaration
    |    formal_incomplete_type_declaration;

formal_complete_type_declaration
    :    TYPE defining_identifier (discriminant_part)? IS
             formal_type_definition aspect_specification? ';';

formal_incomplete_type_declaration
    :    TYPE defining_identifier (discriminant_part)? (IS TAGGED)? ';';

formal_type_definition
    :    formal_private_type_definition
    |    formal_derived_type_definition
    |    formal_discrete_type_definition
    |    formal_signed_integer_type_definition
    |    formal_modular_type_definition
    |    formal_floating_point_definition
    |    formal_ordinary_fixed_point_definition
    |    formal_decimal_fixed_point_definition
    |    formal_array_type_definition
    |    formal_access_type_definition
    |    formal_interface_type_definition;

formal_private_type_definition
    :    (ABSTRACT? TAGGED)? LIMITED? PRIVATE;

formal_derived_type_definition
    :    ABSTRACT? (LIMITED | SYNCHRONIZED)? NEW subtype_mark? ((AND interface_list)?
             WITH PRIVATE)?;

formal_discrete_type_definition
    :    '(' '<>' ')';

formal_signed_integer_type_definition
    :    RANGE '<>';

formal_modular_type_definition
    :    MOD '<>';

formal_floating_point_definition
    :    DIGITS '<>';

formal_ordinary_fixed_point_definition
    :    DELTA '<>';

formal_decimal_fixed_point_definition
    :    DELTA '<>' DIGITS '<>';

formal_array_type_definition
    :    array_type_definition;

formal_access_type_definition
    :    access_type_definition;

formal_interface_type_definition
    :    interface_type_definition;

formal_subprogram_declaration
    :    formal_concrete_subprogram_declaration
    |    formal_abstract_subprogram_declaration;

formal_concrete_subprogram_declaration
    :    WITH subprogram_specification (IS subprogram_default)? aspect_specification? ';';

formal_abstract_subprogram_declaration
    :    WITH subprogram_specification IS
             ABSTRACT subprogram_default? aspect_specification? ';';

subprogram_default
    :    default_name
    |    '<>'
    |    NULL;

default_name
    :    name;

formal_package_declaration
    :    WITH PACKAGE defining_identifier IS
             NEW name formal_package_actual_part aspect_specification? ';';

formal_package_actual_part
    :    '(' (OTHERS '=>')? '<>' ')'
    |    generic_actual_part?
    |    '(' formal_package_association (',' formal_package_association)* (',' OTHERS '=>' '<>')? ')';

formal_package_association
    :    generic_association
    |    selector_name '=>' '<>';

// Section 13
// ----------

aspect_clause
    :    attribute_definition_clause
    |    enumeration_representation_clause
    |    record_representation_clause
    |    at_clause;

local_name
    :    direct_name
    |    direct_name TICK attribute_designator
    |    name;

attribute_definition_clause
    :    FOR local_name TICK attribute_designator USE expression ';'
    |    FOR local_name TICK attribute_designator USE name ';';

aspect_specification
    :    WITH aspect_mark ('=>' aspect_definition)? (',' aspect_mark ('=>' aspect_definition)?)*;

// The IDENTIFIER after the TICK must be "Class"
aspect_mark
    :    IDENTIFIER (TICK IDENTIFIER)?;

aspect_definition
    :    name
    |    expression
    |    IDENTIFIER;

enumeration_representation_clause
    :    FOR local_name USE enumeration_aggregate ';';

enumeration_aggregate
    :    array_aggregate;

record_representation_clause
    :    FOR local_name USE
         RECORD mod_clause? component_clause* END RECORD ';';

component_clause
    :    local_name AT position RANGE first_bit '..' last_bit ';';

position
    :    expression;

first_bit
    :    simple_expression;

last_bit
    :    simple_expression;

code_statement
    :    qualified_expression ';';

storage_pool_indicator
    :    name | NULL;

restriction
    :    IDENTIFIER
    |    IDENTIFIER '=>' restriction_parameter_argument;

restriction_parameter_argument
    :    name
    |    expression;

// Section J
// ---------

delta_constraint
    :    DELTA expression range_constraint?;

at_clause
    :    FOR direct_name USE AT expression ';';

mod_clause
    :    AT MOD expression ';';

/* =========== */
/* Lexer rules */
/* =========== */

// --------------
// Reserved Words
// --------------
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

IDENTIFIER
    :   [a-zA-Z][a-zA-Z0-9_]*;

WHITESPACE
    :   [ \t\f\r\n]+  -> skip;

COMMENT
    :   '--' .*? [\r\n] -> skip;

// TODO: Complete this definition to include all printable characters.
CHARACTER_LITERAL
    :    '\'' [a-zA-Z0-9] '\'';

STRING_LITERAL
	:	'"' .*? '"';

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

// There is a lexical ambiguity between the TICK and the introduction of a character literal. To
// resolve this ambiguity one must keep track of the previous token. If the previous token is an
// INDENTIFIER then the '\'' character is a TICK, otherwise it is the start of a character
// literal.
//
// Currently CHARACTER_LITERAL only matches a single character between the quotation marks.
// There are no attributes with single character names (yes?) so everything works as is. ANTLR
// is unable to create a CHARACTER_LITERAL token for text such as X'Last - X'First because the
// material between the quotation marks is longer than one character. Attributes with single
// character names, if any existed, would cause problems: X'A'First. Currently this will cause a
// confusing error message about a misplaced character literal.
TICK
    :    '\'';
