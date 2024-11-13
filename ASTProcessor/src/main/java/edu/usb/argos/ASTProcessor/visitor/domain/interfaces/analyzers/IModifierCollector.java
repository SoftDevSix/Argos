package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers;

import java.util.List;

public interface IModifierCollector<T> {
    List<String> collectModifiers(T context);
}
