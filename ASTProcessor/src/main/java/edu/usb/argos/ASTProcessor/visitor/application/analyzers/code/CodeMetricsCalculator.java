package edu.usb.argos.ASTProcessor.visitor.application.analyzers.code;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.CodeMetrics;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

public class CodeMetricsCalculator {
    public CodeMetrics calculate(JavaParser.MethodDeclarationContext ctx) {
        MetricsCounters counters = new MetricsCounters();
        processTokens(ctx.getStart(), ctx.getStop(), counters);
        return new CodeMetrics(
                counters.getLinesOfCode(),
                counters.getEffectiveLines(),
                counters.getCommentLines(),
                counters.getEmptyLines()
        );
    }

    private void processTokens(Token start, Token stop, MetricsCounters counters) {
        TokenStream tokens = (TokenStream) start.getInputStream();
        for (int i = start.getTokenIndex(); i <= stop.getTokenIndex(); i++) {
            Token token = tokens.get(i);
            categorizeToken(token, counters);
        }
    }

    private void categorizeToken(Token token, MetricsCounters counters) {
        if (token.getChannel() == Token.DEFAULT_CHANNEL) {
            counters.incrementEffectiveLines();
        } else if (isComment(token)) {
            counters.incrementCommentLines();
        } else if (token.getText().trim().isEmpty()) {
            counters.incrementEmptyLines();
        }
    }

    private boolean isComment(Token token) {
        return token.getType() == JavaLexer.COMMENT ||
                token.getType() == JavaLexer.LINE_COMMENT;
    }
}
