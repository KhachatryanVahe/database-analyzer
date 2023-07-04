package parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.concurrent.Callable;

public class CallableParse implements Callable<CustomPostgreSQLListener> {

    String query;

    public CallableParse(String query) {
        this.query = query;
    }

    public CustomPostgreSQLListener call() throws Exception {
        return parse(this.query);
    }

    public static CustomPostgreSQLListener parse(String query) {
        CustomPostgreSQLListener listener = (CustomPostgreSQLListener) walk(query);
        return listener;
    }

    public static PostgreSQLBaseListener walk(String query) throws ParseCancellationException {

        CharStream characterStream = CharStreams.fromString(query);
        PostgreSQLLexer lexer = new PostgreSQLLexer(characterStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PostgreSQLParser parser = new PostgreSQLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);
        ParseTree tree = parser.start();
        CustomPostgreSQLListener listener = new CustomPostgreSQLListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);
        return listener;
    }

    // public static String hash(String query) {

    //     try {
    //         CustomPostgreSQLListener listener = (CustomPostgreSQLListener) walk(query);
    //         return listener.getHash();

    //     } catch (ParseCancellationException ex) {
    //         //ex.printStackTrace();
    //         return null;
    //     }
    // }
}