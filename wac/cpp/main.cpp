/*!
    \file   main.cpp
    \brief  The main program of the Open Watcom Ada compiler.
    \author (C) Copyright 2010 by Peter C. Chapin <peter@openwatcom.org>
*/

#include <cwchar>
#include <exception>
#include <iostream>

#include "Lexer.hpp"
#include "SourceFile.hpp"

int main( )
{
    std::wint_t ch;
    
    try {
        scanner::SourceFile source( "check\\hello.adb" );
        scanner::Lexer lexical_analyzer( source );

        // For now read the source file.
        while( (ch = source.next_char( )) != WEOF ) {
            std::cout << static_cast< char >( ch );
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

