import sbt._

// See: https://www.scala-sbt.org/1.x/docs/Organizing-Build.html
object Dependencies {

  // Versions
  lazy val scalaTestVersion = "3.0.5"

  // Libraries
  val scalactic = "org.scalactic" %% "scalactic" % scalaTestVersion
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion

  // Projects
  val augustaDeps = Seq(scalactic, scalaTest % Test)
  val tigerDeps   = Seq(scalactic, scalaTest % Test)
  val dragonDeps  = Seq(scalactic, scalaTest % Test)

}
