package parser

import scala.collection.mutable.Map
import scala.jdk.CollectionConverters._

object PostgresParser {
  def parse(query: String): Map[String, Integer] = {
    var processedQuery = query.trim()
    if (processedQuery.takeRight(1) != ";") {
      processedQuery += ";"
    }
    val listener = PgParser.parse(processedQuery)
    return (listener.getFeatureMap().asScala)
  }

  def getInfo(input: String): Map[String, Integer] = {
    var rules = Map[String, Integer]()
    var error: String = null
    try {
      val (r) = parse(input)
      rules = r
    } catch {
      case e: Throwable => {
        error = e.getMessage()
        println("error ======" + error)
      }
    }
    return (rules)
  }
}
