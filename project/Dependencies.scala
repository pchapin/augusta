import sbt._

// See: https://www.scala-sbt.org/1.x/docs/Organizing-Build.html
object Dependencies {

  // Versions
  lazy val scalaTestVersion = "3.2.17"
  lazy val scalaGraphCoreVersion = "1.13.6"  // Are we ready to update to version 2.0.0?

  // Kiama and ScalaZ are not needed at this time.
  //
  // lazy val kiamaVersion = "2.2.0"
  // lazy val scalazVersion = "7.3.0-SNAPSHOT"

  // Libraries
  val scalactic = "org.scalactic" %% "scalactic" % scalaTestVersion
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
  val scalaGraphCore = "org.scala-graph" %% "graph-core" % scalaGraphCoreVersion
  // val kiama = "org.bitbucket.inkytonik.kiama" %% "kiama" % kiamaVersion
  // val scalaz = "org.scalaz" %% "scalaz" % scalazVersion

  // Projects
  val augustaDeps: Seq[ModuleID] = Seq(scalaGraphCore, scalactic, scalaTest % Test)
  val tigerDeps: Seq[ModuleID] = Seq(scalactic, scalaTest % Test)
  val dragonDeps: Seq[ModuleID] = Seq(scalactic, scalaTest % Test)

}
