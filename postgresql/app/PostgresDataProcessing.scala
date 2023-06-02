package PostgresDataProcessing

import scala.util.Properties
import java.sql.{Connection, DriverManager}
import java.sql.SQLException
import org.apache.spark.sql.DataFrame
import sys.process._
import scala.language.postfixOps
import app.helpers.SparkHelper


object PostgresDataProcessing {
  def connnectToPostgres(host : String, port : String, db : String, user : String, password : String) : Connection = {
    var postgresConnection: Connection = null
    DriverManager.setLoginTimeout(20);
    try {
      postgresConnection = DriverManager.getConnection(s"jdbc:postgresql://$host:$port/$db", user, password)
      println("Postgres Connected.")
    } catch {
      case e: SQLException => {
        println("Could not connect to Postgres.")
        System.exit(1)
      }
    }
    (postgresConnection)
  }

  def getQueries(pgHost : String, pgPort : String, pgDB : String, pgUser : String, pgPassword : String, query : String): DataFrame = {
    val spark = SparkHelper.getSpark()
    var df = spark.read
      .format("jdbc")
      .option("url", s"jdbc:postgresql://${pgHost}:${pgPort}/${pgDB}")
      .option("user", pgUser)
      .option("password", pgPassword)
      .option("dbTable", s"($query) as que")
      .option("driver", "org.postgresql.Driver")
      // .options(Map(
      //   "url" -> s"jdbc:postgresql://${pgHost}:${pgPort}/${pgDB}",
      //   "user" -> pgUser,
      //   "password" -> pgPassword,
      //   "dbTable" -> s"($query) as que",
      //   "driver" -> "org.postgresql.Driver"
      // ))
      .load()
    (df)
  }

  def main(args: Array[String]) : Unit = {
    val pgHost = sys.env("DB_HOST")
    val pgPort = sys.env("DB_PORT")
    val pgDB = sys.env("DB")
    val pgUser = sys.env("DB_USER")
    val pgPassword = sys.env("DB_PASSWORD")

    val spark = SparkHelper.getSpark()

    val postgresConnection = connnectToPostgres(pgHost, pgPort, pgDB, pgUser, pgPassword)
    val statement = postgresConnection.createStatement()
    statement.executeUpdate("CREATE EXTENSION IF NOT EXISTS pg_stat_statements;");

    println(("docker restart postgres-thesis")!!)

    var df = getQueries(pgHost, pgPort, pgDB, pgUser, pgPassword, "SELECT query FROM pg_stat_statements")
    df.show()
    // val scriptRunner = new ScriptRunner(connection);
    SparkHelper.closeSession()
  }
}
