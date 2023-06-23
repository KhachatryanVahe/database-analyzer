package parser

import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree.ParseTreeWalker


object Main {
  def main(args: Array[String]): Unit = {
    val input = "SELECT column1, column2 FROM table1, table2 WHERE column1 = 'value' AND column2 > 10"
    val lexer = new PostgreSQLLexer(CharStreams.fromString(input))
    val tokens = new CommonTokenStream(lexer)
    val parser = new PostgreSQLParser(tokens)
    val tree = parser.selectStatement()

    val listener = new CustomPostgreSQLListener()
    val walker = new ParseTreeWalker()
    walker.walk(listener, tree)
  }
}


