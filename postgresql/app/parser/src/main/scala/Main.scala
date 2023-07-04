package parser

import org.antlr.v4.runtime._
import scala.collection.mutable.Map
import scala.jdk.CollectionConverters._
import java.util.concurrent._
import java.util.Arrays
import java.io._


object Main {

  def parse(query: String): (Map[String, Integer]) = {
    val executor = Executors.newSingleThreadExecutor()
    var result: java.util.concurrent.Future[CustomPostgreSQLListener] = null
    val timeout = 5

    var processedQuery = query.trim()
    if (processedQuery.takeRight(1) != ";") {
      processedQuery += ";"
    }
    println("processedQuery === " + processedQuery)

    try {
      val futures = executor.invokeAll(
        Arrays.asList(new CallableParse(processedQuery)),
        timeout,
        TimeUnit.SECONDS
      )
      result = futures.get(0)
    } catch {
      case e: InterruptedException => {}
    }

    executor.shutdown()
    try {
      val listener = result.get()
      return (listener.getFeatureMap().asScala) // , listener.getHash())
    } catch {
      case e: CancellationException => {
        throw new Exception(
          "Parser timed out after " + timeout + " seconds. query: " + query.substring(0, 20) + "..."
        )
      }
      case t: Throwable => {
        throw t
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val inputs = Array(
      // "SELECT column1, column2 FROM table1, table2 WHERE column1 = 'value' AND column2 > 10",
      "SELECT column3 FROM table3 WHERE column3 = 'value' AND column4 = 5",
      "INSERT INTO table3 (col1, col2 ) VALUES ('asd', '54')"
    )

    try {
      for(input <- inputs) {
        val (rules) = parse(input)
        println("rules = " + rules)
      }
    } catch {
      case e: Throwable => {
        println("error = " + e.getMessage())
      }
    }
  }
}
