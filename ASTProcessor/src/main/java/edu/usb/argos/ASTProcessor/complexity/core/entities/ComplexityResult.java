package edu.usb.argos.ASTProcessor.complexity.core.entities;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ComplexityResult {
    String elementName;
    int complexityScore;
    ComplexityLevel complexityLevel;
    boolean isWithinLimits;
    List<ComplexityLocation> complexityLocations;
}
