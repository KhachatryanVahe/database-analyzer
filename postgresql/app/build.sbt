ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.10"

lazy val processing = project
  .in(file("."))
  .settings(
    name := "Query Analyzer",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % "3.5.0" % "provided"
    ),
    mainClass := Some("processing.Processing"),
    assemblyJarName := "app.jar",
  )
  .aggregate(parser)
  .dependsOn(parser)

lazy val parser = project
  .in(file("./parser"))
  .enablePlugins(Antlr4Plugin)
  .settings(
    name := "Postgres parser",
    libraryDependencies ++= Seq(
      "org.antlr" % "antlr4" % "4.9.3",
      "org.antlr" % "antlr4-runtime" % "4.9.3",
    ),
    Antlr4 / antlr4Version := "4.9.3",
    Antlr4 / antlr4GenListener := true,
    Antlr4 / antlr4GenVisitor := false,
    Antlr4 / antlr4PackageName := Some("parser")
  )
