/*!
 *  \file   Lexer.cpp
 *  \brief  The implementation of a class that reads raw text and produces tokens.
 *  \author (C) Copyright 2024 by Peter Chapin <owpeter@pchapin.org>
 */

#include "Lexer.hpp"

namespace scanner {

    Lexer::Lexer( SourceFile &ada_source ) : source( ada_source ) { }
}
