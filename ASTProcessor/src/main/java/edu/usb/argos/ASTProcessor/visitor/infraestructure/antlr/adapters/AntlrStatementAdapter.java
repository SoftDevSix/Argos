package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;

public class AntlrStatementAdapter implements Statement<JavaParser.StatementContext> {
    private final JavaParser.StatementContext node;

    public AntlrStatementAdapter(JavaParser.StatementContext node) {
        this.node = node;
    }

    @Override
    public JavaParser.StatementContext getNode() {
        return node;
    }
}
