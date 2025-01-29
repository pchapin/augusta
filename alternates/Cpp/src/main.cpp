/*!
 *  \file   main.cpp
 *  \brief  The main program of the Agency Ada compiler.
 *  \author (C) Copyright 2024 by Peter Chapin <owpeter@pchapin.org>
 */

#include <cwchar>
#include <exception>
#include <iostream>

#include "ada.tab.hpp"   // Needed for yylex below.
#include "Lexer.hpp"
#include "SourceFile.hpp"

int main( )
{
    std::wint_t ch;
    
    try {
        scanner::SourceFile source( "check/hello.adb" );
        scanner::Lexer lexical_analyzer( source );

        // For now read the source file.
        while( (ch = source.next_char( )) != WEOF ) {
            std::cout << static_cast<char>( ch );
        }
    }
    catch( const std::exception &e ) {
        std::cout << "Caught exception in main: " << e.what( ) << std::endl;
    }
    catch( ... ) {
        std::cout << "Caught unknown exception in main" << std::endl;
    }
    return( 0 );
}


// This is a placeholder token generating file to make the Bison-generated parser happy. It does
// nothing right now, but later it can be moved into the Lexer class, etc.

yy::parser::symbol_type yylex( yy::parser::semantic_type * )
{
    // Return the Y_EOF token to say "we are already at the end of the file!"
    return yy::parser::symbol_type( );
}
