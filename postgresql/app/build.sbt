sbtVersion:="1.9.0"
scalaVersion := "2.13.10"
val sparkVersion = "3.4.0"
name := "PostgresDataProcessing"
(assembly) / mainClass := Some("PostgresDataProcessing")
assembly / assemblyJarName := "app.jar"
libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-sql" % "3.4.0" % "provided"
)