package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInformation;

public interface IClassAnalyzerVisitor<T> {
    ClassInformation visitClass(T ctx);
}
