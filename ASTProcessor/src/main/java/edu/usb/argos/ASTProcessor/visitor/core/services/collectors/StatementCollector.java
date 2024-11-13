package edu.usb.argos.ASTProcessor.visitor.core.services.collectors;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IExpressionCollector;

import java.util.ArrayList;
import java.util.List;

public class StatementCollector implements
        IExpressionCollector<JavaParser.ExpressionContext, JavaParser.MethodDeclarationContext> {
    @Override
    public List<JavaParser.ExpressionContext> collectExpressions(JavaParser.MethodDeclarationContext ctx) {
        ExpressionVisitor visitor = new ExpressionVisitor();
        ctx.accept(visitor);
        return visitor.getExpressions();
    }

    private static class ExpressionVisitor extends JavaParserBaseVisitor<Void> {
        private final List<JavaParser.ExpressionContext> expressions = new ArrayList<>();

        @Override
        public Void visitExpression(JavaParser.ExpressionContext ctx) {
            expressions.add(ctx);
            visitChildren(ctx);
            return null;
        }

        public List<JavaParser.ExpressionContext> getExpressions() {
            return expressions;
        }
    }
}
