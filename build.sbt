import Dependencies._

enablePlugins(Antlr4Plugin)

ThisBuild / organization  := "org.pchapin"
ThisBuild / version       := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion  := "2.12.8"
ThisBuild / scalacOptions :=
  Seq("-encoding", "UTF-8",
      "-feature",
      "-deprecation",
      "-unchecked",
      "-Ywarn-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-infer-any",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard")

logBuffered in Test := false

lazy val augusta = (project in file("."))
  .settings(
    name := "Augusta",
    libraryDependencies ++= augustaDeps,

    antlr4Version     in Antlr4 := "4.7.2",
    antlr4PackageName in Antlr4 := Some("org.pchapin.augusta"),
    antlr4GenListener in Antlr4 := true,
    antlr4GenVisitor  in Antlr4 := true
  )
  .dependsOn(dragon)


lazy val tiger = (project in file("Tiger"))
  .settings(
    name := "Tiger",
    libraryDependencies ++= tigerDeps
  )
  .dependsOn(dragon)


lazy val dragon = (project in file("Dragon"))
  .settings(
    name := "Dragon",
    libraryDependencies ++= dragonDeps
    //excludeFilter in unmanagedSources := HiddenFileFilter || "*slem*"
  )

