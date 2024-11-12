package edu.usb.argos.ASTProcessor.visitor.domain.entities.method;

import java.util.List;

public class MethodInfo {
    private final String name;
    private final String returnType;
    private final List<String> modifiers;
    private final List<ParameterInfo> parameters;
    private final ComplexityMetrics complexityMetrics;
    private final CodeMetrics codeMetrics;
    private final DependencyInfo dependencies;

    public MethodInfo(String name, String returnType, List<String> modifiers,
                      List<ParameterInfo> parameters,
                      ComplexityMetrics complexityMetrics,
                      CodeMetrics codeMetrics, DependencyInfo dependencies) {
        this.name = name;
        this.returnType = returnType;
        this.modifiers = List.copyOf(modifiers);
        this.parameters = List.copyOf(parameters);
        this.complexityMetrics = complexityMetrics;
        this.codeMetrics = codeMetrics;
        this.dependencies = dependencies;
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

    public ComplexityMetrics getComplexityMetrics() {
        return complexityMetrics;
    }

    public CodeMetrics getCodeMetrics() {
        return codeMetrics;
    }

    public DependencyInfo getDependencies() {
        return dependencies;
    }
}
