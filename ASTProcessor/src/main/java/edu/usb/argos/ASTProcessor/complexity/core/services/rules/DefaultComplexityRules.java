package edu.usb.argos.ASTProcessor.complexity.core.services.rules;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.rules.ComplexityRulesProvider;

import java.util.Map;

public class DefaultComplexityRules implements ComplexityRulesProvider {
    private static final int DEFAULT_MAX_COMPLEXITY = 10;
    private Map<ComplexityLevel, Integer> defaultThresholds;

    public DefaultComplexityRules() {
        initDefaultThresholds();
    }

    private void initDefaultThresholds() {
        this.defaultThresholds = Map.of(
                ComplexityLevel.LOW, 5,
                ComplexityLevel.MEDIUM, 10,
                ComplexityLevel.HIGH, 15,
                ComplexityLevel.VERY_HIGH, 20
        );
    }

    @Override
    public int getMaxComplexity() {
        return DEFAULT_MAX_COMPLEXITY;
    }

    @Override
    public boolean isComplexityLimitEnabled() {
        return true;
    }

    @Override
    public Map<ComplexityLevel, Integer> getThresholds() {
        return defaultThresholds;
    }
}
