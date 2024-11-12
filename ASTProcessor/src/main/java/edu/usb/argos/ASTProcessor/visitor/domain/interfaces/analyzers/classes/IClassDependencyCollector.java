package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes;

import java.util.Set;

public interface IClassDependencyCollector <T> {

    Set<String> getImportedClasses(T ctx);
    Set<String> getUsedClasses(T ctx);
    Set<String> getMethodDependencies(T ctx);
    Set<String> getAttributeDependencies(T ctx);

}
