
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / organization := "org.pchapin"

lazy val augusta = (project in file("."))
  .settings(
    name := "Augusta"
  )
