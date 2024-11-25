package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Expression;
import lombok.Value;

@Value
public class AntlrExpressionAdapter implements Expression<JavaParser.ExpressionContext> {
    JavaParser.ExpressionContext node;

    @Override
    public JavaParser.ExpressionContext getNode() {
        return node;
    }
}
