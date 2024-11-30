package edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers;

public interface ComplexityAnalyzerStrategy<T, N> {
    T analyze(N node);
}
