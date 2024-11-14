package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInfo;

public interface IClassAnalyzerVisitor<T> {

    ClassInfo visitClassDeclaration(T ctx);
}
