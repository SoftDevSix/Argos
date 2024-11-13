package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import java.util.List;

public interface IExpressionCollector<T, C> {
    List<T> collectExpressions(C ctx);
}
