package edu.usb.argos.ASTProcessor.lexer.errorHandler;

import edu.usb.argos.ASTProcessor.lexer.errorHandler.exceptions.AntlrException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

public class ArgosErrorListener implements ANTLRErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new AntlrException("Syntax error at line " + line + ":" + charPositionInLine + " " + msg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                                boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        throw new AntlrException("Ambiguity found in " + recognizer + " at " + startIndex + ":" + stopIndex);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex,
                                            int stopIndex, BitSet conflictingAlts,
                                            ATNConfigSet configs) {
        throw new AntlrException("Attempt of full context between indexes " + startIndex + " and " + stopIndex +
                ". Alternatives in conflict: " + conflictingAlts);
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex,
                                         int stopIndex, int prediction, ATNConfigSet configs) {
        throw new AntlrException("Context sensitivity detected among the indexes " + startIndex + " and " + stopIndex +
                ". Prediction: " + prediction);
    }
}
