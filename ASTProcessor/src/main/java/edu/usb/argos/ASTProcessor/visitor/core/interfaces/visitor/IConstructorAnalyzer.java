package edu.usb.argos.ASTProcessor.visitor.core.interfaces.visitor;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInfo;

import java.util.List;

public interface IConstructorAnalyzer<T> {
    List<ConstructorInfo> visitConstructors(T ctx);
}
