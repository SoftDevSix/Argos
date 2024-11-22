package edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers;

public interface ComplexityMetric<T> {
    T analyze();
    String getMetricName();
}
