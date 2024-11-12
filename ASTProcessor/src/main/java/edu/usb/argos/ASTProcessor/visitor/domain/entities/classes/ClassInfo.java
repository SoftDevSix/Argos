package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import java.util.List;

public class ClassInfo {
    private final String name;
    private final List<String> modifiers;
    private final List<String> interfaces;
    private final String superClass;

    public ClassInfo(String name, List<String> modifiers, List<String> interfaces, String superClass) {
        this.name = name;
        this.modifiers = modifiers;
        this.interfaces = interfaces;
        this.superClass = superClass;
    }

    public String getName() {
        return name;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public String getSuperClass() {
        return superClass;
    }

}
