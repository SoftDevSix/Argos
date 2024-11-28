package edu.usb.argos.astprocessor.visitor.core.interfaces.visitor;

import edu.usb.argos.astprocessor.visitor.core.entities.classes.ConstructorInformation;

import java.util.List;

public interface IConstructorAnalyzer<T, S> {
    List<ConstructorInformation<S>> visitConstructors(T classContext);
}
