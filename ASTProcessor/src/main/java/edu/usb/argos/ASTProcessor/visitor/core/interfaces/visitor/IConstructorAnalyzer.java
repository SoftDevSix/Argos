package edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;

import java.util.List;

public interface IConstructorAnalyzer<T, S> {
    List<ConstructorInformation<S>> visitConstructors(T classContext);
}
