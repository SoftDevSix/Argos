package com.softdevsix.argos.service;

import com.softdevsix.argos.domain.RulesRequestMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RulesService {

    private RulesRequestMap rules;
    private final List<String> validationErrors;

    public RulesService() {
        this.rules = new RulesRequestMap();
        this.validationErrors = new ArrayList<>();
    }

    
    public void handleRules(RulesRequestMap rulesRequestMap) {
        validationErrors.clear();
        
        if (rulesRequestMap.getBestPractices() != null) {
            if (rulesRequestMap.getBestPractices().isNoHardcodedValues() == null || !rulesRequestMap.getBestPractices().isNoHardcodedValues()) {
                validationErrors.add("Best Practices - Avoid hardcoded values.");
            }
        }

        if (rulesRequestMap.getCodeSmells() != null) {
            if (rulesRequestMap.getCodeSmells().isLongMethodEnabled() && rulesRequestMap.getCodeSmells().getMaxMethodLength() <= 0) {
                validationErrors.add("Code Smells - Method too long is enabled, but max length must be positive.");
            }
        }

        if (rulesRequestMap.getCodeComplexity() != null) {
            if (rulesRequestMap.getCodeComplexity().getCyclomaticComplexityLimit() <= 0) {
                validationErrors.add("Code Complexity - Cyclomatic complexity limit must be positive.");
            }
        }

        if (rulesRequestMap.getCodeQuality() != null) {
            if (rulesRequestMap.getCodeQuality().getMaxLineLength() <= 0) {
                validationErrors.add("Code Quality - Max line length must be positive.");
            }
        }

        if (rulesRequestMap.getCoverage() != null) {
            if (rulesRequestMap.getCoverage().getMinCoveragePercentage() < 0 || rulesRequestMap.getCoverage().getMinCoveragePercentage() > 100) {
                validationErrors.add("Coverage - Min coverage percentage must be between 0 and 100.");
            }
        }

        
        if (validationErrors.isEmpty()) {
            this.rules.mapFromJson(rulesRequestMap);
        }
    }

    
    public RulesRequestMap getRules() {
        return rules;
    }

   
    public List<String> getValidationErrors() {
        return validationErrors;
    }
}