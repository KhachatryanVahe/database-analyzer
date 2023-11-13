package org.analyzer.PgParser;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


public class PgParser {

  // String query;

  // public PgParser(String query) {
  //     this.query = query;
  // }

  // public CustomPostgreSQLListener call() throws Exception {
  //   return parse(this.query);
  // }

  public static CustomPostgreSQLListener parse(String query) {
    CustomPostgreSQLListener listener = (CustomPostgreSQLListener) walk(query);
    return listener;
  }

  public static PostgreSQLBaseListener walk(String query) throws ParseCancellationException {
    CharStream characterStream = CharStreams.fromString(query);
    PostgreSQLLexer lexer = new PostgreSQLLexer(characterStream);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    PostgreSQLParser parser = new PostgreSQLParser(tokens);
    ParseTree tree = parser.query();
    CustomPostgreSQLListener listener = new CustomPostgreSQLListener();
    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(listener, tree);
    return listener;
  }
}