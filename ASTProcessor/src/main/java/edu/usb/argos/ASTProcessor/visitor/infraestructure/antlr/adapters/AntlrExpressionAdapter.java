package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Expression;

public class AntlrExpressionAdapter implements Expression<JavaParser.ExpressionContext> {
    private final JavaParser.ExpressionContext node;

    public AntlrExpressionAdapter(JavaParser.ExpressionContext node) {
        this.node = node;
    }

    @Override
    public JavaParser.ExpressionContext getNode() {
        return node;
    }
}
