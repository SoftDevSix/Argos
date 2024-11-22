package edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Expression;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;

import java.util.List;

public interface CodeElementAdapter<S, E> {
    List<Statement<S>> getStatements();
    List<Expression<E>> getExpressions();
    String getName();
}
