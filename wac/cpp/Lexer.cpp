/*!
    \file   Lexer.cpp
    \brief  The implementation of a class that reads raw text and produces tokens.
    \author (C) Copyright 2010 by Peter C. Chapin <peter@openwatcom.org>
*/

#include "Lexer.hpp"

namespace scanner {

    Lexer::Lexer( SourceFile &ada_source ) : source( ada_source ) { }
}
