package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

import java.util.List;

public interface ComplexityAnalyzerStrategy<N, C> {
    int analyze(N node);
    List<C> getComplexityLocations(N node);
}
