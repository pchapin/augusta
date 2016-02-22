import java.io._
import java.nio.charset.{Charset, CharsetDecoder, CharsetEncoder}

/**
 * This program converts files using one character encoding into files using another.
 */
object CharacterConverter {

  // All implementations of the Java platform are required to support the following.
  // See the documentation on java.lang.nio.charset.Charset for more information.
  val validCharacterSets =
    List("US-ASCII", "ISO-88591-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16")

  def isValidCharacterSet(name: String) = validCharacterSets.exists( name == _ )

  def main(args: Array[String]) {
    if (args.length != 2) {
      println("Usage: CharacterConverter name1:cset1 name2:cset2")
      println("Where: name1 is the source file name")
      println("       cset1 is the source character set name")
      println("       name2 is the destination file name")
      println("       cset2 is the destination character set name")
      println("")
      println("Allowed character set names:")
      println("       US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16")
    }
    else {
      try {
        // TODO: Some error handling in this program would be nice!

        val source       = args(0).split(":")
        val destination  = args(1).split(":")

        // I will at least check that the character sets requested are acceptable.
        if (!isValidCharacterSet(source(1)) || !isValidCharacterSet(destination(1))) {
          println(s"Error: At least one of '${source(1)}' or '${destination(1)}' is invalid.")
        }
        else {
          // It is necessary to use Decoders and Encoders to get MalformedInputExceptions.

          val inputDecoder = Charset.forName(source(1)).newDecoder
          val inputReader =
            new InputStreamReader(new FileInputStream(source(0)), inputDecoder)
          val inputBufferedReader = new BufferedReader(inputReader)

          val outputEncoder = Charset.forName(destination(1)).newEncoder
          val outputWriter =
            new OutputStreamWriter(new FileOutputStream(destination(0)), outputEncoder)
          val outputBufferedWriter = new BufferedWriter(outputWriter)

          var ch = -1
          while ({ ch = inputBufferedReader.read(); ch != -1 }) {
            outputBufferedWriter.write(ch)
          }

          inputBufferedReader.close();
          outputBufferedWriter.close();
        }
      }
      catch {
        case ex: Exception =>
          println(s"Unhandled exception: ${ex.getClass.toString}, ${ex.getMessage}")
      }
    }
  }

}
