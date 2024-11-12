package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import java.util.List;

public class ClassStructure {

    private final String superClass;
    private final List<String> interfaces;
    private final ClassDependencyInfo dependencies;

    public ClassStructure(String superClass, List<String> interfaces, ClassDependencyInfo dependencies) {
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.dependencies = dependencies;
    }

    public String getSuperClass() {
        return superClass;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public ClassDependencyInfo getDependencies() {
        return dependencies;
    }
}
