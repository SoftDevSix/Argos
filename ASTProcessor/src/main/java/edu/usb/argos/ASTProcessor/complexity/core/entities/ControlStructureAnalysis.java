package edu.usb.argos.ASTProcessor.complexity.core.entities;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ControlStructureAnalysis {
    int totalComplexity;
    List<ComplexityLocation> locations;
}
