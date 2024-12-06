package org.kelseymountain.agc

import cats.effect._
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._

/**
  * The main object of the Augusta.
  */
object Main extends IOApp {

  // Just the sample from the Cats Effect README for now...
  def run(args: List[String]): IO[ExitCode] =
    if (args.headOption.map(_ == "--do-it").getOrElse(false))
      IO.println("I did it!").as(ExitCode.Success)
    else
      IO.println("I didn't do it.").as(ExitCode(-1))
}
