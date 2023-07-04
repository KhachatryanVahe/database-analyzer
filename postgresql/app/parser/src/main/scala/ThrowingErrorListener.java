package parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class ThrowingErrorListener extends BaseErrorListener {

    public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
            throws ParseCancellationException {
        // this will filtered out of stdout by the logger and put in the logfile
        //System.out.println("antlr parse error, line " + line + ":" + charPositionInLine + " " + msg.replace("\n", "").replace("\r", ""));
        throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
    }
}