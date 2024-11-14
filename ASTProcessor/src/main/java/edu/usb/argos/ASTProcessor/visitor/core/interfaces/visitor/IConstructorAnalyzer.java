package edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInfo;

import java.util.List;

public interface IConstructorAnalyzer<T, S> {
    List<ConstructorInfo<S>> visitConstructors(T ctx);
}
