lazy val parser = (project in file("."))
  .enablePlugins(Antlr4Plugin)
  .settings(
    inThisBuild(
      List(
        organization := "org",
        scalaVersion := "2.13.10",
        version := "0.1.0"
      )),
    name := "parser",
    libraryDependencies ++= Seq(
      "org.antlr" % "antlr4" % "4.13.0",
      "org.antlr" % "antlr4-runtime" % "4.13.0",
    ),
    Antlr4 / antlr4Version := "4.13.0",
    Antlr4 / antlr4GenListener := true,
    Antlr4 / antlr4GenVisitor := false,
    Antlr4 / antlr4PackageName := Some("parser")
  )


