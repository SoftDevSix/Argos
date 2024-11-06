package edu.usb.argos.ASTProcessor.lexer;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

public class CustomErrorListener implements ANTLRErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg, RecognitionException e) {
        System.err.println("Syntax error at line " + line + ":" + charPositionInLine + " - " + msg);

        throw new RuntimeException("Lexer: Syntax error - " + msg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                                boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        System.err.println("Ambiguity detected between the indexes " + startIndex + " y " + stopIndex +
                ". Ambiguous alternatives: " + ambigAlts);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex,
                                            int stopIndex, BitSet conflictingAlts,
                                            ATNConfigSet configs) {
        System.err.println("Attempt of full context between indexes " + startIndex + " and " + stopIndex +
                ". Alternatives in conflict: " + conflictingAlts);
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex,
                                         int stopIndex, int prediction, ATNConfigSet configs) {
        System.err.println("Context sensitivity detected among the indexes " + startIndex + " and " + stopIndex +
                ". Prediction: " + prediction);
    }
}
