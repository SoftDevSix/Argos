package edu.usb.argos.ASTProcessor.visitor.domain.entities.method;

import java.util.List;

public class MethodInfo {
    private final String name;
    private final String returnType;
    private final List<String> modifiers;
    private final List<ParameterInfo> parameters;
    private final int lines;

    public MethodInfo(String name, String returnType, List<String> modifiers,
                      List<ParameterInfo> parameters, int lines) {
        this.name = name;
        this.returnType = returnType;
        this.modifiers = modifiers;
        this.parameters = parameters;
        this.lines = lines;
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

    public int getLines() {
        return lines;
    }
}
