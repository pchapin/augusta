/*!
    \file   Lexer.hpp
    \brief  A class that reads raw text and produces tokens.
    \author (C) Copyright 2010 by Peter C. Chapin <peter@openwatcom.org>
*/

#include <string>
#include "SourceFile.hpp"

namespace scanner {

    //! All possible tokens.
    enum token_type {

        // Tokens represented by reserved words.
        Y_ABORT,
        Y_ABS,
        Y_ABSTRACT,
        Y_ACCEPT,
        Y_ACCESS,
        Y_ALIASED,
        Y_ALL,
        Y_AND,
        Y_ARRAY,
        Y_AT,
        Y_BEGIN,
        Y_BODY,
        Y_CASE,
        Y_CONSTANT,
        Y_DECLARE,
        Y_DELAY,
        Y_DELTA,
        Y_DIGITS,
        Y_DO,
        Y_ELSE,
        Y_ELSIF,
        Y_END,
        Y_ENTRY,
        Y_EXCEPTION,
        Y_EXIT,
        Y_FOR,
        Y_FUNCTION,
        Y_GENERIC,
        Y_GOTO,
        Y_IF,
        Y_IN,
        Y_INTERFACE,
        Y_IS,
        Y_LIMITED,
        Y_LOOP,
        Y_MOD,
        Y_NEW,
        Y_NOT,
        Y_NULL,
        Y_OF,
        Y_OR,
        Y_OTHERS,
        Y_OUT,
        Y_OVERRIDING,
        Y_PACKAGE,
        Y_PRAGMA,
        Y_PRIVATE,
        Y_PROCEDURE,
        Y_PROTECTED,
        Y_RAISE,
        Y_RANGE,
        Y_RECORD,
        Y_REM,
        Y_RENAMES,
        Y_REQUEUE,
        Y_RETURN,
        Y_REVERSE,
        Y_SELECT,
        Y_SEPARATE,
        Y_SUBTYPE,
        Y_SYNCHRONIZED,
        Y_TAGGED,
        Y_TASK,
        Y_TERMINATE,
        Y_THEN,
        Y_TYPE,
        Y_UNTIL,
        Y_USE,
        Y_WHEN,
        Y_WHILE,
        Y_WITH,
        Y_XOR,

        // Tokens represented by operator symbols or other forms of punctuation.
        Y_COLON,
        Y_COLONEQUAL,
        Y_COMMA,
        Y_DOT,
        Y_LABELSTART,
        Y_LABELEND,
        Y_LPAREN,
        Y_RPAREN,
        Y_SEMICOLON,
        Y_TICK,

        // Tokens represented by user defined character sequences.
        Y_IDENTIFIER,
        Y_NUMBER,
        Y_STRING
    };

    //! Representation of a single token.
    struct Token {
        token_type  name;
        std::string text;  // Raw text of the token.

        // Coordinates in the file of the where the token begins.
        SourceFile::line_type   line_number;
        SourceFile::column_type column_number;
    };

    //! Class that tokenizes an input file.
    class Lexer {
    public:

        //! Initializes a Lexer with a previously constructed SourceFile object.
        explicit Lexer( SourceFile &source );
        
    private:
        SourceFile &source;    
    };
    
}

