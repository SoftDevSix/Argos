package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import java.util.ArrayList;
import java.util.List;

public class ConstructorInfo<S> {
    private String name;
    private List<String> modifiers;
    private List<String> parameters;
    private List<S> bodyStatements;

    public ConstructorInfo(String name, List<String> modifiers, List<String> parameters, List<S> bodyStatements) {
        this.name = name;
        this.modifiers = modifiers;
        this.parameters = parameters;
        this.bodyStatements = bodyStatements;
    }

    public ConstructorInfo(String name, List<String> modifiers, List<String> parameters) {
        this.name = name;
        this.modifiers = modifiers;
        this.parameters = parameters;
        this.bodyStatements = new ArrayList<>();
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

    public List<S> getBodyStatements() {
        return bodyStatements;
    }
}
