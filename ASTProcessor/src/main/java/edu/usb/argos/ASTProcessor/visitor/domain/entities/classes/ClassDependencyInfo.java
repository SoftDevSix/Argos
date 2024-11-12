package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import java.util.Set;

public class ClassDependencyInfo {

    private final Set<String> importedClasses;
    private final Set<String> usedClasses;
    private final Set<String> methodDependencies;
    private final Set<String> attributeDependencies;

    public ClassDependencyInfo(Set<String> importedClasses, Set<String> usedClasses, Set<String> methodDependencies, Set<String> attributeDependencies) {
        this.importedClasses = importedClasses;
        this.usedClasses = usedClasses;
        this.methodDependencies = methodDependencies;
        this.attributeDependencies = attributeDependencies;
    }

    public Set<String> getImportedClasses() {
        return importedClasses;
    }

    public Set<String> getUsedClasses() {
        return usedClasses;
    }

    public Set<String> getMethodDependencies() {
        return methodDependencies;
    }

    public Set<String> getAttributeDependencies() {
        return attributeDependencies;
    }
}
