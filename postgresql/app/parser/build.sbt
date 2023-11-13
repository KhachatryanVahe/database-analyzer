ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "org.analyzer"

lazy val parser = project
  .in(file("."))
  .enablePlugins(Antlr4Plugin)
  .settings(
    name := "PgParser",
    libraryDependencies ++= Seq(
      "org.antlr" % "antlr4" % "4.13.1",
      "org.antlr" % "antlr4-runtime" % "4.13.1",
    ),
    Antlr4 / antlr4Version := "4.13.1",
    Antlr4 / antlr4GenListener := true,
    Antlr4 / antlr4GenVisitor := false,
    Antlr4 / antlr4PackageName := Some("org.analyzer.PgParser")
  )


