/*!
    \file   SourceFile.cpp
    \brief  Definitions of services for reading source files.
    \author (C) Copyright 2010 by Peter C. Chapin <peter@openwatcom.org>
*/

#include <string>

#include "errors.hpp"
#include "SourceFile.hpp"

namespace scanner {

    //! Open the input file and prepare to read.
    /*!
     * The file must be in one of the supported encodings. If no expected encoding is specified,
     * this class will attempt to deduce the encoding automatically.
     *  
     * \param file_name The name of the file to open for reading.
     * \param expected_encoding The encoding of the file as specified by the caller. If the file
     * is not actually in the encoding given, an exception will be thrown when the first
     * undecodable character is encountered.
     *
     * \exception OpenFailure The specified file could not be opened.
     */
    SourceFile::SourceFile(
        const char * const  file_name, const encoding_type expected_encoding )
        throw( OpenFailure, std::bad_alloc ) :
    
        file          ( 0       ),
        start_of_line ( true    ),
        line_number   ( 0       ),
        column_number ( 0       ),
        encoding      ( expected_encoding )
    {
        file = new std::ifstream( file_name, std::ios::in | std::ios::binary );
        if( !*file ) {
            delete file;
            std::string message( "Unable to open file: ");
            message += file_name;
            throw OpenFailure( message );
        }
    }


    SourceFile::~SourceFile( ) throw( )
    {
        try {
            delete file;
        }
        catch( ... ) {
            wac::report_unexpected_exception( "scanner::SourceFile::~SourceFile" );
        }
    }


    //! Returns the next Unicode character from the file.
    /*!
     * If no characters have yet been read from the file and the expected encoding given to the
     * constructor was AUTOMATIC, this method attempts to deduce the file's encoding based on
     * the first few bytes in the file. The deduced encoding is recorded and then used for as
     * long as this file is open.
     *  
     * \return The next Unicode character or WEOF on end of file.
     *  
     * \exception InvalidCharacter A character was encountered that could not be decoded. This
     * includes characters outside the basic multilingual plane. If this exception occurs, the
     * object can still be used. It will attempt to skip past the invalid character.
     *
     * \exception InvalidEncoding The file does not appear to be encoded using one of the
     * supported encodings. If this exception occurs the object should no longer be used. This
     * exception can only occur when next_char() is called for the first time and then only when
     * an attempt was made to deduce the encoding automatically. If the expected encoding is not
     * AUTOMATIC this exception will not be thrown (although InvalidCharacter might be).
     *
     * \exception ReadFailure The input file could not be read. If this exception occurs, the
     * object should no longer be used.
     *  
     * \bug This method does not handle bare CR characters properly as end of line indications.
     *  
     * \bug This method only properly handles ASCII files. This obviously needs to be improved.
     */
    std::wint_t SourceFile::next_char( ) throw( InvalidCharacter, InvalidEncoding, ReadFailure )
    {
        if( encoding == AUTOMATIC ) detect_encoding( );
        
        if( start_of_line ) {
            ++line_number;
            column_number = 0;
            start_of_line = false;
        }

        uint8_t byte;        
        if( ( file->read( &byte, 1 ) ).gcount( ) != 1 ) {
            //lint -e1924
            if( file->eof( ) ) return WEOF;
            //lint +e1924
            throw ReadFailure( "Unable to completely read source file" );
        }
        ++column_number;
        const std::wint_t result =  static_cast< wchar_t >( byte );
        
        // If this is the end of a line, adjust records accordingly. Need to handle CR better.
        if( byte == '\x0A' || byte == '\x0B' || byte == '\x0C' || byte == '\x85' ) {
            start_of_line = true;
        }
        
        return( result );
    }
    

    //! Returns the line number of the current line.
    /*!
     * The first line of the source file is line #1.
     *
     * \return The line number of the last character returned by next_char().
     */
    SourceFile::line_type SourceFile::current_line( ) const throw( )
    {
        return( line_number );
    }


    //! Returns the column number of the current character.
    /*!
     * The first column is column #1.
     *
     * \return The column number of the last character returned by next_char().
     */
    SourceFile::column_type SourceFile::current_column( ) const throw( )
    {
        return( column_number );
    }

    
    //! Returns the encoding used by the file.
    /*!
     * The default encoding is as provided to the constructor or as deduced when the file is
     * first read. Once set, the encoding for a file never changes.
     *
     * \return The encoded used by the file.
     */
    SourceFile::encoding_type SourceFile::current_encoding( ) const throw( )
    {
        return( encoding );
    }

     
    //! From the first byte(s) of the file, guess at the decoding being used.
    /*!
     * This method uses simple huristics to determine the encoding used by the source file. It
     * should in most realistic cases correctly guess all supported encodings. If an unsupported
     * encoding is encountered, it will (most likely) just assume the default of UTF8. This may
     * cause InvalidCharacter exceptions to be thrown when the file is subsequently read.
     *  
     * \bug This approach used by this method is somewhat simplistic, causing it to guess wrong
     * in various (probably unlikely) cases. This should be improved eventually. The most
     * notable problem is that a UTF16LE file without a BOM will be mistaken for UTF8.
     */
    void SourceFile::detect_encoding( ) throw( InvalidEncoding )
    {
        uint8_t bom[2];

        const int byte = file->peek( );
        switch( byte ) {
        case 0x00:
            encoding = UTF16BE;
            break;
        case 0xFF:
            encoding = UTF16LE;
            if( ( file->read( bom, 2 ) ).gcount( ) != 2 ) {
                throw InvalidEncoding( "Incomplete read of the byte order mark" );
            }
            break;
        case 0xFE:
            encoding = UTF16BE;
            if( ( file->read( bom, 2 ) ).gcount( ) != 2 ) {
                throw InvalidEncoding( "Incomplete read of the byte order mark" );
            }
            break;
        default:
            encoding = UTF8;
            break;
        }
    }
}
