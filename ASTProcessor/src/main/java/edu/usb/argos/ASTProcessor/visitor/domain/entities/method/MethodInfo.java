package edu.usb.argos.ASTProcessor.visitor.domain.entities.method;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import org.antlr.v4.runtime.CommonTokenStream;
import java.util.List;

public class MethodInfo {
    private final String name;
    private final String returnType;
    private final List<String> modifiers;
    private final List<ParameterInfo> parameters;
    private final List<JavaParser.StatementContext> statements;
    private final List<JavaParser.ExpressionContext> expressions;
    private final CommonTokenStream tokens;

    public MethodInfo(String name, String returnType, List<String> modifiers,
                      List<ParameterInfo> parameters,
                      List<JavaParser.StatementContext> statements,
                      List<JavaParser.ExpressionContext> expressions,
                      CommonTokenStream tokens) {
        this.name = name;
        this.returnType = returnType;
        this.modifiers = List.copyOf(modifiers);
        this.parameters = List.copyOf(parameters);
        this.statements = statements;
        this.expressions = expressions;
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

    public List<JavaParser.StatementContext> getStatements() {
        return statements;
    }

    public List<JavaParser.ExpressionContext> getExpressions() {
        return expressions;
    }

    public CommonTokenStream getTokens() {
        return tokens;
    }
}
