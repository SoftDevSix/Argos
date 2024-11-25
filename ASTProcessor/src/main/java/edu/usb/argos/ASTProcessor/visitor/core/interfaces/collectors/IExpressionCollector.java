package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors;

import java.util.List;

public interface IExpressionCollector<T, C> {
    List<T> collectExpressions(C ctx);
}
