package edu.usb.argos.ASTProcessor.visitor.application.analyzers.complexity;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;

public class ComplexityVisitor extends JavaParserBaseVisitor<Void> {
    private int cyclomaticComplexity = 1;
    private int currentNestingDepth = 0;
    private int maxNestingDepth = 0;
    private int numberOfStatements = 0;

    @Override
    public Void visitStatement(JavaParser.StatementContext ctx) {
        numberOfStatements++;
        if (isComplexityIncreasingStatement(ctx)) {
            cyclomaticComplexity++;
            currentNestingDepth++;
            maxNestingDepth = Math.max(maxNestingDepth, currentNestingDepth);
        }
        Void result = super.visitStatement(ctx);
        if (isComplexityIncreasingStatement(ctx)) {
            currentNestingDepth--;
        }
        return result;
    }

    private boolean isComplexityIncreasingStatement(JavaParser.StatementContext ctx) {
        if (ctx.IF() != null || ctx.FOR() != null || ctx.WHILE() != null ||
                ctx.DO() != null || ctx.SWITCH() != null) {
            return true;
        }

        return ctx.catchClause() != null && !ctx.catchClause().isEmpty();
    }

    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    public int getMaxNestingDepth() {
        return maxNestingDepth;
    }

    public int getNumberOfStatements() {
        return numberOfStatements;
    }
}
