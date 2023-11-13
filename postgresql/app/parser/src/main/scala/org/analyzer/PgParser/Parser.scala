package org.analyzer.PgParser

import scala.collection.mutable.Map
import scala.collection.JavaConverters._

object Parser {
  def parse(query: String): (Map[String, Integer]) = {
    var processedQuery = query.trim()
    if (processedQuery.takeRight(1) != ";") {
      processedQuery += ";"
    }
    println("query =====" + query)
    val listener = PgParser.parse(processedQuery)
    return (listener.getFeatureMap().asScala)
  }

   def getInfo(input: String) : (Map[String, Integer]) = {
    // var rules = Map[String, Integer]()

    val (r) = parse(input)
    // rules = r
    // println("rules == " + rules)
    return r
  }

  // def getInfo(input: String) : (Map[String, Integer]) = {
  //   var rules = Map[String, Integer]()
  //   var error : String = null

  //   try {
  //     val (r) = parse(input)
  //     rules = r
  //   } catch {
  //     case e: Throwable => {
  //       error = e.getMessage()
  //     }
  //   }
  //   println("rules == ", rules)
  //   return (rules)
  // }

  // def main(args: Array[String]): Unit = {
  //   val inputs = Array(
  //     // "SELECT column1, column2 FROM table1, table2 WHERE column1 = 'value' AND column2 > 10",
  //     "SELECT column3 FROM table3 WHERE column3 = 'value' AND column4 = 5",
  //     // "INSERT INTO table3 (col1, col2 ) VALUES ('asd', '54')"
  //   )

  //   try {
  //     for(input <- inputs) {
  //       val (rules) = parse(input)
  //       println("rules = " + rules)
  //     }
  //   } catch {
  //     case e: Throwable => {
  //       println("error = " + e.getMessage())
  //     }
  //   }
  // }
}
