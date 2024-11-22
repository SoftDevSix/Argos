package edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;

public interface NodeAnalyzer<T> {
    int analyzeNode(T node);
    ComplexityLocation getComplexityLocation(T node);
}
