package processing

import scala.util.Properties
import java.sql.{Connection, DriverManager}
import java.sql.SQLException
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{MapType, StructType, StructField}
import org.apache.spark.sql.types.DataTypes.{StringType, IntegerType}
import org.apache.spark.sql.functions.{udf, col}
import sys.process._
import scala.language.postfixOps

import app.helpers.SparkHelper
import parser.PostgresParser

object Processing {
  def connnectToPostgres(
      host: String,
      port: String,
      db: String,
      user: String,
      password: String
  ): Connection = {
    var postgresConnection: Connection = null
    DriverManager.setLoginTimeout(20);
    try {
      postgresConnection = DriverManager.getConnection(
        s"jdbc:postgresql://$host:$port/$db",
        user,
        password
      )
      println("Postgres Connected.")
    } catch {
      case e: SQLException => {
        println("Could not connect to Postgres.")
        System.exit(1)
      }
    }
    (postgresConnection)
  }

  def getQueries(
      host: String,
      port: String,
      db: String,
      user: String,
      password: String,
      query: String
  ): DataFrame = {
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

  def getObjects(
      host: String,
      port: String,
      db: String,
      user: String,
      password: String,
      query: String
  ): DataFrame = {
    val spark = SparkHelper.getSpark()
    var postgresConnection = connnectToPostgres(host, port, db, user, password)
    postgresConnection.createStatement().executeUpdate(Constants.createTableDefinitionGetterFunction)
    val df = spark.read
      .format("jdbc")
      .option("url", s"jdbc:postgresql://$host:$port/$db")
      .option("user", user)
      .option("password", password)
      .option("dbTable", s"($query) as que")
      .option("driver", "org.postgresql.Driver")
      .load()
    // TODO: fix asynchronous loading of dataframe and after that uncomment next line
    // postgresConnection.createStatement().executeUpdate(Constants.dropTableDefinitionGetterFunction)
    (df)
  }

  def addInfoColumns(queriesDF: DataFrame, queryField: String): DataFrame = {
    val spark = SparkHelper.getSpark()
    spark.sql("set spark.sql.legacy.allowUntypedScalaUDF = true")
    import spark.implicits._
    val parserInfoSchema = new StructType()
      .add(StructField("features", MapType(StringType, IntegerType)))
      .add(StructField("rules", MapType(StringType, IntegerType)))
      .add(StructField("score", IntegerType))
      .add(StructField("statementType", StringType))
      .add(StructField("parseError", StringType))

    def getParserInfo = udf(PostgresParser.getInfo _, parserInfoSchema)

    queriesDF
      .withColumn("parserInfo", getParserInfo(col(queryField)))
      .withColumn("features", $"parserInfo.features")
      .withColumn("rules", $"parserInfo.rules")
      .withColumn("score", $"parserInfo.score")
      .withColumn("statementType", $"parserInfo.statementType")
      .withColumn("parseError", $"parserInfo.parseError")
  }

  def main(args: Array[String]): Unit = {
    val host = sys.env("DB_HOST")
    val port = sys.env("DB_PORT")
    val db = sys.env("DB")
    val user = sys.env("DB_USER")
    val password = sys.env("DB_PASSWORD")

    var objectsDF = getObjects(
      host,
      port,
      db,
      user,
      password,
      Constants.getObjectsListQuery
    )
    objectsDF.show()

    var queriesDF = getQueries(
      host,
      port,
      db,
      user,
      password,
      "SELECT query, calls, mean_exec_time FROM pg_stat_statements"
    )
    queriesDF = addInfoColumns(queriesDF, "query")
    queriesDF.show()
  }
}
