package edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors;

import java.util.List;

public interface IStatementCollector<T, C> {
    List<T> collectStatements(C ctx);
}
