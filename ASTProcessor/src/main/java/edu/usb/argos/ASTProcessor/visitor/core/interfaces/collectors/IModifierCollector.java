package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors;

import java.util.List;

public interface IModifierCollector<T> {
    List<String> collectModifiers(T context);
}
