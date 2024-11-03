package com.softdevsix.argos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softdevsix.argos.domain.BestPractices;
import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.CodeSmells;
import com.softdevsix.argos.domain.CodingStandards;
import com.softdevsix.argos.domain.Coverage;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.repository.RulesRepoImpl;

@Component
public class RulesService {

    private Rules validatedRules;

    @Autowired
    private RulesRepoImpl rulesRepo;

    public RulesService() {
        this.validatedRules = new Rules();
    }

    public void handleRules(RulesRequestMap rulesRequestMap) {
       
        if (rulesRequestMap.getCodeQuality() != null) {
            CodeQuality codeQuality = rulesRequestMap.getCodeQuality();
            
            if (codeQuality.isMaxLineLengthEnabled() && codeQuality.getMaxLineLengthLimit() <= 0) {
                throw new IllegalArgumentException("The line length limit must be positive");
            }
            validatedRules.setCodeQuality(codeQuality);
        }

       
        if (rulesRequestMap.getBestPractices() != null) {
            BestPractices bestPractices = rulesRequestMap.getBestPractices();
            validatedRules.setBestPractices(bestPractices);
        }

        
        if (rulesRequestMap.getCodeSmells() != null) {
            CodeSmells codeSmells = rulesRequestMap.getCodeSmells();
            if (codeSmells.isMethodTooLongEnabled() && codeSmells.getMaxMethodLength() <= 0) {
                throw new IllegalArgumentException("The method length limit must be positive");
            }
            validatedRules.setCodeSmells(codeSmells);
        }

        if (rulesRequestMap.getCodeComplexity() != null) {
            CodeComplexity codeComplexity = rulesRequestMap.getCodeComplexity();
            if (codeComplexity.isCyclomaticComplexityLimitEnabled() && codeComplexity.getMaxCyclomaticComplexity() <= 0) {
                throw new IllegalArgumentException("The cyclomatic complexity limit must be positive");
            }
            if (codeComplexity.isNestingDepthLimitEnabled() && codeComplexity.getMaxNestingDepth() <= 0) {
                throw new IllegalArgumentException("The nesting depth limit must be positive.");
            }
            validatedRules.setCodeComplexity(codeComplexity);
        }

        if (rulesRequestMap.getCodingStandards() != null) {
            CodingStandards codingStandards = rulesRequestMap.getCodingStandards();
            validatedRules.setCodingStandards(codingStandards);
        }

        
        if (rulesRequestMap.getCoverage() != null) {
            Coverage coverage = rulesRequestMap.getCoverage();
            if (coverage.isMinCoveragePercentageEnabled() && coverage.getCoverageThreshold() < 0) {
                throw new IllegalArgumentException("The coverage threshold must be non-negative");
            }
            validatedRules.setCoverage(coverage);
        }

        rulesRepo.createRule(validatedRules);
    }

    public Rules getRules() {
        return validatedRules;
    }
}
