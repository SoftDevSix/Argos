package edu.usb.argos.ASTProcessor.lexer.errorhandler;

import edu.usb.argos.ASTProcessor.lexer.errorhandler.exceptions.AntlrException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

class ArgosErrorListenerTest {
    private final ArgosErrorListener errorListener = new ArgosErrorListener();

    @Test
    void testSyntaxError() {
        int line = 1;
        int charPositionInLine = 1;
        String msg = "Syntax issue";
        RecognitionException e = null;

        AntlrException exception = assertThrows(AntlrException.class, () -> {
            errorListener.syntaxError(null, null, line, charPositionInLine, msg, e);
        });
        assertEquals("Syntax error at line 1:1 Syntax issue", exception.getMessage());
    }

    @Test
    void testReportAmbiguity() {
        Parser recognizer = null;
        DFA dfa = null;
        int startIndex = 10;
        int stopIndex = 20;
        boolean exact = false;
        BitSet ambigAlts = new BitSet();
        ATNConfigSet configs = null;

        AntlrException exception = assertThrows(AntlrException.class, () -> {
            errorListener.reportAmbiguity(recognizer, dfa, startIndex, stopIndex, exact, ambigAlts, configs);
        });
        assertEquals("Ambiguity found in null at 10:20", exception.getMessage());
    }

    @Test
    void testReportAttemptingFullContext() {
        Parser recognizer = null;
        DFA dfa = null;
        int startIndex = 5;
        int stopIndex = 15;
        BitSet conflictingAlts = new BitSet();
        ATNConfigSet configs = null;

        AntlrException exception = assertThrows(AntlrException.class, () -> {
            errorListener.reportAttemptingFullContext(recognizer, dfa, startIndex, stopIndex, conflictingAlts, configs);
        });
        assertEquals(
                "Attempt of full context between indexes 5 and 15. Alternatives in conflict: {}",
                exception.getMessage()
        );
    }

    @Test
    void testReportContextSensitivity() {
        Parser recognizer = null;
        DFA dfa = null;
        int startIndex = 0;
        int stopIndex = 10;
        int prediction = 1;
        ATNConfigSet configs = null;

        AntlrException exception = assertThrows(AntlrException.class, () -> {
            errorListener.reportContextSensitivity(recognizer, dfa, startIndex, stopIndex, prediction, configs);
        });
        assertEquals(
                "Context sensitivity detected among the indexes 0 and 10. Prediction: 1",
                exception.getMessage()
        );
    }
}
