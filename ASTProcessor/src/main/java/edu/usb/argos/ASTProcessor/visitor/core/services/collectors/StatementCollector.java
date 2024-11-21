package edu.usb.argos.ASTProcessor.visitor.core.services.collectors;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IExpressionCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.visitors.ExpressionVisitor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StatementCollector implements
        IExpressionCollector<JavaParser.ExpressionContext, JavaParser.MethodDeclarationContext> {
    @Override
    public List<JavaParser.ExpressionContext> collectExpressions(JavaParser.MethodDeclarationContext ctx) {
        return Optional.ofNullable(ctx)
                .map(this::visitAndCollectExpressions)
                .orElse(Collections.emptyList());
    }

    private List<JavaParser.ExpressionContext> visitAndCollectExpressions(JavaParser.MethodDeclarationContext ctx) {
        ExpressionVisitor visitor = new ExpressionVisitor();
        ctx.accept(visitor);
        return visitor.getExpressions();
    }
}
