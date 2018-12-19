name := "yuima.towin"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

lazy val util = RootProject(file("../util"))

lazy val towin = (project in file("."))
  .dependsOn(util).aggregate(util)

logLevel in Global := Level.Error

showSuccess := false

outputStrategy := Some(StdoutOutput)

libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.0"

dependencyOverrides +=  "org.json4s" %% "json4s-core" % "3.6.0"

dependencyOverrides +=  "org.json4s" %% "json4s-ast" % "3.6.0"
