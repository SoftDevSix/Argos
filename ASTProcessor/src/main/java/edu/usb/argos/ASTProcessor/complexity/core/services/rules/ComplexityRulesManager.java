package edu.usb.argos.ASTProcessor.complexity.core.services.rules;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.rules.ComplexityRulesProvider;

import java.util.Map;

public class ComplexityRulesManager {
    private ComplexityRulesProvider rulesProvider;

    public ComplexityRulesManager() {
        this.rulesProvider = new DefaultComplexityRules();
    }

    public void setRulesProvider(ComplexityRulesProvider provider) {
        this.rulesProvider = provider;
    }

    public int getMaxComplexity() {
        return rulesProvider.getMaxComplexity();
    }

    public boolean isComplexityLimitEnabled() {
        return rulesProvider.isComplexityLimitEnabled();
    }

    public ComplexityLevel determineLevel(int score) {
        Map<ComplexityLevel, Integer> thresholds = rulesProvider.getThresholds();
        if (score <= thresholds.get(ComplexityLevel.LOW)) return ComplexityLevel.LOW;
        if (score <= thresholds.get(ComplexityLevel.MEDIUM)) return ComplexityLevel.MEDIUM;
        if (score <= thresholds.get(ComplexityLevel.HIGH)) return ComplexityLevel.HIGH;
        return ComplexityLevel.VERY_HIGH;
    }
}
