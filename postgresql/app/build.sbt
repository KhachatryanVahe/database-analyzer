ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "org.analyzer"

lazy val processing = project
  .in(file("."))
  .settings(
    name := "Query Analyzer",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % "3.4.0" % "provided"
    ),
    mainClass := Some("org.analyzer.processing.Processing"),
    assemblyJarName := "app.jar",
  )
  .aggregate(parser)
  .dependsOn(parser)

lazy val parser = project
  .in(file("./parser"))
  .settings(
    name := "parser"
  )
