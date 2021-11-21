//-----------------------------------------------------------------------
// FILE    : ConfigurationSettings.scala
// SUBJECT : A facility to manage a program's configuration
// AUTHOR  : (C) Copyright 2012 by Peter C. Chapin <pchapin@vtc.edu>
//-----------------------------------------------------------------------
package org.pchapin.dragon

import java.io.*

/**
 * Class to manage a collection of (name, value) pairs that can be used to hold the
 * configuration information used by a program. This class provides facilities for reading
 * settings from a configuration file and for processing a collection of command line arguments.
 *
 * @param configurableItems A map from the name of a configuration setting to a validation
 * function. The validation function is used to both check a candidate setting and to normalize
 * the value of the setting into whatever form the application finds convenient.
 */
class ConfigurationSettings(private val configurableItems: Map[String, String => String]) {
  import ConfigurationSettings.*

  /** Contains a map of (name, value) pairs. */
  private var settings = Map[String, String]()


  /**
   * Provides default values for some (possibly all) configurable items.
   *
   * @param defaultValues Map of (name, value) pairs with which to "seed" the settings.
   * @throws BadNameException if a name is encountered that is not configurable.
   * @throws BadValidationException if a value does not pass validation.
   */
  def setDefaults(defaultValues: Map[String, String]): Unit = {
    for ((name, value) <- defaultValues) {
      put(name, value)
    }
  }


  /**
   * Remove all text from '#' to the end of the string. Any '#' embedded inside a quoted
   * substring is treated as literal text.
   *
   * @param line The string of text to modify.
   *
   * @return The modified string. Note that in many cases the returned string will have trailing
   * white space characters. For example an input string such as "Hello! # This is a comment" is
   * returned as "Hello! ".
   */
  private def killComments(line: String): String = {
    object States extends Enumeration {
      val Normal, Quote = Value
    }
    import States.*

    var currentState = Normal
    var index = -1

    for (currentIndex <- 0 until line.length) {
      line(currentIndex) match {
        case '\"' => currentState = if (currentState == Normal) Quote else Normal
        case _ =>
          if (currentState == Normal && line(currentIndex) == '#')
            index = if (index == -1) currentIndex else index
      }
    }
    if (index == -1) line else line.substring(0, index)
  }


  /**
   * Read a configuration file and use its contents to populate the settings map. The file
   * consists of lines in the form
   *
   * name="value"
   *
   * The name contains only normal identifier characters (letters, digits, and the underscore).
   * The value can contain any text but the quotation marks are required in all cases. There can
   * be only one (name, value) pair on each line. Blank lines are ignored. Leading white space,
   * trailing white space, and white space around the '=' character are all ignored. White space
   * inside the value string is part of the value. Comments start with a '#' character and go to
   * the end of the line.
   *
   * @param fileName The name of the file to open. If the file can not be opened there is no
   * effect (no exception is thrown and the settings map is unchanged).
   *
   * @throws BadNameException if a name is encountered that is not configurable.
   * @throws BadValidationException if any of the file members fail to pass validation.
   */
  def readConfigurationFile(fileName: String): Unit = {
    // TODO: This method does not allow the value to contain an '=' character.
    // TODO: If a name is found that is not among the set of allowed configurable items, it is silently ignored.

    var inputFile: BufferedReader = null
    try {
      inputFile = new BufferedReader(new FileReader(fileName))
      var line: String = inputFile.readLine()
      while (line != null) {
        line = killComments(line)
        line = line.trim
        if (line.nonEmpty) {
          val fields = line.split("""\s*=\s*""")

          // Do some sanity checking on the line. Ignore it if it looks bad.
          if (fields.length == 2          &&
              fields(0).nonEmpty          &&
              fields(1).length >= 2       &&
              fields(1).charAt(0) == '\"' &&
              fields(1).charAt(fields(1).length - 1) == '\"') {

            fields(1) = fields(1).substring(1, fields(1).length - 1)
            put(fields(0), fields(1))
          }
        }
        line = inputFile.readLine()
      }
    }
    catch {
      // Explicitly ignore the exception if the configuration file can't be opened.
      case _: FileNotFoundException =>
    }
    finally {
      if (inputFile != null) inputFile.close()
    }
  }


  /**
   * Look up the value associated with a given name.
   *
   * @param name The name to look up.
   * @return An option containing the associated value as a string or None if there is no value.
   *
   * @throws BadNameException if a non-configurable name is used.
   */
  def apply(name: String): Option[String] = {
    if (!configurableItems.contains(name))
      throw new BadNameException("Attempt to look up unknown configuration item: " + name)
    if (settings.contains(name))
      Some(settings(name))
    else
      None
  }


  /**
   * Adds an item to the configuration.
   *
   * @param name The name of the configuration item to add.
   * @param value The value corresponding to the name. If the name already exists in the
   * configuration its value is replaced by this value, otherwise the (name, value) pair is
   * added.
   *
   * @throws BadNameException if the name does not specify a configurable item.
   * @throws BadValidationException if the value does not pass validation.
   */
  def put(name: String, value: String): Unit = {
    if (configurableItems.contains(name))
      settings += ( name -> configurableItems(name)(value) )
    else
      throw new BadNameException("Unknown configuration item: " + name)
  }

}


/**
 * Companion object to hold a collection of useful data validation methods. Each method accepts
 * a string to validate and returns a possibly modified string that represents the result of an
 * acceptable validation. If validation fails, a BadValidationException is thrown. Programmers
 * are free to use these methods, extend them, or ignore them as they see fit. However, if a
 * programmer uses a custom validation method it should conform to the same semantics as
 * described here.
 */
object ConfigurationSettings {

  /** Exception thrown when an unrecognized configurable name is encountered. */
  class BadNameException(message: String) extends Exception(message)

  /** Exception thrown by all validation methods when validation fails. */
  class BadValidationException(message: String) extends Exception(message)

  /**
   * Validates boolean settings. The strings "true" or "t" (case insensitive) are normalized to
   * "true." The strings "false" or "f" (case insensitive) are normalized to "false."
   *
   * @param raw The string to validate.
   * @throws BadValidationException if the raw string is not in the form described above.
   */
  def basicBooleanValidator(raw: String): String = {
    val upperRaw = raw.toUpperCase()
    if      (upperRaw == "TRUE"  || upperRaw == "T") "true"
    else if (upperRaw == "FALSE" || upperRaw == "F") "false"
    else
      throw new BadValidationException("Invalid boolean string")
  }

  // TODO: Do some real validation here.
  def basicIntegerValidator(raw: String): String = raw

  /*
   * Validates simple strings. Every string is considered valid.
   *
   * @param raw The string to validate.
   * @throws BadValidationException if raw is a null reference.
   */
  def basicStringValidator(raw: String): String =
    if (raw != null)
      raw
    else
      throw new BadValidationException("Invalid string (null reference)")

  /**
   * Verifies that the given string looks like a valid path name on the host system. This
   * validator does not attempt to verify if the specified file or directory exists or is
   * readable, etc. In some applications it is not an error to configure the name of a non-
   * existent file. More powerful validators can be defined that do check these features if
   * desired.
   */
  def basicPathValidator(raw: String): String = {
    // TODO: Be sure each character is a valid file name character.
    raw.map( ch =>
      if (ch == '/' || ch == '\\') File.separatorChar else ch
    )
  }
}
