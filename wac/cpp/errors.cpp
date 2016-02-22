/*!
    \file   errors.cpp
    \brief  Various error reporting services.
    \author (C) Copyright 2010 by Peter C. Chapin <peter@openwatcom.org>
*/

#include <cstdlib>
#include <iostream>
#include "errors.hpp"

namespace wac {

    //! Inform user of an out of memory condition.
    /*!
     * This function treats out of memory problems as fatal errors. If the program is unable to
     * find the memory it needs to do what needs doing there is little point in continuing. Wac
     * does not attempt to reclaim memory and try again. Thus this function aborts the program
     * immediately.
     *
     * \param location The full name of the function or method where the out of memory condition
     * was detected.
     */
    void report_memory_exhaustion_and_abort( const char * const location ) throw( )
    {
        std::cout << "*** OUT OF MEMORY!\n";
        std::cout << "       Location: " << location << "\n";
        std::abort( );
    }


    //! Inform user of an unexpected exception.
    /*!
     * This function assumes that unexpected exceptions are internal errors much like an
     * assertion failure. It logs the exception accordingly. The behavior of the program after
     * calling this function is up to the caller. Depending on the situation it may make sense
     * to continue normally, throw a special exception, or even abort the program.
     *
     * \param location The full name of the function or method were the exception was detected.
     *
     * \todo A log file should probably be used (as an option at least) when reporting internal
     * errors. The precise format of the report is subject to change.
     */
    void report_unexpected_exception( const char * const location ) throw( )
    {
        std::cout << "*** Internal Error: Unexpected exception!\n";
        std::cout << "       Location: " << location << "\n";
    }


    //! Terminate the program at once.
    /*!
     * Currently this function simply calls exit. A future version might add a message to a log
     * file.
     *
     * \param status The exit status returned to the operating system.
     */
    void terminate_program( int status ) throw( )
    {
        std::cout << "PROGRAM TERMINATED\n";
        std::exit( status );
    }

}
