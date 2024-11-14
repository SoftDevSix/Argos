package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInfo;

public interface IClassAnalyzerVisitor<T> {

    ClassInfo visitClassDeclaration(T ctx);
}
