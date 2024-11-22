package edu.usb.argos.ASTProcessor.complexity.core.entities;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ComplexityLocation {
    int lineNumber;
    ComplexityType complexityType;
    String description;
    String nodeType;
}
