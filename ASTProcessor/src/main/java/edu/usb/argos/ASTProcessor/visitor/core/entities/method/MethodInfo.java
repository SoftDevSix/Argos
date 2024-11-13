package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Expression;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Token;
import java.util.List;

public class MethodInfo<S, E, T> {
    private final String name;
    private final String returnType;
    private final List<String> modifiers;
    private final List<ParameterInfo> parameters;
    private final List<Statement<S>> statements;
    private final List<Expression<E>> expressions;
    private final Token<T> tokens;

    public MethodInfo(String name, String returnType, List<String> modifiers,
                      List<ParameterInfo> parameters,
                      List<Statement<S>> statements,
                      List<Expression<E>> expressions,
                      Token<T> tokens) {
        this.name = name;
        this.returnType = returnType;
        this.modifiers = List.copyOf(modifiers);
        this.parameters = List.copyOf(parameters);
        this.statements = List.copyOf(statements);
        this.expressions = List.copyOf(expressions);
        this.tokens = tokens;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    public List<Statement<S>> getStatements() {
        return statements;
    }

    public List<Expression<E>> getExpressions() {
        return expressions;
    }

    public Token<T> getTokens() {
        return tokens;
    }
}
