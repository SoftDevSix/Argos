package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import java.util.List;

public class ClassIdentity {

    private final String name;
    private final String packageName;
    private final List<String> modifiers;
    private final List<AnnotationInfo> annotations;

    public ClassIdentity(String name, String packageName, List<String> modifiers, List<AnnotationInfo> annotations) {
        this.name = name;
        this.packageName = packageName;
        this.modifiers = modifiers;
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }
}
