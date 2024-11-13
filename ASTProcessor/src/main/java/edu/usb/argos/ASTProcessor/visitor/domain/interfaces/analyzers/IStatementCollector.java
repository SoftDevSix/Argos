package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import java.util.List;

public interface IStatementCollector<T, C> {
    List<T> collectStatements(C ctx);
}
