/*!
    \file   errors.hpp
    \brief  Declarations of various error reporting services.
    \author (C) Copyright 2010 by Peter C. Chapin <peter@openwatcom.org>
*/

#ifndef ERRORS_HPP
#define ERRORS_HPP

#include <string>
#include <stdexcept>

namespace wac {

    //! Base class for all wac related runtime error exceptions.
    class RunTimeError : public std::runtime_error {
    public:
        explicit RunTimeError( const std::string &error_message ) throw( ) :
            runtime_error( error_message )
            { }
    };

    void report_memory_exhaustion_and_abort( const char *location) throw( );
    void report_unexpected_exception( const char *location ) throw( );
    void terminate_program( int status ) throw( );
}

#endif

