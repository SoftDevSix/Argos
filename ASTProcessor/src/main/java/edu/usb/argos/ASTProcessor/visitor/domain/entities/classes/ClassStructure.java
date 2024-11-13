package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import java.util.List;

public class ClassStructure {

    private final String superClass;
    private final List<String> interfaces;

    public ClassStructure(String superClass, List<String> interfaces) {
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    public String getSuperClass() {
        return superClass;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

}
