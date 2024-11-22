package edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;

public interface CyclomaticComplexityAnalyzer<S, E, T> extends ComplexityMetric<T> {
    int calculateComplexityScore();
    ComplexityLevel determineComplexityLevel(int score);
    boolean isWithinComplexityLimits(int score);
}
