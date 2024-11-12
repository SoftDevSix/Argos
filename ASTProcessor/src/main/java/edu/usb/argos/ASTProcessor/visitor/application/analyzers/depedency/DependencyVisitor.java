package edu.usb.argos.ASTProcessor.visitor.application.analyzers.depedency;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.DependencyInfo;

import java.util.ArrayList;
import java.util.List;

public class DependencyVisitor extends JavaParserBaseVisitor<Void> {
    private final List<String> methodCalls = new ArrayList<>();
    private final List<String> fieldAccess = new ArrayList<>();
    private final List<String> exceptions = new ArrayList<>();

    @Override
    public Void visitMethodCall(JavaParser.MethodCallContext ctx) {
        methodCalls.add(ctx.identifier().getText());
        return super.visitMethodCall(ctx);
    }

    @Override
    public Void visitExpression(JavaParser.ExpressionContext ctx) {
        if (isFieldAccess(ctx)) {
            fieldAccess.add(ctx.identifier().getText());
        }
        return super.visitExpression(ctx);
    }

    @Override
    public Void visitStatement(JavaParser.StatementContext ctx) {
        if (ctx.THROW() != null && ctx.expression() != null) {
            exceptions.add(ctx.expression().getClass().getName());
        }
        return super.visitStatement(ctx);
    }

    private boolean isFieldAccess(JavaParser.ExpressionContext ctx) {
        return ctx.bop != null &&
                ctx.bop.getText().equals(".") &&
                ctx.identifier() != null;
    }

    public DependencyInfo getDependencyInfo() {
        return new DependencyInfo(
                methodCalls,
                fieldAccess,
                exceptions
        );
    }
}
