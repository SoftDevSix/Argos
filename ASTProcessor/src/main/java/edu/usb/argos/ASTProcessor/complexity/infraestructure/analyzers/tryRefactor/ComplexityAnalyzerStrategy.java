package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.tryRefactor;

public interface ComplexityAnalyzerStrategy<T, N> {
    T analyze(N node);
}
