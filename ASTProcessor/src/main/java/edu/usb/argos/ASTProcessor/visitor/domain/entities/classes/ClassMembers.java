package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.MethodInfo;

import java.util.List;

public class ClassMembers {

    private final List<MethodInfo> methods;
//    private final List<AttributeInfo> attributes;
//    private final List<ConstructorInfo> constructors;

//    public ClassMembers(List<MethodInfo> methods, List<AttributeInfo> attributes, List<ConstructorInfo> constructors) {
//        this.methods = methods;
//        this.attributes = attributes;
//        this.constructors = constructors;
//    }


    public ClassMembers(List<MethodInfo> methods) {
        this.methods = methods;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

//    public List<AttributeInfo> getAttributes() {
//        return attributes;
//    }
//
//    public List<ConstructorInfo> getConstructors() {
//        return constructors;
//    }
}
