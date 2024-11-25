package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import lombok.Value;

@Value
public class StatementAdapter implements Statement<JavaParser.StatementContext> {
    JavaParser.StatementContext node;

    @Override
    public JavaParser.StatementContext getNode() {
        return node;
    }
}
