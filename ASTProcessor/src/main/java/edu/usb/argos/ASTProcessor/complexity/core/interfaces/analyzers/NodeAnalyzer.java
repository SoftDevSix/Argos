package edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;

import java.util.List;

public interface NodeAnalyzer<T> {
    int analyzeNode(T node);
    List<ComplexityLocation> getComplexityLocation(T node);
}
