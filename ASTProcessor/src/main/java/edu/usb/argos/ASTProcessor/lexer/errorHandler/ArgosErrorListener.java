package edu.usb.argos.ASTProcessor.lexer.errorHandler;

import edu.usb.argos.ASTProcessor.lexer.errorHandler.exceptions.AntlrException;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;

public class ArgosErrorListener implements ANTLRErrorListener {

    private static final Logger logger = LoggerFactory.getLogger(ArgosErrorListener.class);

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg, RecognitionException e) {
        String errorMessage = "Syntax error at line " + line + ":" + charPositionInLine + " " + msg;
        logger.error(errorMessage);
        throw new AntlrException(errorMessage);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                                boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        String ambiguityMessage = "Ambiguity found in " + recognizer + " at " + startIndex + ":" + stopIndex;
        logger.warn(ambiguityMessage);
        throw new AntlrException(ambiguityMessage);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex,
                                            int stopIndex, BitSet conflictingAlts,
                                            ATNConfigSet configs) {
        String contextMessage = "Attempt of full context between indexes " + startIndex +
                " and " + stopIndex + ". Alternatives in conflict: " + conflictingAlts;
        logger.info(contextMessage);
        throw new AntlrException(contextMessage);
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex,
                                         int stopIndex, int prediction, ATNConfigSet configs) {
        String sensitivityMessage = "Context sensitivity detected among the indexes " + startIndex +
                " and " + stopIndex + ". Prediction: " + prediction;
        logger.info(sensitivityMessage);
        throw new AntlrException(sensitivityMessage);
    }
}
