/*!
 *  \file   SourceFile.hpp
 *  \brief  Declarations of services for reading source files.
 *  \author (C) Copyright 2024 by Peter Chapin <owpeter@pchapin.org>
 */

#ifndef SOURCEFILE_HPP
#define SOURCEFILE_HPP

#include <cwchar>    // Needed for std::wint_t
#include <fstream>
#include <new>
#include "errors.hpp"

namespace scanner {

    //! Exception for file open errors.
    class OpenFailure : public wac::RunTimeError {
    public:
        explicit OpenFailure( const std::string &error_message ) noexcept :
            RunTimeError( error_message )
            { }
    };

    //! Exception for file read errors.
    class ReadFailure : public wac::RunTimeError {
    public:
        explicit ReadFailure( const std::string &error_message ) noexcept :
            RunTimeError( error_message )
            { }
    };

    //! Exception for invalid (or unsupported) source file encodings.
    class InvalidEncoding : public wac::RunTimeError {
    public:
        explicit InvalidEncoding( const std::string &error_message ) noexcept :
            RunTimeError( error_message )
            { }
    };

    //! Exception for invalid (or unsupported) input characters.
    class InvalidCharacter : public wac::RunTimeError {
    public:
        explicit InvalidCharacter( const std::string &error_message ) noexcept :
            RunTimeError( error_message )
            { }
    };


    //! This class decodes a source file into a stream of Unicode characters.
    /*!
     * Ada source files use the ISO 10646 character set in Normalization Form KC (support for
     * other normalization forms is implementation defined; wac currently does not support any
     * others). The precise source character encodings supported are implementation defined; wac
     * supports UTF8, UTF16 (using a BOM) and explicit UTF16BE and UTF16LE.
     *  
     * This class encapsulates the process of detecting the encoding used by a source file and
     * then decoding the file. This class also watches for the ends of lines as marked with one
     * or more 16#0A# (LINE FEED), 16#0B# (LINE TABULATION), 16#0C# (FORM FEED), 16#0D#
     * (CARRIAGE RETURN), or 16#85# (NEXT LINE) character. [Note: The Ada standard also says
     * line ends are marked with characters in the separator_line and separator_paragraph
     * categories. See section 2.1 of the Ada reference manual.] Each occurrence of a line
     * separator character counts as one line except that adjacent CARRIAGE RETURN, LINE FEED
     * pairs count as only a single separator.
     *  
     * This class returns decoded characters from the file as values of type wchar_t. It is
     * assumed that wchar_t is a 16 bit type.
     */
    class SourceFile {
    public:
        typedef long line_type;     // Numbering starts at 1.
        typedef int  column_type;   // Numbering starts at 1.
        enum encoding_type {
            AUTOMATIC,
            UTF8,
            UTF16BE,
            UTF16LE
        };

        explicit SourceFile(
            const char *file_name, const encoding_type expected_encoding = AUTOMATIC );

       ~SourceFile( ) throw( );
       
        std::wint_t   next_char( );
        line_type     current_line( ) const noexcept;
        column_type   current_column( ) const noexcept;
        encoding_type current_encoding( ) const noexcept;

    private:
        // Disable copying the old fashion way.
        SourceFile( const SourceFile & );
        SourceFile &operator=( const SourceFile & );
        
        void detect_encoding( );

        std::ifstream *file;
        bool           start_of_line;
        line_type      line_number;
        column_type    column_number;
        encoding_type  encoding;
    };

}

#endif

