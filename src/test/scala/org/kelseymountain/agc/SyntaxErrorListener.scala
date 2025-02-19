package org.kelseymountain.agc

import org.antlr.v4.runtime._

class SyntaxErrorListener extends BaseErrorListener:
  private val errors = scala.collection.mutable.ListBuffer.empty[String]

  override def syntaxError(
    recognizer        : Recognizer[_, _],
    offendingSymbol   : Object,
    line              : Int,
    charPositionInLine: Int,
    message           : String,
    ex                : RecognitionException): Unit =

    val errorMsg = s"Line $line:$charPositionInLine - $message"
    errors += errorMsg
  end syntaxError

  def hasErrors: Boolean = errors.nonEmpty
  def getErrors: List[String] = errors.toList
