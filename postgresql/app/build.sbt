sbtVersion:="1.8.0"
scalaVersion := "2.13.10"
val sparkVersion = "3.3.1"
name := "PostgresDataProcessing"
(assembly) / mainClass := Some("PostgresDataProcessing")
assembly / assemblyJarName := "app.jar"
libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-sql" % "3.3.1" % "provided",
    "org.postgresql" % "postgresql" % "42.5.0",
    // "org.scalikejdbc" %% "scalikejdbc" % "3.5.0"
)