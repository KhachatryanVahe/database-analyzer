package org.analyzer.processing

import scala.util.Properties
import java.sql.{Connection, DriverManager}
import java.sql.SQLException
import org.apache.spark.sql.DataFrame
import sys.process._
import scala.language.postfixOps
import app.helpers.SparkHelper
import org.analyzer.PgParser.Parser

object Processing {
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

  def getQueries(host : String, port : String, db : String, user : String, password : String, query : String): DataFrame = {
    val spark = SparkHelper.getSpark()
    var df = spark.read
      .format("jdbc")
      .option("url", s"jdbc:postgresql://$host:$port/$db")
      .option("user", user)
      .option("password", password)
      .option("dbTable", s"($query) as que")
      .option("driver", "org.postgresql.Driver")
      .load()
    (df)
  }

  def main(args: Array[String]) : Unit = {
    val host = sys.env("DB_HOST")
    val port = sys.env("DB_PORT")
    val db = sys.env("DB")
    val user = sys.env("DB_USER")
    val password = sys.env("DB_PASSWORD")

    val spark = SparkHelper.getSpark()
    // val postgresConnection = connnectToPostgres(host, port, db, user, password)
    // val statement = postgresConnection.createStatement()
    // statement.executeUpdate("CREATE EXTENSION IF NOT EXISTS pg_stat_statements;");

    // println(("docker restart postgres-thesis")!!)

    // var df = getQueries(host, port, db, user, password, "SELECT query FROM pg_stat_statements")
    // df.show()
    val inputs = Array(
      // "SELECT column1, column2 FROM table1, table2 WHERE column1 = 'value' AND column2 > 10",
      "SELECT column3 FROM table3 WHERE column3 = 'value' AND column4 = 5",
      // "INSERT INTO table3 (col1, col2 ) VALUES ('asd', '54')"
    )
    val input = "SELECT column3 FROM table3 WHERE column3 = 'value' AND column4 = 5"
    println("input = " + input)
    val (rules) = Parser.getInfo(input)
    println("rules = " + rules)

    // try {
    //   for(input <- inputs) {
    //     println("input = " + input)
    //     val (rules) = Parser.getInfo(input)
    //     println("rules = " + rules)
    //   }
    // } catch {
    //   case e: Throwable => {
    //     println("error = " + e.getMessage())
    //   }
    // }
    SparkHelper.closeSession()
  }
}
