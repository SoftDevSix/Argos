package edu.usb.argos.astprocessor.visitor.core.interfaces.visitor;

import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;

public interface IClassAnalyzerVisitor<T, S> {
    ClassInformation<S> visitClass(T ctx);
}
