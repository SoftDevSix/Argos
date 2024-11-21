package edu.usb.argos.ASTProcessor.visitor.core.services.visitors;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpressionVisitor extends JavaParserBaseVisitor<Void> {
    private final List<JavaParser.ExpressionContext> expressions;

    public ExpressionVisitor() {
        this.expressions = new ArrayList<>();
    }

    @Override
    public Void visitExpression(JavaParser.ExpressionContext ctx) {
        if (ctx != null) {
            processExpression(ctx);
        }
        return null;
    }

    private void processExpression(JavaParser.ExpressionContext ctx) {
        expressions.add(ctx);
        visitChildren(ctx);
    }

    public List<JavaParser.ExpressionContext> getExpressions() {
        return Collections.unmodifiableList(expressions);
    }
}
