package edu.usb.argos.ASTProcessor.complexity.core.interfaces.rules;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;

import java.util.Map;

public interface ComplexityRulesProvider {
    int getMaxComplexity();
    boolean isComplexityLimitEnabled();
    Map<ComplexityLevel, Integer> getThresholds();
}
