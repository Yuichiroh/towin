name := "yuima.towin"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

lazy val util = RootProject(file("../util"))

lazy val towin = (project in file("."))
  .dependsOn(util).aggregate(util)

logLevel in Global := Level.Error

showSuccess := false

outputStrategy := Some(StdoutOutput)

javaOptions in Test += "-Djava.library.path=./lib/"

javaOptions in Runtime += "-Djava.library.path=./lib/"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.0"

dependencyOverrides +=  "org.json4s" %% "json4s-core" % "3.6.0"

dependencyOverrides +=  "org.json4s" %% "json4s-ast" % "3.6.0"
