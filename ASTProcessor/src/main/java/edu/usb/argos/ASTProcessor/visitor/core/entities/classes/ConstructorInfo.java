package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import java.util.List;

public class ConstructorInfo {
    private String name;
    private List<String> modifiers;
    private List<String> parameters;

    public ConstructorInfo(String name, List<String> modifiers, List<String> parameters) {
        this.name = name;
        this.modifiers = modifiers;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "ConstructorInfo{" +
                "name='" + name + '\'' +
                ", modifiers=" + modifiers +
                ", parameters=" + parameters +
                '}';
    }
}
