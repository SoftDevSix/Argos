package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Expression;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Token;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MethodInformation<S, E, T> {
    String name;
    String returnType;
    List<String> modifiers;
    List<ParameterInformation> parameters;
    List<Statement<S>> statements;
    List<Expression<E>> expressions;
    Token<T> tokens;
}
