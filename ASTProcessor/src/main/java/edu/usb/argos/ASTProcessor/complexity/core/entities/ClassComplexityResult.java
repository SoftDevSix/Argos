package edu.usb.argos.ASTProcessor.complexity.core.entities;

import java.util.List;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClassComplexityResult {
    String className;
    int totalComplexityScore;
    ComplexityLevel overallComplexityLevel;
    boolean isWithinLimits;
    List<MethodComplexityInfo> methodResults;
    List<MethodComplexityInfo> constructorResults;
}
