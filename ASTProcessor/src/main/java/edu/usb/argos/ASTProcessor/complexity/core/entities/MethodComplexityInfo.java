package edu.usb.argos.ASTProcessor.complexity.core.entities;

import java.util.List;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MethodComplexityInfo {
    String methodName;
    int complexityScore;
    ComplexityLevel complexityLevel;
    boolean isWithinLimits;
    List<ComplexityLocation> complexityLocations;
}
